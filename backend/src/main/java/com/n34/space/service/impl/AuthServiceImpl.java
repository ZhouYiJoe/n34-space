package com.n34.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.n34.space.entity.dto.NormalUserLoginState;
import com.n34.space.entity.User;
import com.n34.space.entity.dto.UserDTO;
import com.n34.space.mapper.UserMapper;
import com.n34.space.service.AuthService;
import com.n34.space.service.SpringSecurityService;
import com.n34.space.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;
    private final SpringSecurityService springSecurityService;

    @Override
    public String login(UserDTO userDTO) {
        LambdaQueryWrapper<User> cond = new LambdaQueryWrapper<>();
        cond.eq(User::getUsername, userDTO.getUsername());
        User user = userMapper.selectOne(cond);
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(passwordEncoder.matches(userDTO.getPassword(), user.getPassword()), "密码错误");
        NormalUserLoginState loginState = new NormalUserLoginState()
                .setUserId(user.getId())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setActive(true);
        String token = JwtUtils.createToken(user.getId().toString());
        springSecurityService.saveLoginState(user.getId(), token, loginState);
        return token;
    }
}
