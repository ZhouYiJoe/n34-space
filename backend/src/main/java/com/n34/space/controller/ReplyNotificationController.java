package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.n34.space.entity.*;
import com.n34.space.entity.vo.MentionNotificationVo;
import com.n34.space.entity.vo.ReplyNotificationVo;
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
@RequiredArgsConstructor
@RequestMapping("/reply_notification")
public class ReplyNotificationController {

    private final SpringSecurityService springSecurityService;
    private final ReplyNotificationService replyNotificationService;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final CommentReplyService commentReplyService;

    @GetMapping("/countNewNotification")
    public Long countNewNotification() {
        String currentUserId = springSecurityService.getCurrentUserId();
        String filterConfig = userService.getById(currentUserId).getFilterConfig();
        List<String> categories = AiUtils.getAllCategories();
        List<ReplyNotification> replyNotifications = replyNotificationService.lambdaQuery()
                .eq(ReplyNotification::getRepliedUserId, currentUserId)
                .eq(ReplyNotification::getRead, false)
                .list();
        return replyNotifications.stream().filter(replyNotification -> {
            if (ReplyNotification.COMMENT_TYPE.equals(replyNotification.getType())) {
                Comment comment = commentService.lambdaQuery()
                        .select(Comment::getCategory, Comment::getExtreme, Comment::getUserId)
                        .eq(Comment::getId, replyNotification.getReplyTextId())
                        .one();
                return !comment.getExtreme()
                        || currentUserId.equals(comment.getUserId())
                        || filterConfig.charAt(categories.indexOf(comment.getCategory())) == '0';
            } else if (ReplyNotification.REPLY_TYPE.equals(replyNotification.getType())) {
                CommentReply commentReply = commentReplyService.lambdaQuery()
                        .select(CommentReply::getCategory, CommentReply::getExtreme, CommentReply::getUserId)
                        .eq(CommentReply::getId, replyNotification.getReplyTextId())
                        .one();
                return !commentReply.getExtreme()
                        || currentUserId.equals(commentReply.getUserId())
                        || filterConfig.charAt(categories.indexOf(commentReply.getCategory())) == '0';
            }
            return false;
        }).count();
    }

    @GetMapping("/getNewNotification")
    public List<ReplyNotificationVo> getNewNotification() {
        String currentUserId = springSecurityService.getCurrentUserId();
        String filterConfig = userService.getById(currentUserId).getFilterConfig();
        List<String> categories = AiUtils.getAllCategories();
        List<ReplyNotification> replyNotifications = replyNotificationService.lambdaQuery()
                .eq(ReplyNotification::getRepliedUserId, currentUserId)
                .orderByDesc(ReplyNotification::getTimeCreated)
                .list();

        replyNotifications = replyNotifications.stream().filter(replyNotification -> {
            if (ReplyNotification.COMMENT_TYPE.equals(replyNotification.getType())) {
                Comment comment = commentService.lambdaQuery()
                        .select(Comment::getCategory, Comment::getExtreme, Comment::getUserId)
                        .eq(Comment::getId, replyNotification.getReplyTextId())
                        .one();
                return !comment.getExtreme()
                        || currentUserId.equals(comment.getUserId())
                        || filterConfig.charAt(categories.indexOf(comment.getCategory())) == '0';
            } else if (ReplyNotification.REPLY_TYPE.equals(replyNotification.getType())) {
                CommentReply commentReply = commentReplyService.lambdaQuery()
                        .select(CommentReply::getCategory, CommentReply::getExtreme, CommentReply::getUserId)
                        .eq(CommentReply::getId, replyNotification.getReplyTextId())
                        .one();
                return !commentReply.getExtreme()
                        || currentUserId.equals(commentReply.getUserId())
                        || filterConfig.charAt(categories.indexOf(commentReply.getCategory())) == '0';
            }
            return false;
        }).collect(Collectors.toList());

        List<ReplyNotificationVo> replyNotificationVos =
                BeanCopyUtils.copyList(replyNotifications, ReplyNotificationVo.class);
        for (ReplyNotificationVo replyNotificationVo : replyNotificationVos) {
            User repliedUser = userService.getById(replyNotificationVo.getRepliedUserId());
            replyNotificationVo.setRepliedUsername(repliedUser.getUsername());
            User replyUser = null;
            if (ReplyNotification.COMMENT_TYPE.equals(replyNotificationVo.getType())) {
                Comment comment = commentService.getById(replyNotificationVo.getReplyTextId());
                replyUser = userService.getById(comment.getUserId());
                replyNotificationVo.setReplyText(comment.getContent());
                replyNotificationVo.setReplyTextTimeCreated(comment.getTimeCreated());
            } else if (ReplyNotification.REPLY_TYPE.equals(replyNotificationVo.getType())) {
                CommentReply commentReply = commentReplyService.getById(replyNotificationVo.getReplyTextId());
                replyUser = userService.getById(commentReply.getUserId());
                replyNotificationVo.setReplyText(commentReply.getContent());
                replyNotificationVo.setReplyTextTimeCreated(commentReply.getTimeCreated());
            }
            Assert.notNull(replyUser, "未知错误");
            replyNotificationVo.setReplyUsername(replyUser.getUsername());
            replyNotificationVo.setReplyNickname(replyUser.getNickname());
            replyNotificationVo.setReplyAvatarFilename(replyUser.getAvatarFilename());
            replyNotificationVo.setReplyText(RegexUtils.parseMentionedUsername(replyNotificationVo.getReplyText()));
            if (ReplyNotification.COMMENT_TYPE.equals(replyNotificationVo.getType())) {
                Post post = postService.getById(replyNotificationVo.getRepliedTextId());
                replyNotificationVo.setRepliedText(RegexUtils.parseHashtag(post.getContent()));
                replyNotificationVo.setRepliedText(RegexUtils.parseMentionedUsername(replyNotificationVo.getRepliedText()));
            } else if (ReplyNotification.REPLY_TYPE.equals(replyNotificationVo.getType())) {
                Comment comment = commentService.getById(replyNotificationVo.getRepliedTextId());
                replyNotificationVo.setRepliedText(RegexUtils.parseMentionedUsername(comment.getContent()));
            }
            if (!replyNotificationVo.getRead()) {
                replyNotificationService.lambdaUpdate().set(ReplyNotification::getRead, true)
                        .eq(ReplyNotification::getRepliedTextId, replyNotificationVo.getRepliedTextId())
                        .eq(ReplyNotification::getReplyTextId, replyNotificationVo.getReplyTextId())
                        .eq(ReplyNotification::getType, replyNotificationVo.getType())
                        .update();
            }
        }
        return replyNotificationVos;
    }
}
