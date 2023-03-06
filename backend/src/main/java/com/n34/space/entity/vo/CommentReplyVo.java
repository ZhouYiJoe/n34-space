package com.n34.space.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class CommentReplyVo {
    private String id;
    private String content;
    //发表该回复的用户的ID
    private String userId;
    //发表该回复的用户的用户名
    private String username;
    //发表该回复的用户的昵称
    private String nickname;
    //被回复的评论的ID
    private String commentId;
    private Integer numLike;
    private Boolean likedByMe;
    private String avatarFilename;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeCreated;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeUpdated;
    private String html;
}
