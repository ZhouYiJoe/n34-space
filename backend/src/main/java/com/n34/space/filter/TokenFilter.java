package com.n34.space.filter;

import com.n34.space.entity.dto.NormalUserLoginState;
import com.n34.space.service.SpringSecurityService;
import com.n34.space.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {
    private final SpringSecurityService springSecurityService;

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        String userId = JwtUtils.getUserId(token);
        if (userId == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String tokenFromRedis = springSecurityService.getTokenFromRedis(userId);
        if (!token.equals(tokenFromRedis)) {
            filterChain.doFilter(request, response);
            return;
        }
        NormalUserLoginState loginInfo = springSecurityService.getLoginStateFromRedis(userId);
        if (loginInfo == null) {
            filterChain.doFilter(request, response);
            return;
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginInfo, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
