package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.n34.space.entity.PostLike;
import com.n34.space.service.PostLikeService;
import com.n34.space.service.SpringSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post_likes")
@Validated
public class PostLikeController {
    private final PostLikeService postLikeService;
    private final SpringSecurityService springSecurityService;

    @GetMapping
    public Boolean leaveLike(@NotNull @RequestParam String postId) {
        try {
            PostLike postLike = new PostLike()
                    .setUserId(springSecurityService.getCurrentUserId())
                    .setPostId(postId);
            return postLikeService.save(postLike);
        } catch (Exception e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                throw new RuntimeException("已经点赞过了");
            } else {
                throw e;
            }
        }
    }

    @DeleteMapping
    public Boolean cancelLike(@NotNull @RequestParam String postId) {
        LambdaQueryWrapper<PostLike> cond = new LambdaQueryWrapper<>();
        cond.eq(PostLike::getPostId, postId);
        cond.eq(PostLike::getUserId, springSecurityService.getCurrentUserId());
        return postLikeService.remove(cond);
    }
}
