package com.n34.space.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.n34.space.entity.Message;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface MessageService extends IService<Message> {
    List<Message> getMessagesBetweenTwoUsers(String user1Id, String user2Id);
}
