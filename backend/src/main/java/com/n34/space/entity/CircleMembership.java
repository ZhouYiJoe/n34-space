package com.n34.space.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CircleMembership {
    private String circleId;
    private String memberId;
}
