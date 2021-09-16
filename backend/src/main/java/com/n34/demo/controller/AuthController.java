package com.n34.demo.controller;

import com.n34.demo.entity.User;
import com.n34.demo.service.UserService;
import com.n34.demo.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public Map<String, Object> login(@RequestHeader String username,
                                     @RequestHeader String password) {
        return userService.checkLogin(username, password);
    }

    @PostMapping("register")
    public Map<String, Object> register(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("check-token")
    public boolean checkToken(@RequestHeader String token) {
        return JwtUtils.checkToken(token);
    }
}
