package com.n34.space.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class CommentReplyVo {
    private Long id;
    private String content;
    //发表该回复的用户的ID
    private Long userId;
    //发表该回复的用户的用户名
    private String username;
    //发表该回复的用户的昵称
    private String nickname;
    //被回复的评论的ID
    private Long commentId;
    private Integer numLike;
    private Boolean likedByMe;
    private Date timeCreated;
    private Date timeUpdated;
}
