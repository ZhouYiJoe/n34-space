package com.n34.space.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 评论点赞 用户-评论关系
 */
@Data
@Accessors(chain = true)
public class CommentLike {
    private String userId;
    private String commentId;
}
