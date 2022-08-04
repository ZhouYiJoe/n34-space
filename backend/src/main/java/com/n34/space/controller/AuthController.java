package com.n34.space.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.n34.space.entity.dto.UserDTO;
import com.n34.space.service.AuthService;
import com.n34.space.service.UserService;
import com.n34.space.utils.StrParamValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/login")
    public R<String> login(@RequestBody UserDTO userDTO) {
        if (StrParamValidationUtils.checkUsername(userDTO.getUsername()))
            throw new RuntimeException("用户名不合法");
        if (StrParamValidationUtils.checkPassword(userDTO.getPassword()))
            throw new RuntimeException("密码不合法");
        return R.ok(authService.login(userDTO));
    }

    @PostMapping("/register")
    public R<Boolean> register(@RequestBody UserDTO userDTO) {
        if (StrParamValidationUtils.checkUsername(userDTO.getUsername()))
            throw new RuntimeException("用户名不合法");
        if (StrParamValidationUtils.checkPassword(userDTO.getPassword()))
            throw new RuntimeException("密码不合法");
        if (StrParamValidationUtils.checkEmail(userDTO.getEmail()))
            throw new RuntimeException("邮箱不合法");
        if (StrParamValidationUtils.checkNickname(userDTO.getNickname()))
            throw new RuntimeException("昵称不合法");

        return R.ok(userService.register(userDTO));
    }

    @GetMapping("/tokenValidation")
    public R<Boolean> checkToken() {
        //由于访问这个接口前已经通过过滤器验证，所以此时已经可以直接确定token是有效的
        return R.ok(true);
    }
}
