package com.n34.space.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 对评论的回复
 */
@Data
@Accessors(chain = true)
public class CommentReply {
    @TableId
    private String id;
    private String content;
    //发表该回复的用户的ID
    private String userId;
    //被回复的评论的ID
    private String commentId;
    @TableField(fill = FieldFill.INSERT)
    private Date timeCreated;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date timeUpdated;
}
