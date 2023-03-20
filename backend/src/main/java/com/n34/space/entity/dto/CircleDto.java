package com.n34.space.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CircleDto {
    private String id;
    private String creatorId;
    private String name;
    private String introduction;
}
