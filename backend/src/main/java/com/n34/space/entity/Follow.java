package com.n34.space.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Follow {
    private String followerId;
    private String followeeId;
}
