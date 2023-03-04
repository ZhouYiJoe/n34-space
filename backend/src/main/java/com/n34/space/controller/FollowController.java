package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.n34.space.entity.Follow;
import com.n34.space.service.FollowService;
import com.n34.space.service.SpringSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final SpringSecurityService springSecurityService;

    @GetMapping
    public Boolean followUser(@NotNull @RequestParam String followeeId) {
        try {
            LambdaQueryWrapper<Follow> cond = new LambdaQueryWrapper<>();
            cond.eq(Follow::getFolloweeId, followeeId);
            cond.eq(Follow::getFollowerId, springSecurityService.getCurrentUserId());
            Follow follow = followService.getOne(cond);
            if (follow == null) {
                follow = new Follow()
                        .setFollowerId(springSecurityService.getCurrentUserId())
                        .setFolloweeId(followeeId);
                return followService.save(follow);
            }
            return true;
        } catch (Exception e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                e.printStackTrace();
                return true;
            } else {
                throw e;
            }
        }
    }

    @DeleteMapping
    public Boolean unfollowUser(@NotNull @RequestParam String followeeId) {
        LambdaQueryWrapper<Follow> cond = new LambdaQueryWrapper<>();
        cond.eq(Follow::getFolloweeId, followeeId);
        cond.eq(Follow::getFollowerId, springSecurityService.getCurrentUserId());
        followService.remove(cond);
        return true;
    }
}
