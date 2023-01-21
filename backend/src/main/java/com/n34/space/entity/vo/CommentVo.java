package com.n34.space.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class CommentVo {
    private String id;
    private String content;
    //发表该评论的用户的ID
    private String userId;
    //发表该评论的用户的用户名
    private String username;
    //发表该评论的用户的昵称
    private String nickname;
    //被评论的博文的ID
    private String postId;
    private String avatarFilename;
    private Integer numLike;
    private Integer numReply;
    private Boolean likedByMe;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeCreated;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeUpdated;
}
