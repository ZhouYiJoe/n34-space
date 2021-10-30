package com.n34.demo.config;

import com.n34.demo.interceptor.PostInterceptor;
import com.n34.demo.interceptor.TokenInterceptor;
import com.n34.demo.repository.PostRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final PostRepository postRepository;

    public InterceptorConfig(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(List.of("/login", "/register", "/check-token"));

        registry.addInterceptor(new PostInterceptor(postRepository)).addPathPatterns("/posts/*");
    }
}
