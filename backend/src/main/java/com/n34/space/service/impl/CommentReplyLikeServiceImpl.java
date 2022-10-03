package com.n34.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.CommentReplyLike;
import com.n34.space.mapper.CommentReplyLikeMapper;
import com.n34.space.service.CommentReplyLikeService;
import org.springframework.stereotype.Service;

@Service
public class CommentReplyLikeServiceImpl
        extends ServiceImpl<CommentReplyLikeMapper, CommentReplyLike>
        implements CommentReplyLikeService {
}
