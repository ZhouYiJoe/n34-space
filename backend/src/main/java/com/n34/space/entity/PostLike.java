package com.n34.space.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 博文点赞 用户-博文关系
 */
@Data
@Accessors(chain = true)
public class PostLike {
    private Long userId;
    private Long postId;
}
