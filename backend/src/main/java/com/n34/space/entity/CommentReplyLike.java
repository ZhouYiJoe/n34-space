package com.n34.space.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 对评论下面的回复的点赞 用户-回复关系
 */
@Data
@Accessors(chain = true)
public class CommentReplyLike {
    private String userId;
    private String commentReplyId;
}
