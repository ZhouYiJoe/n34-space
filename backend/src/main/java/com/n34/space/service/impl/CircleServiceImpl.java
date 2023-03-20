package com.n34.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.Circle;
import com.n34.space.mapper.CircleMapper;
import com.n34.space.service.CircleService;
import org.springframework.stereotype.Service;

@Service
public class CircleServiceImpl extends ServiceImpl<CircleMapper, Circle> implements CircleService {
}
