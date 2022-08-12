package com.n34.space.service.impl;

import com.n34.space.entity.dto.NormalUserLoginState;
import com.n34.space.service.RedisService;
import com.n34.space.service.SpringSecurityService;
import com.n34.space.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SpringSecurityServiceImpl implements SpringSecurityService {
    private static final String LOGIN_STATE_KEY = "login_state";
    private static final String TOKEN_HASH_KEY = "token";
    private static final String LOGIN_INFO_HASH_KEY = "login_info";
    //单位：秒
    private static final long LOGIN_STATE_TIMEOUT = 7 * 24 * 60 * 60;

    private final RedisService redisService;

    @Override
    public void saveLoginState(long userId, String token, UserDetails loginState) {
        Map<Object, Object> map = new HashMap<>();
        map.put(TOKEN_HASH_KEY, token);
        map.put(LOGIN_INFO_HASH_KEY, JsonUtils.toJson(loginState));
        redisService.hmset(LOGIN_STATE_KEY + ":" + userId, map);
        redisService.expire(LOGIN_STATE_KEY + ":" + userId, LOGIN_STATE_TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    public long getCurrentUserId() {
        return getLoginState().getUserId();
    }

    private NormalUserLoginState getLoginState() {
        return (NormalUserLoginState) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
    }

    @Override
    public NormalUserLoginState getLoginStateFromRedis(String userId) {
        String json = (String) redisService.hget(
                LOGIN_STATE_KEY + ":" + userId, LOGIN_INFO_HASH_KEY);
        return JsonUtils.toObj(json, NormalUserLoginState.class);
    }

    @Override
    public String getTokenFromRedis(String userId) {
        return (String) redisService.hget(
                LOGIN_STATE_KEY + ":" + userId, TOKEN_HASH_KEY);
    }

    public void removeLoginState(long userId) {
        redisService.del(LOGIN_STATE_KEY + ":" + userId);
    }
}
