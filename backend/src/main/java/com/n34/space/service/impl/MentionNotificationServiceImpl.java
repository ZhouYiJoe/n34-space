package com.n34.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.MentionNotification;
import com.n34.space.mapper.MentionNotificationMapper;
import com.n34.space.service.MentionNotificationService;
import org.springframework.stereotype.Service;

@Service
public class MentionNotificationServiceImpl extends ServiceImpl<MentionNotificationMapper, MentionNotification> implements MentionNotificationService {
}
