package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.n34.space.entity.User;
import com.n34.space.entity.dto.NormalUserLoginState;
import com.n34.space.entity.dto.UserDto;
import com.n34.space.service.SpringSecurityService;
import com.n34.space.service.UserService;
import com.n34.space.utils.BeanCopyUtils;
import com.n34.space.utils.JwtUtils;
import com.n34.space.utils.StrParamValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SpringSecurityService springSecurityService;

    @PostMapping("/login")
    public String login(@RequestBody UserDto userDTO) {
        Assert.isTrue(StrParamValidationUtils.checkUsername(userDTO.getUsername()), "用户名不合法");
        Assert.isTrue(StrParamValidationUtils.checkPassword(userDTO.getPassword()), "密码不合法");
        LambdaQueryWrapper<User> cond = new LambdaQueryWrapper<>();
        cond.eq(User::getUsername, userDTO.getUsername());
        User user = userService.getOne(cond);
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(passwordEncoder.matches(userDTO.getPassword(), user.getPassword()), "密码错误");
        NormalUserLoginState loginState = new NormalUserLoginState()
                .setUserId(user.getId())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setActive(true);
        String token = JwtUtils.createToken(user.getId());
        springSecurityService.saveLoginState(user.getId(), token, loginState);
        return token;
    }

    @PostMapping("/register")
    public Boolean register(@RequestBody UserDto userDTO) {
        Assert.isTrue(StrParamValidationUtils.checkUsername(userDTO.getUsername()), "用户名不合法");
        Assert.isTrue(StrParamValidationUtils.checkPassword(userDTO.getPassword()), "密码不合法");
        Assert.isTrue(StrParamValidationUtils.checkEmail(userDTO.getEmail()), "邮箱不合法");
        Assert.isTrue(StrParamValidationUtils.checkNickname(userDTO.getNickname()), "昵称不合法");

        LambdaQueryWrapper<User> cond1 = new LambdaQueryWrapper<>();
        cond1.eq(User::getUsername, userDTO.getUsername());
        User user1 = userService.getOne(cond1);
        Assert.isNull(user1, "用户名跟已有用户重复");

        LambdaQueryWrapper<User> cond2 = new LambdaQueryWrapper<>();
        cond2.eq(User::getEmail, userDTO.getEmail());
        User user2 = userService.getOne(cond2);
        Assert.isNull(user2, "邮箱已被注册");

        User user = BeanCopyUtils.copyObject(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.save(user);
    }

    @GetMapping("/tokenValidation")
    public Boolean checkToken() {
        //由于访问这个接口前已经通过过滤器验证，所以此时已经可以直接确定token是有效的
        return true;
    }

    @GetMapping("/logout")
    public Boolean logout() {
        String userId = springSecurityService.getCurrentUserId();
        springSecurityService.removeLoginState(userId);
        return true;
    }
}
