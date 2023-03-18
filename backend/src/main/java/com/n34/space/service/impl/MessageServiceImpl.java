package com.n34.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.Message;
import com.n34.space.mapper.MessageMapper;
import com.n34.space.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    @Override
    public List<Message> getMessagesBetweenTwoUsers(String user1Id, String user2Id) {
        return baseMapper.getMessagesBetweenTwoUsers(user1Id, user2Id);
    }
}
