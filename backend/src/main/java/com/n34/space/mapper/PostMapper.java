package com.n34.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.n34.space.entity.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
