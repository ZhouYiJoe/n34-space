package com.n34.space.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.n34.space.entity.*;
import com.n34.space.entity.dto.PostDto;
import com.n34.space.entity.vo.PostVo;
import com.n34.space.service.*;
import com.n34.space.utils.AiUtils;
import com.n34.space.utils.BeanCopyUtils;
import com.n34.space.utils.ConditionUtils;
import com.n34.space.utils.RegexUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final MentionNotificationService mentionNotificationService;

    @GetMapping("/{id}")
    public PostVo getById(@PathVariable String id) {
        Post post = postService.getById(id);
        PostVo postVo = BeanCopyUtils.copyObject(post, PostVo.class);
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

        postVo.setHtml(RegexUtils.parseHashtag(postVo.getContent()));
        postVo.setHtml(RegexUtils.parseMentionedUsername(postVo.getHtml()));
        return postVo;
    }

    @PostMapping
    public Boolean postNew(@RequestBody PostDto postDto) {
        String currentUserId = springSecurityService.getCurrentUserId();
        postDto.setId(null);
        Set<String> hashtagNames = RegexUtils.getAllHashtag(postDto.getContent());
        if (!RegexUtils.checkHashtag(hashtagNames)) {
            throw new RuntimeException("话题标签中不能带有\"@\"");
        }
        Set<String> atUsernames = RegexUtils.getMentionedUsernames(postDto.getContent());
        if (!RegexUtils.checkMentionedUsername(atUsernames)) {
            throw new RuntimeException("被@的用户名中不能带有\"#\"");
        }
        Post post = BeanCopyUtils.copyObject(postDto, Post.class);
        post.setAuthorId(currentUserId);
        post.setCategory(AiUtils.getCategory(post.getContent()));
        post.setExtreme(AiUtils.getSentiment(post.getContent()));
        if (!postService.save(post)) {
            return false;
        }
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
        for (String mentionedUsername : atUsernames) {
            LambdaQueryWrapper<User> cond = new LambdaQueryWrapper<>();
            cond.eq(User::getUsername, mentionedUsername);
            User mentionedUser = userService.getOne(cond);
            MentionNotification mentionNotification = new MentionNotification()
                    .setMentionedUserId(mentionedUser.getId())
                    .setTextId(post.getId())
                    .setType(MentionNotification.POST_TYPE)
                    .setRead(false);
            mentionNotificationService.save(mentionNotification);
        }
        return true;
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

            postVo.setHtml(RegexUtils.parseHashtag(postVo.getContent()));
            postVo.setHtml(RegexUtils.parseMentionedUsername(postVo.getHtml()));
        }

        postVos = filterByCircle(postVos, null);

        return postVos;
    }

    @PutMapping
    public PostVo update(@RequestBody PostDto postDto) {
        Assert.notNull(postDto.getId(), "ID为null");
        Post post = postService.getById(postDto.getId());
        Assert.notNull(post, "博文不存在");
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(post.getAuthorId()), "无权访问");
        Assert.notNull(postDto.getContent(), "博文为null");
        Set<String> oldHashtagNames = RegexUtils.getAllHashtag(post.getContent());
        Set<String> newHashtagNames = RegexUtils.getAllHashtag(postDto.getContent());
        if (!RegexUtils.checkHashtag(newHashtagNames)) {
            throw new RuntimeException("话题标签中不能带有\"@\"");
        }
        Set<String> oldMentionedUsernames = RegexUtils.getMentionedUsernames(post.getContent());
        Set<String> newMentionedUsernames = RegexUtils.getMentionedUsernames(postDto.getContent());
        if (!RegexUtils.checkMentionedUsername(newMentionedUsernames)) {
            throw new RuntimeException("被@的用户名中不能带有\"#\"");
        }
        for (String hashtagName : oldHashtagNames) {
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
        Post post1 = BeanCopyUtils.copyObject(postDto, Post.class);
        post1.setCategory(AiUtils.getCategory(post1.getContent()));
        post1.setExtreme(AiUtils.getSentiment(post1.getContent()));
        for (String hashtagName : newHashtagNames) {
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
        if (!postService.updateById(post1)) {
            return null;
        }

        // 构造PostVo
        post = postService.getById(post1.getId());
        PostVo postVo = BeanCopyUtils.copyObject(post, PostVo.class);
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

        postVo.setHtml(RegexUtils.parseHashtag(postVo.getContent()));
        postVo.setHtml(RegexUtils.parseMentionedUsername(postVo.getHtml()));

        //解析被提及的用户
        Set<String> addedMentionedUsernames = new HashSet<>(newMentionedUsernames);
        addedMentionedUsernames.removeAll(oldMentionedUsernames);
        Set<String> deletedMentionedUsernames = new HashSet<>(oldMentionedUsernames);
        deletedMentionedUsernames.removeAll(newMentionedUsernames);
        for (String addedMentionedUsername : addedMentionedUsernames) {
            LambdaQueryWrapper<User> cond = new LambdaQueryWrapper<>();
            cond.eq(User::getUsername, addedMentionedUsername);
            User mentionedUser = userService.getOne(cond);
            MentionNotification mentionNotification = new MentionNotification()
                    .setMentionedUserId(mentionedUser.getId())
                    .setTextId(post.getId())
                    .setType(MentionNotification.POST_TYPE)
                    .setRead(false);
            mentionNotificationService.save(mentionNotification);
        }
        for (String deletedMentionedUsername : deletedMentionedUsernames) {
            LambdaQueryWrapper<User> cond = new LambdaQueryWrapper<>();
            cond.eq(User::getUsername, deletedMentionedUsername);
            User mentionedUser = userService.getOne(cond);
            LambdaQueryWrapper<MentionNotification> cond1 = new LambdaQueryWrapper<>();
            cond1.eq(MentionNotification::getTextId, postDto.getId());
            cond1.eq(MentionNotification::getMentionedUserId, mentionedUser.getId());
            cond1.eq(MentionNotification::getType, MentionNotification.POST_TYPE);
            mentionNotificationService.remove(cond1);
        }

        return postVo;
    }

    @DeleteMapping("/{id}")
    public Boolean remove(@NotNull @PathVariable String id) {
        Post post = postService.getById(id);
        Assert.notNull(post, "博文不存在");
        Assert.isTrue(springSecurityService.getCurrentUserId().equals(post.getAuthorId()), "无权访问");

        LambdaQueryWrapper<PostLike> cond = new LambdaQueryWrapper<>();
        cond.eq(PostLike::getPostId, id);
        postLikeService.remove(cond);
        if (!postService.removeById(id)) {
            return false;
        }

        Set<String> hashtagNames = RegexUtils.getAllHashtag(post.getContent());
        for (String hashtagName : hashtagNames) {
            LambdaQueryWrapper<Hashtag> cond1 = new LambdaQueryWrapper<>();
            cond1.eq(Hashtag::getName, hashtagName);
            Hashtag hashtag = hashtagService.getOne(cond1);
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

        Set<String> mentionedUsernames = RegexUtils.getMentionedUsernames(post.getContent());
        for (String mentionedUsername : mentionedUsernames) {
            LambdaQueryWrapper<User> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(User::getUsername, mentionedUsername);
            User mentionedUser = userService.getOne(cond2);
            LambdaQueryWrapper<MentionNotification> cond1 = new LambdaQueryWrapper<>();
            cond1.eq(MentionNotification::getTextId, post.getId());
            cond1.eq(MentionNotification::getMentionedUserId, mentionedUser.getId());
            cond1.eq(MentionNotification::getType, MentionNotification.POST_TYPE);
            mentionNotificationService.remove(cond1);
        }

        return true;
    }

    @GetMapping("/hot")
    public List<PostVo> findHot(@RequestParam(required = false) String searchText,
                                @RequestParam(required = false) String hashtag,
                                @RequestParam(required = false) String circleId) {
        if (!StringUtils.hasText(searchText)) {
            searchText = null;
        }
        if (!StringUtils.hasText(hashtag)) {
            hashtag = null;
        }
        String currentUserId = springSecurityService.getCurrentUserId();
        String filterConfig = userService.getById(currentUserId).getFilterConfig();
        List<String> categories = AiUtils.getAllCategories();
        List<PostVo> postVos = postService.findHot(searchText, hashtag, categories, filterConfig);
        for (PostVo postVo : postVos) {
            LambdaQueryWrapper<PostLike> cond = new LambdaQueryWrapper<>();
            cond.eq(PostLike::getPostId, postVo.getId())
                    .eq(PostLike::getUserId, springSecurityService.getCurrentUserId());
            postVo.setLikedByMe(postLikeService.count(cond) == 1);
            postVo.setHtml(RegexUtils.parseHashtag(postVo.getContent()));
            postVo.setHtml(RegexUtils.parseMentionedUsername(postVo.getHtml()));
        }
        postVos = filterByCircle(postVos, circleId);
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

            postVo.setHtml(RegexUtils.parseHashtag(postVo.getContent()));
            postVo.setHtml(RegexUtils.parseMentionedUsername(postVo.getHtml()));
        }
        postVos = filterByCircle(postVos, null);
        return postVos;
    }

    @GetMapping("/latest")
    public List<PostVo> getLatest(@RequestParam(required = false) String searchText,
                                  @RequestParam(required = false) String hashtag,
                                  @RequestParam(required = false) String circleId) {
        LambdaQueryWrapper<Post> cond = new LambdaQueryWrapper<>();
        cond.orderByDesc(Post::getTimeUpdated);
        if (StringUtils.hasText(searchText)) {
            cond.like(Post::getContent, searchText);
        }
        if (StringUtils.hasText(hashtag)) {
            cond.like(Post::getContent, "#" + hashtag + "#");
        }
        List<Post> posts = postService.list(cond);
        String currentUserId = springSecurityService.getCurrentUserId();
        String filterConfig = userService.getById(currentUserId).getFilterConfig();
        List<String> categories = AiUtils.getAllCategories();
        posts = posts.stream().filter(post -> !post.getExtreme()
                        || post.getAuthorId().equals(currentUserId)
                        || filterConfig.charAt(categories.indexOf(post.getCategory())) == '0')
                .collect(Collectors.toList());
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

            postVo.setHtml(RegexUtils.parseHashtag(postVo.getContent()));
            postVo.setHtml(RegexUtils.parseMentionedUsername(postVo.getHtml()));
        }
        postVos = filterByCircle(postVos, circleId);
        return postVos;
    }

    private List<PostVo> filterByCircle(List<PostVo> postVos, String circleId) {
        return postVos.stream()
                .filter(postVo -> StrUtil.equals(postVo.getCircleId(), circleId))
                .collect(Collectors.toList());
    }
}
