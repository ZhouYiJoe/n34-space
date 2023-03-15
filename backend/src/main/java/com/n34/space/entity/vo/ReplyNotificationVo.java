package com.n34.space.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class ReplyNotificationVo {
    private String repliedUserId;
    private String repliedUsername;
    private String replyUserId;
    private String replyUsername;
    private String replyNickname;
    private String replyAvatarFilename;
    private String repliedTextId;
    private String replyTextId;
    private String replyText;
    private String replyTextTimeCreated;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeCreated;
    private Boolean read;
    private String type;
}
