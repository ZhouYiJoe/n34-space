package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.n34.space.entity.*;
import com.n34.space.entity.dto.CommentReplyDto;
import com.n34.space.entity.vo.CommentReplyVo;
import com.n34.space.service.*;
import com.n34.space.utils.AiUtils;
import com.n34.space.utils.BeanCopyUtils;
import com.n34.space.utils.RegexUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment_replies")
public class CommentReplyController {
    private final SpringSecurityService springSecurityService;
    private final UserService userService;
    private final CommentReplyService commentReplyService;
    private final CommentReplyLikeService commentReplyLikeService;
    private final MentionNotificationService mentionNotificationService;
    private final CommentService commentService;
    private final ReplyNotificationService replyNotificationService;

    @GetMapping("/{id}")
    public CommentReplyVo getById(@PathVariable String id) {
        CommentReply commentReply = commentReplyService.getById(id);
        CommentReplyVo commentReplyVo = BeanCopyUtils.copyObject(commentReply, CommentReplyVo.class);
        User user = userService.getById(commentReplyVo.getUserId());
        commentReplyVo.setNickname(user.getNickname());
        commentReplyVo.setUsername(user.getUsername());
        commentReplyVo.setAvatarFilename(user.getAvatarFilename());

        LambdaQueryWrapper<CommentReplyLike> cond2 = new LambdaQueryWrapper<>();
        cond2.eq(CommentReplyLike::getCommentReplyId, commentReplyVo.getId());
        commentReplyVo.setNumLike(commentReplyLikeService.count(cond2));

        cond2 = new LambdaQueryWrapper<>();
        cond2.eq(CommentReplyLike::getCommentReplyId, commentReplyVo.getId());
        cond2.eq(CommentReplyLike::getUserId, springSecurityService.getCurrentUserId());
        commentReplyVo.setLikedByMe(commentReplyLikeService.count(cond2) == 1);

        commentReplyVo.setHtml(RegexUtils.parseMentionedUsername(commentReplyVo.getContent()));
        return commentReplyVo;
    }

    @PostMapping
    public Boolean postNew(@RequestBody CommentReplyDto commentReplyDto) {
        Assert.notNull(commentReplyDto.getUserId(), "userId为null");
        Assert.notNull(commentReplyDto.getCommentId(), "commentId为null");
        Assert.isNull(commentReplyDto.getId(), "无权访问");
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(commentReplyDto.getUserId().equals(currentUserId), "无权访问");
        CommentReply commentReply = BeanCopyUtils.copyObject(commentReplyDto, CommentReply.class);
        commentReply.setCategory(AiUtils.getCategory(commentReply.getContent()));
        commentReply.setExtreme(AiUtils.getSentiment(commentReply.getContent()));
        if (!commentReplyService.save(commentReply)) {
            return false;
        }

        Set<String> mentionedUsernames = RegexUtils.getMentionedUsernames(commentReply.getContent());
        for (String mentionedUsername : mentionedUsernames) {
            LambdaQueryWrapper<User> cond = new LambdaQueryWrapper<>();
            cond.eq(User::getUsername, mentionedUsername);
            User mentionedUser = userService.getOne(cond);
            MentionNotification mentionNotification = new MentionNotification()
                    .setMentionedUserId(mentionedUser.getId())
                    .setTextId(commentReply.getId())
                    .setType(MentionNotification.REPLY_TYPE)
                    .setRead(false);
            mentionNotificationService.save(mentionNotification);
        }

        Comment comment = commentService.getById(commentReplyDto.getCommentId());
        User repliedUser = userService.getById(comment.getUserId());
        ReplyNotification replyNotification = new ReplyNotification()
                .setRepliedUserId(repliedUser.getId())
                .setReplyUserId(currentUserId)
                .setRepliedTextId(comment.getId())
                .setReplyTextId(commentReply.getId())
                .setRead(false)
                .setType(ReplyNotification.REPLY_TYPE);
        replyNotificationService.save(replyNotification);

        return true;
    }

    @GetMapping
    public List<CommentReplyVo> getList(@RequestParam(required = false) String commentId,
                                        @RequestParam(required = false) String timeSortOrder) {
        String currentUserId = springSecurityService.getCurrentUserId();
        String filterConfig = userService.getById(currentUserId).getFilterConfig();
        LambdaQueryWrapper<CommentReply> cond = new LambdaQueryWrapper<>();
        cond.eq(CommentReply::getCommentId, commentId);
        if ("asc".equals(timeSortOrder)) {
            cond.orderByAsc(CommentReply::getTimeCreated);
        } else if ("desc".equals(timeSortOrder)) {
            cond.orderByDesc(CommentReply::getTimeCreated);
        }
        List<CommentReply> commentReplies = commentReplyService.list(cond);
        List<String> categories = AiUtils.getAllCategories();
        commentReplies = commentReplies.stream().filter(commentReply -> !commentReply.getExtreme()
                        || commentReply.getUserId().equals(currentUserId)
                        || filterConfig.charAt(categories.indexOf(commentReply.getCategory())) == '0')
                .collect(Collectors.toList());
        List<CommentReplyVo> commentReplyVos = BeanCopyUtils.copyList(commentReplies, CommentReplyVo.class);
        for (CommentReplyVo commentReplyVo : commentReplyVos) {
            String userId = commentReplyVo.getUserId();
            Assert.notNull(userId, "userId为null");
            User user = userService.getById(userId);
            Assert.notNull(user, "user为null");
            commentReplyVo.setUsername(user.getUsername());
            commentReplyVo.setNickname(user.getNickname());
            commentReplyVo.setAvatarFilename(user.getAvatarFilename());

            LambdaQueryWrapper<CommentReplyLike> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(CommentReplyLike::getCommentReplyId, commentReplyVo.getId());
            commentReplyVo.setNumLike(commentReplyLikeService.count(cond2));

            cond2 = new LambdaQueryWrapper<>();
            cond2.eq(CommentReplyLike::getCommentReplyId, commentReplyVo.getId());
            cond2.eq(CommentReplyLike::getUserId, springSecurityService.getCurrentUserId());
            commentReplyVo.setLikedByMe(commentReplyLikeService.count(cond2) == 1);

            commentReplyVo.setHtml(RegexUtils.parseMentionedUsername(commentReplyVo.getContent()));
        }

        return commentReplyVos;
    }

    @PutMapping
    public Boolean update(@RequestBody CommentReplyDto commentReplyDto) {
        Assert.notNull(commentReplyDto.getId(), "id为null");
        CommentReply commentReply = commentReplyService.getById(commentReplyDto.getId());
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(commentReply.getUserId()), "无权访问");
        Assert.isTrue(currentUserId.equals(commentReplyDto.getUserId()), "无权访问");
        Assert.isNull(commentReply.getCommentId(), "无权访问");
        CommentReply commentReply1 = BeanCopyUtils.copyObject(commentReplyDto, CommentReply.class);
        commentReply1.setCategory(AiUtils.getCategory(commentReply1.getContent()));
        commentReply1.setExtreme(AiUtils.getSentiment(commentReply1.getContent()));
        return commentReplyService.updateById(commentReply1);
    }

    @DeleteMapping("/{id}")
    public Boolean remove(@NotNull @PathVariable String id) {
        CommentReply commentReply = commentReplyService.getById(id);
        Assert.notNull(commentReply, "回复不存在");
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(commentReply.getUserId()), "无权访问");
        LambdaQueryWrapper<CommentReplyLike> cond = new LambdaQueryWrapper<>();
        cond.eq(CommentReplyLike::getCommentReplyId, id);
        commentReplyLikeService.remove(cond);
        if (!commentReplyService.removeById(id)) {
            return false;
        }

        Set<String> mentionedUsernames = RegexUtils.getMentionedUsernames(commentReply.getContent());
        for (String mentionedUsername : mentionedUsernames) {
            LambdaQueryWrapper<User> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(User::getUsername, mentionedUsername);
            User mentionedUser = userService.getOne(cond2);
            LambdaQueryWrapper<MentionNotification> cond1 = new LambdaQueryWrapper<>();
            cond1.eq(MentionNotification::getTextId, commentReply.getId());
            cond1.eq(MentionNotification::getMentionedUserId, mentionedUser.getId());
            cond1.eq(MentionNotification::getType, MentionNotification.REPLY_TYPE);
            mentionNotificationService.remove(cond1);
        }

        replyNotificationService.lambdaUpdate()
                .eq(ReplyNotification::getRepliedTextId, commentReply.getCommentId())
                .eq(ReplyNotification::getReplyTextId, commentReply.getId())
                .eq(ReplyNotification::getType, ReplyNotification.REPLY_TYPE)
                .remove();

        return true;
    }
}
