package com.n34.space.service.impl;

import com.n34.space.entity.NormalUserLoginState;
import com.n34.space.service.RedisService;
import com.n34.space.service.SpringSecurityService;
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
        map.put(LOGIN_INFO_HASH_KEY, loginState);
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
}
