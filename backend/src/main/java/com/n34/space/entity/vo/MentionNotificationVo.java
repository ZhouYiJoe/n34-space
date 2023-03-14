package com.n34.space.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class MentionNotificationVo {
    private String mentionedUserId;
    private String mentionedUsername;
    private String textId;
    private String text;
    private String jumpToTextId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date textTimeCreated;
    private String authorId;
    private String authorUsername;
    private String authorNickname;
    private String authorAvatarFilename;
    private Boolean read;
    private String type;
}
