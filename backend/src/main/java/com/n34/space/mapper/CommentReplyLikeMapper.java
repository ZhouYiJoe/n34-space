package com.n34.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.n34.space.entity.CommentLike;
import com.n34.space.entity.CommentReply;
import com.n34.space.entity.CommentReplyLike;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentReplyLikeMapper extends BaseMapper<CommentReplyLike> {
}
