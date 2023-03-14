package com.n34.space.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class CommentNotification {
    private String commentAuthorId;
    private String postAuthorId;
    private String commentId;
    private String postId;
    @TableField(fill = FieldFill.INSERT)
    private Date timeCreated;
}
