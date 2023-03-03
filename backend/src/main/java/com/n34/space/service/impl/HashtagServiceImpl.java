package com.n34.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.Hashtag;
import com.n34.space.mapper.HashtagMapper;
import com.n34.space.service.HashtagService;
import org.springframework.stereotype.Service;

@Service
public class HashtagServiceImpl extends ServiceImpl<HashtagMapper, Hashtag> implements HashtagService {
}
