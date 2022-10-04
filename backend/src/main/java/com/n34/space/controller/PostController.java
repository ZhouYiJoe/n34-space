package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.n34.space.entity.*;
import com.n34.space.entity.dto.PostDto;
import com.n34.space.entity.vo.PostVo;
import com.n34.space.service.*;
import com.n34.space.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

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
        Assert.isNull(postDto.getId(), "无权访问");
        Assert.notNull(postDto.getAuthorId(), "authorId为null");
        Assert.isTrue(postDto.getAuthorId().equals(springSecurityService.getCurrentUserId()), "无权访问");
        return postService.save(BeanCopyUtils.copyObject(postDto, Post.class));
    }

    @GetMapping
    public IPage<PostVo> findPage(@NotNull @RequestParam Long authorId,
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

            LambdaQueryWrapper<PostLike> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(PostLike::getPostId, postVo.getId());
            postVo.setNumLike(postLikeService.count(cond2));

            cond2 = new LambdaQueryWrapper<>();
            cond2.eq(PostLike::getPostId, postVo.getId());
            cond2.eq(PostLike::getUserId, springSecurityService.getCurrentUserId());
            postVo.setLikedByMe(postLikeService.count() == 1);

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
        Long currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(post.getAuthorId()), "无权访问");
        Assert.notNull(postDto.getContent(), "博文为null");
        Assert.isTrue(currentUserId.equals(postDto.getAuthorId()), "无权访问");
        return postService.updateById(BeanCopyUtils.copyObject(postDto, Post.class));
    }

    @DeleteMapping("/{id}")
    public Boolean remove(@NotNull @PathVariable Long id) {
        Post post = postService.getById(id);
        Assert.notNull(post, "博文不存在");
        Assert.isTrue(springSecurityService.getCurrentUserId().equals(post.getAuthorId()), "无权访问");
        return postService.removeById(id);
    }
}
