package com.n34.space.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PostDto {
    private Long id;
    private String content;
    private Long authorId;
}
