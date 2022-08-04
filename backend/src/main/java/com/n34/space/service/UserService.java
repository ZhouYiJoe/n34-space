package com.n34.space.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.n34.space.entity.User;
import com.n34.space.entity.dto.UserDTO;

public interface UserService extends IService<User> {
    boolean register(UserDTO userDTO);
}
