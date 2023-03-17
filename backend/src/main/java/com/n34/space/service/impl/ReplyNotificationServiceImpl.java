package com.n34.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.ReplyNotification;
import com.n34.space.mapper.ReplyNotificationMapper;
import com.n34.space.service.ReplyNotificationService;
import org.springframework.stereotype.Service;

@Service
public class ReplyNotificationServiceImpl extends ServiceImpl<ReplyNotificationMapper, ReplyNotification> implements ReplyNotificationService {
}
