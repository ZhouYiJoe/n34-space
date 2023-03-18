package com.n34.space.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageUserListItem {
    private String userId;
    private String username;
    private String nickname;
    private String avatarFilename;
    private String latestMessageContent;
    private Integer numNewMessage;
}
