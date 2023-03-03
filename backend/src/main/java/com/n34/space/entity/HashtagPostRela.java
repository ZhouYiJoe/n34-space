package com.n34.space.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HashtagPostRela {
    private String hashtagId;
    private String postId;
}
