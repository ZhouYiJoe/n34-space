package com.n34.space.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentDto {
    private String id;
    private String content;
    //发表该评论的用户的ID
    private String userId;
    //被评论的博文的ID
    private String postId;
}
