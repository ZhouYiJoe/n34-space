package com.n34.space.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChangePasswordReq {
    private String oldPassword;
    private String newPassword;
}
