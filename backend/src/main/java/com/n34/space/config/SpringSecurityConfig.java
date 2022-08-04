package com.n34.space.config;

import com.n34.space.filter.TokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootConfiguration
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    //可以把过滤器自动注入到自定义的WebSecurityConfigurerAdapter子类
    private final TokenFilter tokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("auth/login").anonymous()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/swagger-ui.html/**").permitAll()
                .anyRequest().authenticated();

        //addFilterBefore方法用于配置过滤器拦截请求的顺序
        //方法第一个参数是自定义的过滤器对象
        //第二个参数是一个过滤器的Class对象
        //该方法用来指定将第一个参数对应的过滤器添加在第二个参数对应的过滤器之前
        httpSecurity.addFilterBefore(tokenFilter,
                UsernamePasswordAuthenticationFilter.class);
    }
}
