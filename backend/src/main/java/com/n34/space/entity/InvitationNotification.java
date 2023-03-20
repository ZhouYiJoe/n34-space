package com.n34.space.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class InvitationNotification {
    public static final String WAITING = "waiting";
    public static final String ACCEPTED = "accepted";
    public static final String REJECTED = "rejected";

    @TableId
    private String id;
    private String inviterId;
    private String inviteeId;
    private String circleId;
    private String state;
    private Boolean read;
    @TableField(fill = FieldFill.INSERT)
    private Date timeCreated;
}
