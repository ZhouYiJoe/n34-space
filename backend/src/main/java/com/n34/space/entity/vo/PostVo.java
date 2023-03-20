package com.n34.space.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class PostVo {
    private String id;
    private String content;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeCreated;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeUpdated;
    private String authorId;
    private String authorUsername;
    private String authorNickname;
    private String authorAvatarFilename;
    private Integer numLike;
    private Integer numComment;
    private Boolean likedByMe;
    private String html;
    private String circleId;
}
