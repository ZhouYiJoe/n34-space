package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.n34.space.entity.*;
import com.n34.space.entity.vo.MentionNotificationVo;
import com.n34.space.service.*;
import com.n34.space.utils.AiUtils;
import com.n34.space.utils.BeanCopyUtils;
import com.n34.space.utils.RegexUtils;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mention_notification")
@RequiredArgsConstructor
public class MentionNotificationController {

    private final SpringSecurityService springSecurityService;
    private final MentionNotificationService mentionNotificationService;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final CommentReplyService commentReplyService;

    @GetMapping("/countNewNotification")
    public Long countNewNotification() {
        String currentUserId = springSecurityService.getCurrentUserId();
        String filterConfig = userService.getById(currentUserId).getFilterConfig();
        List<String> categories = AiUtils.getAllCategories();
        LambdaQueryWrapper<MentionNotification> cond = new LambdaQueryWrapper<>();
        cond.select(MentionNotification::getTextId, MentionNotification::getType);
        cond.eq(MentionNotification::getMentionedUserId, currentUserId);
        cond.eq(MentionNotification::getRead, false);
        List<MentionNotification> mentionNotifications = mentionNotificationService.list(cond);
        return mentionNotifications.stream().filter(mentionNotification -> {
            if (MentionNotification.POST_TYPE.equals(mentionNotification.getType())) {
                Post post = postService.lambdaQuery()
                        .select(Post::getCategory, Post::getExtreme, Post::getAuthorId)
                        .eq(Post::getId, mentionNotification.getTextId())
                        .one();
                return !post.getExtreme()
                        || currentUserId.equals(post.getAuthorId())
                        || filterConfig.charAt(categories.indexOf(post.getCategory())) == '0';
            } else if (MentionNotification.COMMENT_TYPE.equals(mentionNotification.getType())) {
                Comment comment = commentService.lambdaQuery()
                        .select(Comment::getCategory, Comment::getExtreme, Comment::getUserId)
                        .eq(Comment::getId, mentionNotification.getTextId())
                        .one();
                return !comment.getExtreme()
                        || currentUserId.equals(comment.getUserId())
                        || filterConfig.charAt(categories.indexOf(comment.getCategory())) == '0';
            } else if (MentionNotification.REPLY_TYPE.equals(mentionNotification.getType())) {
                CommentReply commentReply = commentReplyService.lambdaQuery()
                        .select(CommentReply::getCategory, CommentReply::getExtreme, CommentReply::getUserId)
                        .eq(CommentReply::getId, mentionNotification.getTextId())
                        .one();
                return !commentReply.getExtreme()
                        || currentUserId.equals(commentReply.getUserId())
                        || filterConfig.charAt(categories.indexOf(commentReply.getCategory())) == '0';
            }
            return false;
        }).count();
    }

    @GetMapping("/getNewNotification")
    public List<MentionNotificationVo> getNewNotification() {
        String currentUserId = springSecurityService.getCurrentUserId();
        String filterConfig = userService.getById(currentUserId).getFilterConfig();
        List<String> categories = AiUtils.getAllCategories();
        List<MentionNotification> mentionNotifications = mentionNotificationService.lambdaQuery()
                .eq(MentionNotification::getMentionedUserId, currentUserId)
                .orderByDesc(MentionNotification::getTimeCreated)
                .list();

        mentionNotifications = mentionNotifications.stream().filter(mentionNotification -> {
            if (MentionNotification.POST_TYPE.equals(mentionNotification.getType())) {
                Post post = postService.lambdaQuery()
                        .select(Post::getCategory, Post::getExtreme, Post::getAuthorId)
                        .eq(Post::getId, mentionNotification.getTextId())
                        .one();
                return !post.getExtreme()
                        || currentUserId.equals(post.getAuthorId())
                        || filterConfig.charAt(categories.indexOf(post.getCategory())) == '0';
            } else if (MentionNotification.COMMENT_TYPE.equals(mentionNotification.getType())) {
                Comment comment = commentService.lambdaQuery()
                        .select(Comment::getCategory, Comment::getExtreme, Comment::getUserId)
                        .eq(Comment::getId, mentionNotification.getTextId())
                        .one();
                return !comment.getExtreme()
                        || currentUserId.equals(comment.getUserId())
                        || filterConfig.charAt(categories.indexOf(comment.getCategory())) == '0';
            } else if (MentionNotification.REPLY_TYPE.equals(mentionNotification.getType())) {
                CommentReply commentReply = commentReplyService.lambdaQuery()
                        .select(CommentReply::getCategory, CommentReply::getExtreme, CommentReply::getUserId)
                        .eq(CommentReply::getId, mentionNotification.getTextId())
                        .one();
                return !commentReply.getExtreme()
                        || currentUserId.equals(commentReply.getUserId())
                        || filterConfig.charAt(categories.indexOf(commentReply.getCategory())) == '0';
            }
            return false;
        }).collect(Collectors.toList());

        List<MentionNotificationVo> mentionNotificationVos =
                BeanCopyUtils.copyList(mentionNotifications, MentionNotificationVo.class);
        for (MentionNotificationVo mentionNotificationVo : mentionNotificationVos) {
            User mentionedUser = userService.getById(mentionNotificationVo.getMentionedUserId());
            mentionNotificationVo.setMentionedUsername(mentionedUser.getUsername());
            User author = null;
            if (MentionNotification.POST_TYPE.equals(mentionNotificationVo.getType())) {
                Post post = postService.getById(mentionNotificationVo.getTextId());
                author = userService.getById(post.getAuthorId());
                mentionNotificationVo.setText(post.getContent());
                mentionNotificationVo.setTextTimeCreated(post.getTimeCreated());
                mentionNotificationVo.setJumpToTextId(post.getId());
            } else if (MentionNotification.COMMENT_TYPE.equals(mentionNotificationVo.getType())) {
                Comment comment = commentService.getById(mentionNotificationVo.getTextId());
                author = userService.getById(comment.getUserId());
                mentionNotificationVo.setText(comment.getContent());
                mentionNotificationVo.setTextTimeCreated(comment.getTimeCreated());
                mentionNotificationVo.setJumpToTextId(comment.getId());
            } else if (MentionNotification.REPLY_TYPE.equals(mentionNotificationVo.getType())) {
                CommentReply commentReply = commentReplyService.getById(mentionNotificationVo.getTextId());
                author = userService.getById(commentReply.getUserId());
                mentionNotificationVo.setText(commentReply.getContent());
                mentionNotificationVo.setTextTimeCreated(commentReply.getTimeCreated());
                mentionNotificationVo.setJumpToTextId(commentReply.getCommentId());
            }
            Assert.notNull(author, "未知错误");
            mentionNotificationVo.setAuthorId(author.getId());
            mentionNotificationVo.setAuthorUsername(author.getUsername());
            mentionNotificationVo.setAuthorNickname(author.getNickname());
            mentionNotificationVo.setAuthorAvatarFilename(author.getAvatarFilename());
            mentionNotificationVo.setText(RegexUtils.parseMentionedUsername(mentionNotificationVo.getText()));
            if (MentionNotification.POST_TYPE.equals(mentionNotificationVo.getType())) {
                mentionNotificationVo.setText(RegexUtils.parseHashtag(mentionNotificationVo.getText()));
            }
            if (!mentionNotificationVo.getRead()) {
                mentionNotificationService.lambdaUpdate().set(MentionNotification::getRead, true)
                        .eq(MentionNotification::getMentionedUserId, mentionNotificationVo.getMentionedUserId())
                        .eq(MentionNotification::getTextId, mentionNotificationVo.getTextId())
                        .eq(MentionNotification::getType, mentionNotificationVo.getType())
                        .update();
            }
        }
        return mentionNotificationVos;
    }
}
