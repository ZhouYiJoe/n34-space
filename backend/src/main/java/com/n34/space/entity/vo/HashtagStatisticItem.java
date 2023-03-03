package com.n34.space.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HashtagStatisticItem {
    private String hashtag;
    private Integer count;
}
