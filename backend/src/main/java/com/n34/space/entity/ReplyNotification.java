package com.n34.space.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class ReplyNotification {
    public static final String COMMENT_TYPE = "comment";
    public static final String REPLY_TYPE = "reply";

    private String repliedUserId;
    private String replyUserId;
    private String repliedTextId;
    private String replyTextId;
    @TableField(fill = FieldFill.INSERT)
    private Date timeCreated;
    @TableField("`read`")
    private Boolean read;
    private String type;
}
