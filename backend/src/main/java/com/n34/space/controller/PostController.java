package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.n34.space.entity.Comment;
import com.n34.space.entity.Post;
import com.n34.space.entity.PostLike;
import com.n34.space.entity.User;
import com.n34.space.entity.dto.PostDto;
import com.n34.space.entity.vo.PostVo;
import com.n34.space.service.*;
import com.n34.space.utils.AiUtils;
import com.n34.space.utils.BeanCopyUtils;
import com.n34.space.utils.ConditionUtils;
import com.n34.space.utils.FontUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
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
        post.setCategory(AiUtils.getCategory(post.getContent()));
        post.setExtreme(AiUtils.getSentiment(post.getContent()));
        return postService.save(post);
    }

    @GetMapping
    public List<PostVo> findForUser(@NotNull @RequestParam String authorId,
                                    @NotNull @RequestParam Boolean filtered) {
        String currentUserId = springSecurityService.getCurrentUserId();
        String filterConfig = userService.getById(currentUserId).getFilterConfig();
        QueryWrapper<Post> cond = new QueryWrapper<>();
        cond.eq("author_id", authorId);
        if (filtered) {
            String filterExtremeSql = ConditionUtils.filterExtreme(filterConfig);
            cond.last(filterExtremeSql + " order by time_created desc");
        } else {
            cond.last(" order by time_created desc");
        }
        List<Post> page = postService.list(cond);
        List<PostVo> postVos = BeanCopyUtils.copyList(page, PostVo.class);
        User author = userService.getById(authorId);
        Assert.notNull(author, "博文的作者不存在");

        for (PostVo postVo : postVos) {
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

        return postVos;
    }

    @PutMapping
    public Boolean update(@RequestBody PostDto postDto) {
        Assert.notNull(postDto.getId(), "ID为null");
        Post post = postService.getById(postDto.getId());
        Assert.notNull(post, "博文不存在");
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(post.getAuthorId()), "无权访问");
        Assert.notNull(postDto.getContent(), "博文为null");
        Post post1 = BeanCopyUtils.copyObject(postDto, Post.class);
        post1.setCategory(AiUtils.getCategory(post1.getContent()));
        post1.setExtreme(AiUtils.getSentiment(post1.getContent()));
        return postService.updateById(post1);
    }

    @DeleteMapping("/{id}")
    public Boolean remove(@NotNull @PathVariable String id) {
        Post post = postService.getById(id);
        Assert.notNull(post, "博文不存在");
        Assert.isTrue(springSecurityService.getCurrentUserId().equals(post.getAuthorId()), "无权访问");
        return postService.removeById(id);
    }

    @GetMapping("/hot")
    public List<PostVo> findHot(@RequestParam String searchText) {
        if (!StringUtils.hasText(searchText)) {
            searchText = null;
        }
        String currentUserId = springSecurityService.getCurrentUserId();
        String filterConfig = userService.getById(currentUserId).getFilterConfig();
        List<String> categories = AiUtils.getAllCategories();
        List<PostVo> postVos = postService.findHot(searchText, categories, filterConfig);
        for (PostVo postVo : postVos) {
            LambdaQueryWrapper<PostLike> cond = new LambdaQueryWrapper<>();
            cond.eq(PostLike::getPostId, postVo.getId())
                    .eq(PostLike::getUserId, springSecurityService.getCurrentUserId());
            postVo.setLikedByMe(postLikeService.count(cond) == 1);
            if (searchText != null) {
                String content = postVo.getContent();
                postVo.setContent(FontUtils.emphasize(content, searchText));
            }
        }
        return postVos;
    }

    @GetMapping("/followee_posts")
    public List<PostVo> getFolloweePosts() {
        String currentUserId = springSecurityService.getCurrentUserId();
        String filterConfig = userService.getById(currentUserId).getFilterConfig();
        List<String> categories = AiUtils.getAllCategories();
        List<Post> posts = postService.getFolloweePosts(currentUserId, categories, filterConfig);
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

        return postVos;
    }

    @GetMapping("/latest")
    public List<PostVo> getLatest(@RequestParam String searchText) {
        LambdaQueryWrapper<Post> cond = new LambdaQueryWrapper<>();
        cond.orderByDesc(Post::getTimeUpdated);
        if (StringUtils.hasText(searchText)) {
            cond.like(Post::getContent, searchText);
        }
        List<Post> posts = postService.list(cond);
        String currentUserId = springSecurityService.getCurrentUserId();
        String filterConfig = userService.getById(currentUserId).getFilterConfig();
        List<String> categories = AiUtils.getAllCategories();
        List<Post> posts2 = new ArrayList<>();
        for (Post post : posts) {
            int i = categories.indexOf(post.getCategory());
            if (filterConfig.charAt(i) == '0' || !post.getExtreme() || post.getAuthorId().equals(currentUserId)) {
                posts2.add(post);
            }
        }

        List<PostVo> postVos = BeanCopyUtils.copyList(posts2, PostVo.class);
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
        return postVos;
    }
}
