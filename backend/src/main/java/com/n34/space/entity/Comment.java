package com.n34.space.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Comment {
    @TableId
    private Long id;
    private String content;
    //发表该评论的用户的ID
    private Long userId;
    //被评论的博文的ID
    private Long postId;
    @TableField(fill = FieldFill.INSERT)
    private Date timeCreated;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date timeUpdated;
}