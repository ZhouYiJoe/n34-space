package com.n34.space.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class CommentVo {
    private Long id;
    private String content;
    //发表该评论的用户的ID
    private Long userId;
    //发表该评论的用户的用户名
    private String username;
    //发表该评论的用户的昵称
    private String nickname;
    //被评论的博文的ID
    private Long postId;
    private Integer numLike;
    private Integer numReply;
    private Boolean likedByMe;
    private Date timeCreated;
    private Date timeUpdated;
}
