package com.n34.demo.interceptor;

import com.n34.demo.entity.Post;
import com.n34.demo.repository.PostRepository;
import com.n34.demo.response.Response;
import com.n34.demo.response.Status;
import com.n34.demo.utils.JsonUtils;
import com.n34.demo.utils.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PostInterceptor implements HandlerInterceptor {
    private final PostRepository postRepository;

    public PostInterceptor(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * 拦截添加或删除博文的请求，以防止用户的博文被其他用户操作
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String method = request.getMethod();
        String token = request.getHeader("token");
        String authorInToken = (String) Jwts.parser().setSigningKey(JwtUtils.SECRET_KEY)
                .parseClaimsJws(token).getBody().get("username");
        String authorInPath = null;

        if (method.equals("POST")) {
            String[] url = request.getRequestURL().toString().split("/");
            authorInPath = url[url.length - 1];
        } else if (method.equals("DELETE")) {
            try {
                String[] url = request.getRequestURL().toString().split("/");
                long postId = Long.parseLong(url[url.length - 1]);
                Post post = postRepository.findById(postId).orElse(null);
                if (post == null) {
                    JsonUtils.setResponse(response, new Response(Status.POST_NOT_FOUND));
                    return false;
                }
                authorInPath = post.getAuthor().getUsername();
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return true;
        }

        if (authorInPath.equals(authorInToken)) {
            return true;
        }
        JsonUtils.setResponse(response, new Response(Status.INVALID_TOKEN));
        return false;
    }
}
