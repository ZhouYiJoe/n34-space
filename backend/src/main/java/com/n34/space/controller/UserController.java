package com.n34.space.controller;

import com.n34.space.entity.User;
import com.n34.space.entity.dto.UserDto;
import com.n34.space.entity.vo.UserVo;
import com.n34.space.service.SpringSecurityService;
import com.n34.space.service.UserService;
import com.n34.space.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SpringSecurityService springSecurityService;

    @GetMapping("/self")
    public UserVo getSelfInfo() {
        String userId = springSecurityService.getCurrentUserId();
        User user = userService.getById(userId);
        Assert.notNull(user, "用户不存在");
        return BeanCopyUtils.copyObject(user, UserVo.class);
    }
}
