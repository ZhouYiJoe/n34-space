package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.n34.space.entity.CommentLike;
import com.n34.space.service.CommentLikeService;
import com.n34.space.service.SpringSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment_likes")
@Validated
public class CommentLikeController {
    private final CommentLikeService commentLikeService;
    private final SpringSecurityService springSecurityService;

    @GetMapping
    public Boolean leaveLike(@NotNull @RequestParam String commentId) {
        try {
            CommentLike commentLike = new CommentLike()
                    .setUserId(springSecurityService.getCurrentUserId())
                    .setCommentId(commentId);
            return commentLikeService.save(commentLike);
        } catch (Exception e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                throw new RuntimeException("已经点赞过了");
            } else {
                throw e;
            }
        }
    }

    @DeleteMapping
    public Boolean cancelLike(@NotNull @RequestParam String commentId) {
        LambdaQueryWrapper<CommentLike> cond = new LambdaQueryWrapper<>();
        cond.eq(CommentLike::getCommentId, commentId);
        cond.eq(CommentLike::getUserId, springSecurityService.getCurrentUserId());
        return commentLikeService.remove(cond);
    }
}
