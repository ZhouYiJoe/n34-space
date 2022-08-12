package com.n34.space.controller;

import com.n34.space.entity.dto.UserDTO;
import com.n34.space.service.AuthService;
import com.n34.space.service.SpringSecurityService;
import com.n34.space.service.UserService;
import com.n34.space.utils.StrParamValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final SpringSecurityService springSecurityService;

    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) {
        Assert.isTrue(StrParamValidationUtils.checkUsername(userDTO.getUsername()), "用户名不合法");
        Assert.isTrue(StrParamValidationUtils.checkPassword(userDTO.getPassword()), "密码不合法");
        return authService.login(userDTO);
    }

    @PostMapping("/register")
    public Boolean register(@RequestBody UserDTO userDTO) {
        Assert.isTrue(StrParamValidationUtils.checkUsername(userDTO.getUsername()), "用户名不合法");
        Assert.isTrue(StrParamValidationUtils.checkPassword(userDTO.getPassword()), "密码不合法");
        Assert.isTrue(StrParamValidationUtils.checkEmail(userDTO.getEmail()), "邮箱不合法");
        Assert.isTrue(StrParamValidationUtils.checkNickname(userDTO.getNickname()), "昵称不合法");
        return userService.register(userDTO);
    }

    @GetMapping("/tokenValidation")
    public Boolean checkToken() {
        //由于访问这个接口前已经通过过滤器验证，所以此时已经可以直接确定token是有效的
        return true;
    }

    @GetMapping("/logout")
    public Boolean logout() {
        long userId = springSecurityService.getCurrentUserId();
        springSecurityService.removeLoginState(userId);
        return true;
    }
}
