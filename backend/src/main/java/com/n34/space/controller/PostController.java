package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.n34.space.entity.*;
import com.n34.space.entity.dto.PostDto;
import com.n34.space.entity.vo.PostVo;
import com.n34.space.service.*;
import com.n34.space.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final SpringSecurityService springSecurityService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;
    private final HashtagService hashtagService;
    private final HashtagPostRelaService hashtagPostRelaService;

    @PostMapping
    public Boolean postNew(@RequestBody PostDto postDto) {
        postDto.setId(null);
        Set<String> hashtagNames = RegexUtils.getAllHashtag(postDto.getContent());
        Post post = BeanCopyUtils.copyObject(postDto, Post.class);
        post.setAuthorId(springSecurityService.getCurrentUserId());
        post.setCategory(AiUtils.getCategory(post.getContent()));
        post.setExtreme(AiUtils.getSentiment(post.getContent()));
        boolean successful = postService.save(post);
        for (String hashtagName : hashtagNames) {
            LambdaQueryWrapper<Hashtag> cond = new LambdaQueryWrapper<>();
            cond.eq(Hashtag::getName, hashtagName);
            Hashtag hashtag = hashtagService.getOne(cond);
            if (hashtag == null) {
                hashtag = new Hashtag().setName(hashtagName);
                hashtagService.save(hashtag);
            }
            HashtagPostRela hashtagPostRela = new HashtagPostRela()
                    .setHashtagId(hashtag.getId())
                    .setPostId(post.getId());
            hashtagPostRelaService.save(hashtagPostRela);
        }
        return successful;
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

            postVo.setContent(RegexUtils.parseHashtag(postVo.getContent()));
        }

        return postVos;
    }

    @PutMapping
    public Boolean update(@RequestBody PostDto postDto) {
        Assert.notNull(postDto.getId(), "ID为null");
        Post post = postService.getById(postDto.getId());
        Set<String> hashtagNames = RegexUtils.getAllHashtag(post.getContent());
        for (String hashtagName : hashtagNames) {
            LambdaQueryWrapper<Hashtag> cond = new LambdaQueryWrapper<>();
            cond.eq(Hashtag::getName, hashtagName);
            Hashtag hashtag = hashtagService.getOne(cond);
            LambdaQueryWrapper<HashtagPostRela> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(HashtagPostRela::getPostId, post.getId());
            cond2.eq(HashtagPostRela::getHashtagId, hashtag.getId());
            hashtagPostRelaService.remove(cond2);
            cond2 = new LambdaQueryWrapper<>();
            cond2.eq(HashtagPostRela::getHashtagId, hashtag.getId());
            int count = hashtagPostRelaService.count(cond2);
            if (count == 0) {
                hashtagService.removeById(hashtag.getId());
            }
        }
        Assert.notNull(post, "博文不存在");
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(post.getAuthorId()), "无权访问");
        Assert.notNull(postDto.getContent(), "博文为null");
        Post post1 = BeanCopyUtils.copyObject(postDto, Post.class);
        post1.setCategory(AiUtils.getCategory(post1.getContent()));
        post1.setExtreme(AiUtils.getSentiment(post1.getContent()));
        hashtagNames = RegexUtils.getAllHashtag(post1.getContent());
        for (String hashtagName : hashtagNames) {
            LambdaQueryWrapper<Hashtag> cond = new LambdaQueryWrapper<>();
            cond.eq(Hashtag::getName, hashtagName);
            Hashtag hashtag = hashtagService.getOne(cond);
            if (hashtag == null) {
                hashtag = new Hashtag().setName(hashtagName);
                hashtagService.save(hashtag);
            }
            HashtagPostRela hashtagPostRela = new HashtagPostRela()
                    .setHashtagId(hashtag.getId())
                    .setPostId(post.getId());
            hashtagPostRelaService.save(hashtagPostRela);
        }
        return postService.updateById(post1);
    }

    @DeleteMapping("/{id}")
    public Boolean remove(@NotNull @PathVariable String id) {
        Post post = postService.getById(id);
        Assert.notNull(post, "博文不存在");
        Assert.isTrue(springSecurityService.getCurrentUserId().equals(post.getAuthorId()), "无权访问");
        Set<String> hashtagNames = RegexUtils.getAllHashtag(post.getContent());
        for (String hashtagName : hashtagNames) {
            LambdaQueryWrapper<Hashtag> cond = new LambdaQueryWrapper<>();
            cond.eq(Hashtag::getName, hashtagName);
            Hashtag hashtag = hashtagService.getOne(cond);
            LambdaQueryWrapper<HashtagPostRela> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(HashtagPostRela::getPostId, post.getId());
            cond2.eq(HashtagPostRela::getHashtagId, hashtag.getId());
            hashtagPostRelaService.remove(cond2);
            cond2 = new LambdaQueryWrapper<>();
            cond2.eq(HashtagPostRela::getHashtagId, hashtag.getId());
            int count = hashtagPostRelaService.count(cond2);
            if (count == 0) {
                hashtagService.removeById(hashtag.getId());
            }
        }
        return postService.removeById(id);
    }

    @GetMapping("/hot")
    public List<PostVo> findHot(@RequestParam String searchText) {
        if (searchText != null) {
            searchText = RegexUtils.correctSearchText(searchText);
            if (!StringUtils.hasText(searchText)) {
                searchText = null;
            }
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
            postVo.setContent(RegexUtils.parseHashtag(postVo.getContent()));
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

            postVo.setContent(RegexUtils.parseHashtag(postVo.getContent()));
        }

        return postVos;
    }

    @GetMapping("/latest")
    public List<PostVo> getLatest(@RequestParam String searchText) {
        if (searchText != null) {
            searchText = RegexUtils.correctSearchText(searchText);
        }
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

            if (StringUtils.hasText(searchText)) {
                String content = postVo.getContent();
                postVo.setContent(FontUtils.emphasize(content, searchText));
            }

            postVo.setContent(RegexUtils.parseHashtag(postVo.getContent()));
        }
        return postVos;
    }
}
