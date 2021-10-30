package com.n34.demo.controller;

import com.n34.demo.response.Response;
import com.n34.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Response getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{username}")
    public Response getUserInfoByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }
}
