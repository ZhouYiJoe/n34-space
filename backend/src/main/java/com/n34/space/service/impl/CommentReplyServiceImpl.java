package com.n34.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.CommentReply;
import com.n34.space.mapper.CommentReplyMapper;
import com.n34.space.service.CommentReplyService;
import org.springframework.stereotype.Service;

@Service
public class CommentReplyServiceImpl
        extends ServiceImpl<CommentReplyMapper, CommentReply>
        implements CommentReplyService {
}
