package com.n34.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.n34.space.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> getMessagesBetweenTwoUsers(String user1Id, String user2Id);

    Message getLatestMessageBetweenTwoUsers(String user1Id, String user2Id);
}
