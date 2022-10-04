package com.n34.space.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class PostVo {
    private Long id;
    private String content;
    private Date timeCreated;
    private Date timeUpdated;
    private Long authorId;
    private String authorUsername;
    private String authorNickname;
    private Integer numLike;
    private Integer numComment;
    private Boolean likedByMe;
}
