package com.n34.space.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentReplyDto {
    private Long id;
    private String content;
    //发表该回复的用户的ID
    private Long userId;
    //被回复的评论的ID
    private Long commentId;
}
