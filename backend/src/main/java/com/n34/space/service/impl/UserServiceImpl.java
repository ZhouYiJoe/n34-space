package com.n34.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.User;
import com.n34.space.entity.vo.PostVo;
import com.n34.space.mapper.UserMapper;
import com.n34.space.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
