package com.n34.space.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDto {
    private String username;
    private String password;
    private String email;
    private String nickname;
}
