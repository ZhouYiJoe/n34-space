package com.n34.space.entity.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InvitationNotificationVo {
    @TableId
    private String id;
    private String inviterId;
    private String inviterUsername;
    private String inviterNickname;
    private String inviterAvatarFilename;
    private String inviteeId;
    private String circleId;
    private String circleName;
    private String state;
    private Boolean read;
}
