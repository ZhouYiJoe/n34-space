package com.n34.space.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CircleVo {
    private String id;
    private String creatorId;
    private String creatorUsername;
    private String introduction;
    private Integer timeCreatedYear;
    private Integer timeCreatedMonth;
    private Integer numMember;
    private String name;
    private String avatarFilename;
    private String wallpaperFilename;
}
