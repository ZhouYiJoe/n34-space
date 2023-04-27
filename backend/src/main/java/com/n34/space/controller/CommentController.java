package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.n34.space.entity.*;
import com.n34.space.entity.dto.CommentDto;
import com.n34.space.entity.vo.CommentVo;
import com.n34.space.service.*;
import com.n34.space.utils.AiUtils;
import com.n34.space.utils.BeanCopyUtils;
import com.n34.space.utils.ConditionUtils;
import com.n34.space.utils.RegexUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Validated
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    private final SpringSecurityService springSecurityService;
    private final UserService userService;
    private final CommentReplyService commentReplyService;
    private final MentionNotificationService mentionNotificationService;
    private final PostService postService;
    private final ReplyNotificationService replyNotificationService;

    @GetMapping("/{id}")
    public CommentVo getById(@PathVariable String id) {
        Comment comment = commentService.getById(id);
        CommentVo commentVo = BeanCopyUtils.copyObject(comment, CommentVo.class);
        User user = userService.getById(commentVo.getUserId());
        commentVo.setNickname(user.getNickname());
        commentVo.setUsername(user.getUsername());
        commentVo.setAvatarFilename(user.getAvatarFilename());

        LambdaQueryWrapper<CommentLike> cond2 = new LambdaQueryWrapper<>();
        cond2.eq(CommentLike::getCommentId, commentVo.getId());
        commentVo.setNumLike(commentLikeService.count(cond2));

        cond2 = new LambdaQueryWrapper<>();
        cond2.eq(CommentLike::getCommentId, commentVo.getId());
        cond2.eq(CommentLike::getUserId, springSecurityService.getCurrentUserId());
        commentVo.setLikedByMe(commentLikeService.count(cond2) == 1);

        LambdaQueryWrapper<CommentReply> cond3 = new LambdaQueryWrapper<>();
        cond3.eq(CommentReply::getCommentId, commentVo.getId());
        commentVo.setNumReply(commentReplyService.count(cond3));

        commentVo.setHtml(RegexUtils.parseMentionedUsername(commentVo.getContent()));
        return commentVo;
    }

    @PostMapping
    public Boolean postNew(@RequestBody CommentDto commentDto) {
        Assert.notNull(commentDto.getUserId(), "userId为null");
        Assert.notNull(commentDto.getPostId(), "postId为null");
        Assert.isNull(commentDto.getId(), "无权访问");
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(commentDto.getUserId().equals(currentUserId), "无权访问");
        Comment comment = BeanCopyUtils.copyObject(commentDto, Comment.class);
        comment.setCategory(AiUtils.getCategory(comment.getContent()));
        comment.setExtreme(AiUtils.getSentiment(comment.getContent()));
        if (!commentService.save(comment)) {
            return false;
        }

        Set<String> mentionedUsernames = RegexUtils.getMentionedUsernames(comment.getContent());
        for (String mentionedUsername : mentionedUsernames) {
            LambdaQueryWrapper<User> cond = new LambdaQueryWrapper<>();
            cond.eq(User::getUsername, mentionedUsername);
            User mentionedUser = userService.getOne(cond);
            MentionNotification mentionNotification = new MentionNotification()
                    .setMentionedUserId(mentionedUser.getId())
                    .setTextId(comment.getId())
                    .setType(MentionNotification.COMMENT_TYPE)
                    .setRead(false);
            mentionNotificationService.save(mentionNotification);
        }

        Post post = postService.getById(commentDto.getPostId());
        User repliedUser = userService.getById(post.getAuthorId());
        ReplyNotification replyNotification = new ReplyNotification()
                .setRepliedUserId(repliedUser.getId())
                .setReplyUserId(currentUserId)
                .setRepliedTextId(post.getId())
                .setReplyTextId(comment.getId())
                .setRead(false)
                .setType(ReplyNotification.COMMENT_TYPE);
        replyNotificationService.save(replyNotification);

        return true;
    }

    @GetMapping
    public List<CommentVo> getList(@RequestParam(required = false) String postId,
                                   @RequestParam(required = false) String timeSortOrder) {
        String currentUserId = springSecurityService.getCurrentUserId();
        String filterConfig = userService.getById(currentUserId).getFilterConfig();
        QueryWrapper<Comment> cond = new QueryWrapper<>();
        cond.eq("post_id", postId);
        String filterExtremeSql = ConditionUtils.filterExtreme(filterConfig);
        if ("asc".equals(timeSortOrder)) {
            cond.last(filterExtremeSql + "order by time_created");
        } else if ("desc".equals(timeSortOrder)) {
            cond.last(filterExtremeSql + "order by time_created desc");
        } else {
            cond.last(filterExtremeSql);
        }
        List<Comment> comments = commentService.list(cond);
        List<CommentVo> commentVos = BeanCopyUtils.copyList(comments, CommentVo.class);

        for (CommentVo commentVo : commentVos) {
            String userId = commentVo.getUserId();
            Assert.notNull(userId, "userId为null");
            User user = userService.getById(userId);
            Assert.notNull(user, "user为null");
            commentVo.setUsername(user.getUsername());
            commentVo.setNickname(user.getNickname());
            commentVo.setAvatarFilename(user.getAvatarFilename());

            LambdaQueryWrapper<CommentLike> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(CommentLike::getCommentId, commentVo.getId());
            commentVo.setNumLike(commentLikeService.count(cond2));

            cond2 = new LambdaQueryWrapper<>();
            cond2.eq(CommentLike::getCommentId, commentVo.getId());
            cond2.eq(CommentLike::getUserId, springSecurityService.getCurrentUserId());
            commentVo.setLikedByMe(commentLikeService.count(cond2) == 1);

            LambdaQueryWrapper<CommentReply> cond3 = new LambdaQueryWrapper<>();
            cond3.eq(CommentReply::getCommentId, commentVo.getId());
            commentVo.setNumReply(commentReplyService.count(cond3));

            commentVo.setHtml(RegexUtils.parseMentionedUsername(commentVo.getContent()));
        }

        return commentVos;
    }

    @PutMapping
    public Boolean update(@RequestBody CommentDto commentDto) {
        Assert.notNull(commentDto.getId(), "id为null");
        Comment comment = commentService.getById(commentDto.getId());
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(comment.getUserId()), "无权访问");
        Assert.isTrue(currentUserId.equals(commentDto.getUserId()), "无权访问");
        Assert.isNull(commentDto.getPostId(), "无权访问");
        Comment comment1 = BeanCopyUtils.copyObject(commentDto, Comment.class);
        comment1.setCategory(AiUtils.getCategory(comment1.getContent()));
        comment1.setExtreme(AiUtils.getSentiment(comment1.getContent()));
        return commentService.updateById(comment1);
    }

    @DeleteMapping("/{id}")
    public Boolean remove(@NotNull @PathVariable String id) {
        Comment comment = commentService.getById(id);
        Assert.notNull(comment, "评论不存在");
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(comment.getUserId()), "无权访问");

        LambdaQueryWrapper<CommentLike> cond = new LambdaQueryWrapper<>();
        cond.eq(CommentLike::getCommentId, id);
        commentLikeService.remove(cond);
        if (!commentService.removeById(id)) {
            return false;
        }

        Set<String> mentionedUsernames = RegexUtils.getMentionedUsernames(comment.getContent());
        for (String mentionedUsername : mentionedUsernames) {
            LambdaQueryWrapper<User> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(User::getUsername, mentionedUsername);
            User mentionedUser = userService.getOne(cond2);
            LambdaQueryWrapper<MentionNotification> cond1 = new LambdaQueryWrapper<>();
            cond1.eq(MentionNotification::getTextId, comment.getId());
            cond1.eq(MentionNotification::getMentionedUserId, mentionedUser.getId());
            cond1.eq(MentionNotification::getType, MentionNotification.COMMENT_TYPE);
            mentionNotificationService.remove(cond1);
        }

        replyNotificationService.lambdaUpdate()
                .eq(ReplyNotification::getReplyTextId, id)
                .eq(ReplyNotification::getType, ReplyNotification.COMMENT_TYPE)
                .remove();
        replyNotificationService.lambdaUpdate()
                .eq(ReplyNotification::getRepliedTextId, id)
                .eq(ReplyNotification::getType, ReplyNotification.REPLY_TYPE)
                .remove();
        mentionNotificationService.lambdaUpdate()
                .eq(MentionNotification::getTextId, id)
                .eq(MentionNotification::getType, MentionNotification.COMMENT_TYPE)
                .remove();

        return true;
    }
}
