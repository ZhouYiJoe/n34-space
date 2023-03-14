package com.n34.space.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class MentionNotification {
    public static final String POST_TYPE = "post";
    public static final String COMMENT_TYPE = "comment";
    public static final String REPLY_TYPE = "reply";

    private String mentionedUserId;
    private String textId;
    private String type;
    @TableField(fill = FieldFill.INSERT)
    private Date timeCreated;
    @TableField("`read`")
    private Boolean read;
}
