package com.n34.space.controller;

import com.n34.space.entity.dto.UserDTO;
import com.n34.space.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return null;
    }

    @GetMapping("/{username}")
    public UserDTO getUserInfoByUsername(@PathVariable String username) {
        return null;
    }
}
