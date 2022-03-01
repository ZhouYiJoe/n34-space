package com.n34.demo.interceptor;

import com.n34.demo.response.Response;
import com.n34.demo.response.Status;
import com.n34.demo.utils.JsonUtils;
import com.n34.demo.utils.JwtUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截所有请求，检查请求中的JWT
 */
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        boolean isValidToken = JwtUtils.checkToken(token);

        if (isValidToken) {
            return true;
        } else {
            JsonUtils.setResponse(response, new Response(Status.INVALID_TOKEN));
            return false;
        }
    }
}
