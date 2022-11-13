package com.n34.space.service;

import com.n34.space.entity.dto.NormalUserLoginState;
import org.springframework.security.core.userdetails.UserDetails;

public interface SpringSecurityService {
    void saveLoginState(String userId, String token, UserDetails loginState);

    String getCurrentUserId();

    NormalUserLoginState getLoginStateFromRedis(String userId);

    String getTokenFromRedis(String userId);

    void removeLoginState(String userId);
}
