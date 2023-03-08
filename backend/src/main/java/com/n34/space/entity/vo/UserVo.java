package com.n34.space.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UserVo {
    private String id;
    private String username;
    private String email;
    private String nickname;
    private String avatarFilename;
    private String wallpaperFilename;
    private Boolean followedByMe;
    private String filterConfig;
    private Integer numFollower;
    private String introduction;
    private String location;
    private String link;
    private Integer numFollowee;
    private Integer registerYear;
    private Integer registerMonth;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeCreated;
}
