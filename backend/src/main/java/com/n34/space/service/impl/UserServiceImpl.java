package com.n34.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.User;
import com.n34.space.entity.dto.UserDTO;
import com.n34.space.mapper.UserMapper;
import com.n34.space.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public boolean register(UserDTO userDTO) {
        LambdaQueryWrapper<User> cond1 = new LambdaQueryWrapper<>();
        cond1.eq(User::getUsername, userDTO.getUsername());
        User user1 = getOne(cond1);
        if (user1 != null) throw new RuntimeException("用户名跟已有用户重复");

        LambdaQueryWrapper<User> cond2 = new LambdaQueryWrapper<>();
        cond2.eq(User::getEmail, userDTO.getEmail());
        User user2 = getOne(cond2);
        if (user2 != null) throw new RuntimeException("邮箱已被注册");

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return save(user);
    }
}
