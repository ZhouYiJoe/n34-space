package com.n34.space.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserVo {
    private String id;
    private String username;
    private String email;
    private String nickname;
    private String avatarFilename;
    private Boolean followedByMe;
    private String filterConfig;
    private Integer numFollower;
}
