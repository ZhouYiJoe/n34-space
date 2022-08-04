package com.n34.space.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface SpringSecurityService {
    void saveLoginState(long userId, String token, UserDetails loginState);

    long getCurrentUserId();
}
