package com.n34.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.Follow;
import com.n34.space.mapper.FollowMapper;
import com.n34.space.service.FollowService;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {
}
