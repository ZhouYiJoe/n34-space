package com.n34.space.service;

import com.n34.space.entity.dto.UserDTO;

public interface AuthService {
    String login(UserDTO userDTO);
}
