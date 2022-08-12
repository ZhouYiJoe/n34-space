package com.n34.space.service;

import com.n34.space.entity.dto.NormalUserLoginState;
import org.springframework.security.core.userdetails.UserDetails;

public interface SpringSecurityService {
    void saveLoginState(long userId, String token, UserDetails loginState);

    long getCurrentUserId();

    NormalUserLoginState getLoginStateFromRedis(String userId);

    String getTokenFromRedis(String userId);

    void removeLoginState(long userId);
}
