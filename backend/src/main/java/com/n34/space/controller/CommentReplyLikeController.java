package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.n34.space.entity.CommentReplyLike;
import com.n34.space.service.CommentReplyLikeService;
import com.n34.space.service.SpringSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment_reply_likes")
@Validated
public class CommentReplyLikeController {
    private final CommentReplyLikeService commentReplyLikeService;
    private final SpringSecurityService springSecurityService;

    @GetMapping
    public Boolean leaveLike(@NotNull @RequestParam Long commentReplyId) {
        try {
            CommentReplyLike commentReplyLike = new CommentReplyLike()
                    .setUserId(springSecurityService.getCurrentUserId())
                    .setCommentReplyId(commentReplyId);
            return commentReplyLikeService.save(commentReplyLike);
        } catch (Exception e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                throw new RuntimeException("已经点赞过了");
            } else {
                throw e;
            }
        }
    }

    @DeleteMapping
    public Boolean cancelLike(@NotNull @RequestParam Long commentReplyId) {
        LambdaQueryWrapper<CommentReplyLike> cond = new LambdaQueryWrapper<>();
        cond.eq(CommentReplyLike::getCommentReplyId, commentReplyId);
        cond.eq(CommentReplyLike::getUserId, springSecurityService.getCurrentUserId());
        return commentReplyLikeService.remove(cond);
    }
}
