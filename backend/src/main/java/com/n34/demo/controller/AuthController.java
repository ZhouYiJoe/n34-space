package com.n34.demo.controller;

import com.n34.demo.entity.User;
import com.n34.demo.response.Response;
import com.n34.demo.response.Status;
import com.n34.demo.service.UserService;
import com.n34.demo.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public Response login(@RequestHeader String username,
                          @RequestHeader String password) {
        return userService.checkLogin(username, password);
    }

    @PostMapping("register")
    public Response register(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("check-token")
    public Response checkToken(@RequestHeader String token) {
        if (JwtUtils.checkToken(token)) {
            return new Response(Status.SUCCESS);
        } else {
            return new Response(Status.FAILED);
        }
    }
}
