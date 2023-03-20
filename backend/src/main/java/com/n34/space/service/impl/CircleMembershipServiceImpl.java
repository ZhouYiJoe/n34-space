package com.n34.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.CircleMembership;
import com.n34.space.mapper.CircleMembershipMapper;
import com.n34.space.service.CircleMembershipService;
import org.springframework.stereotype.Service;

@Service
public class CircleMembershipServiceImpl extends ServiceImpl<CircleMembershipMapper, CircleMembership> implements CircleMembershipService {
}
