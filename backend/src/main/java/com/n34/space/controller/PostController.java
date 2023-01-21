package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.n34.space.entity.Comment;
import com.n34.space.entity.Post;
import com.n34.space.entity.PostLike;
import com.n34.space.entity.User;
import com.n34.space.entity.dto.PostDto;
import com.n34.space.entity.vo.PostVo;
import com.n34.space.service.*;
import com.n34.space.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final SpringSecurityService springSecurityService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;

    @PostMapping
    public Boolean postNew(@RequestBody PostDto postDto) {
        postDto.setId(null);
        Post post = BeanCopyUtils.copyObject(postDto, Post.class);
        post.setAuthorId(springSecurityService.getCurrentUserId());
        return postService.save(post);
    }

    @GetMapping
    public IPage<PostVo> findPage(@NotNull @RequestParam String authorId,
                                  @NotNull @RequestParam Integer pageNo,
                                  @NotNull @RequestParam Integer pageSize) {
        LambdaQueryWrapper<Post> cond = new LambdaQueryWrapper<>();
        cond.eq(Post::getAuthorId, authorId);
        cond.orderByDesc(Post::getTimeCreated);
        IPage<Post> page = postService.page(new Page<>(pageNo, pageSize), cond);
        IPage<PostVo> postVoPage = BeanCopyUtils.copyPage(page, PostVo.class);
        User author = userService.getById(authorId);
        Assert.notNull(author, "博文的作者不存在");

        for (PostVo postVo : postVoPage.getRecords()) {
            postVo.setAuthorNickname(author.getNickname());
            postVo.setAuthorUsername(author.getUsername());
            postVo.setAuthorAvatarFilename(author.getAvatarFilename());

            LambdaQueryWrapper<PostLike> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(PostLike::getPostId, postVo.getId());
            postVo.setNumLike(postLikeService.count(cond2));

            cond2 = new LambdaQueryWrapper<>();
            cond2.eq(PostLike::getPostId, postVo.getId());
            cond2.eq(PostLike::getUserId, springSecurityService.getCurrentUserId());
            postVo.setLikedByMe(postLikeService.count(cond2) == 1);

            LambdaQueryWrapper<Comment> cond3 = new LambdaQueryWrapper<>();
            cond3.eq(Comment::getPostId, postVo.getId());
            postVo.setNumComment(commentService.count(cond3));
        }

        return postVoPage;
    }

    @PutMapping
    public Boolean update(@RequestBody PostDto postDto) {
        Assert.notNull(postDto.getId(), "ID为null");
        Post post = postService.getById(postDto.getId());
        Assert.notNull(post, "博文不存在");
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(post.getAuthorId()), "无权访问");
        Assert.notNull(postDto.getContent(), "博文为null");
        return postService.updateById(BeanCopyUtils.copyObject(postDto, Post.class));
    }

    @DeleteMapping("/{id}")
    public Boolean remove(@NotNull @PathVariable String id) {
        Post post = postService.getById(id);
        Assert.notNull(post, "博文不存在");
        Assert.isTrue(springSecurityService.getCurrentUserId().equals(post.getAuthorId()), "无权访问");
        return postService.removeById(id);
    }

    @GetMapping("/hot")
    IPage<PostVo> findHot(@NotNull @RequestParam Integer pageNo,
                          @NotNull @RequestParam Integer pageSize) {
        List<PostVo> postVos = postService.findHot((pageNo - 1) * pageSize, pageSize);

        for (PostVo postVo : postVos) {
            LambdaQueryWrapper<PostLike> cond = new LambdaQueryWrapper<>();
            cond.eq(PostLike::getPostId, postVo.getId())
                    .eq(PostLike::getUserId, springSecurityService.getCurrentUserId());
            postVo.setLikedByMe(postLikeService.count(cond) == 1);
        }
        IPage<PostVo> page = new Page<>();
        page.setRecords(postVos);
        int count = postService.count();
        page.setPages((int) Math.ceil((double) count / pageSize));
        page.setTotal(count);
        page.setSize(pageSize);
        return page;
    }

    @GetMapping("/followee_posts")
    public IPage<PostVo> getFolloweePosts(@NotNull @RequestParam Integer pageNo,
                                         @NotNull @RequestParam Integer pageSize) {
        String currentUserId = springSecurityService.getCurrentUserId();
        List<Post> posts = postService.getFolloweePosts(
                currentUserId, (pageNo - 1) * pageSize, pageSize);
        List<PostVo> postVos = BeanCopyUtils.copyList(posts, PostVo.class);

        for (PostVo postVo : postVos) {
            User author = userService.getById(postVo.getAuthorId());
            postVo.setAuthorNickname(author.getNickname());
            postVo.setAuthorUsername(author.getUsername());
            postVo.setAuthorAvatarFilename(author.getAvatarFilename());

            LambdaQueryWrapper<PostLike> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(PostLike::getPostId, postVo.getId());
            postVo.setNumLike(postLikeService.count(cond2));

            cond2 = new LambdaQueryWrapper<>();
            cond2.eq(PostLike::getPostId, postVo.getId());
            cond2.eq(PostLike::getUserId, springSecurityService.getCurrentUserId());
            postVo.setLikedByMe(postLikeService.count(cond2) == 1);

            LambdaQueryWrapper<Comment> cond3 = new LambdaQueryWrapper<>();
            cond3.eq(Comment::getPostId, postVo.getId());
            postVo.setNumComment(commentService.count(cond3));
        }

        IPage<PostVo> page = new Page<>();
        page.setRecords(postVos);
        int count = postService.countFolloweePosts(currentUserId);
        page.setPages((int) Math.ceil((double) count / pageSize));
        page.setTotal(count);
        page.setSize(pageSize);
        return page;
    }
}
