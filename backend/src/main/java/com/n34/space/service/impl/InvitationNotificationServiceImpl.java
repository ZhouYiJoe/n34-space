package com.n34.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.InvitationNotification;
import com.n34.space.mapper.InvitationNotificationMapper;
import com.n34.space.service.InvitationNotificationService;
import org.springframework.stereotype.Service;

@Service
public class InvitationNotificationServiceImpl extends ServiceImpl<InvitationNotificationMapper, InvitationNotification> implements InvitationNotificationService {
}
