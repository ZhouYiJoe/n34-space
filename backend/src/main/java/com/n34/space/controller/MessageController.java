package com.n34.space.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.n34.space.entity.Message;
import com.n34.space.entity.User;
import com.n34.space.entity.vo.MessageUserListItem;
import com.n34.space.service.MessageService;
import com.n34.space.service.SpringSecurityService;
import com.n34.space.service.UserService;
import com.n34.space.utils.AiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SpringSecurityService springSecurityService;
    private final UserService userService;

    @PostMapping
    public Message save(@RequestBody Message message) {
        Assert.isTrue(StringUtils.hasText(message.getContent()), "私信内容不能为空");
        message.setRead(false);
        message.setCategory(AiUtils.getCategory(message.getContent()));
        message.setExtreme(AiUtils.getSentiment(message.getContent()));
        if (!messageService.save(message)) return null;
        return message;
    }

    @GetMapping("/getMessagesBetweenMeAndOther")
    public List<Message> getMessagesBetweenMeAndOther(@RequestParam(required = false) String otherUserId) {
        String currentUserId = springSecurityService.getCurrentUserId();
        User currentUser = userService.getById(currentUserId);
        String filterConfig = currentUser.getFilterConfig();
        List<String> categories = AiUtils.getAllCategories();
        List<Message> messages = messageService.getMessagesBetweenTwoUsers(currentUserId, otherUserId);
        messages = messages.stream().filter(message -> !message.getExtreme()
                        || message.getSenderId().equals(currentUserId)
                        || filterConfig.charAt(categories.indexOf(message.getCategory())) == '0')
                .collect(Collectors.toList());
        for (Message message : messages) {
            if (!message.getRead()) {
                messageService.lambdaUpdate()
                        .set(Message::getRead, true)
                        .eq(Message::getId, message.getId())
                        .update();
            }
        }
        return messages;
    }

    @GetMapping("/countNewMessagesToMe")
    public Long countNewMessagesToMe() {
        String currentUserId = springSecurityService.getCurrentUserId();
        User currentUser = userService.getById(currentUserId);
        String filterConfig = currentUser.getFilterConfig();
        List<String> categories = AiUtils.getAllCategories();
        List<Message> messages = messageService.lambdaQuery()
                .select(Message::getExtreme, Message::getCategory)
                .eq(Message::getReceiverId, currentUserId)
                .eq(Message::getRead, false)
                .list();
        return messages.stream().filter(message -> !message.getExtreme()
                        || filterConfig.charAt(categories.indexOf(message.getCategory())) == '0')
                .count();
    }

    @GetMapping("/getMessageUserList")
    public List<MessageUserListItem> getMessageUserList() {
        String currentUserId = springSecurityService.getCurrentUserId();
        User currentUser = userService.getById(currentUserId);
        String filterConfig = currentUser.getFilterConfig();
        List<String> categories = AiUtils.getAllCategories();
        List<Message> messagesSentByMe = messageService.lambdaQuery()
                .select(Message::getReceiverId)
                .eq(Message::getSenderId, currentUserId)
                .groupBy(Message::getReceiverId)
                .list();
        List<Message> messagesReceivedByMe = messageService.lambdaQuery()
                .select(Message::getSenderId)
                .eq(Message::getReceiverId, currentUserId)
                .groupBy(Message::getSenderId)
                .list();
        List<String> receiverIds = messagesSentByMe.stream().map(Message::getReceiverId).collect(Collectors.toList());
        List<String> senderIds = messagesReceivedByMe.stream().map(Message::getSenderId).collect(Collectors.toList());
        Set<String> userIds = new HashSet<>();
        userIds.addAll(receiverIds);
        userIds.addAll(senderIds);
        List<MessageUserListItem> messageUserList = new ArrayList<>();
        for (String userId : userIds) {
            List<Message> messages = messageService.getMessagesBetweenTwoUsers(userId, currentUserId);
            messages = messages.stream().filter(message -> !message.getExtreme()
                            || message.getSenderId().equals(currentUserId)
                            || filterConfig.charAt(categories.indexOf(message.getCategory())) == '0')
                    .collect(Collectors.toList());
            if (messages.isEmpty()) continue;
            Message latestMessage = messages.get(messages.size() - 1);
            long numNewMessage = messages.stream()
                    .filter(message -> !message.getRead() && currentUserId.equals(message.getReceiverId()))
                    .count();
            User user = userService.getById(userId);
            MessageUserListItem messageUserListItem = new MessageUserListItem()
                    .setUserId(userId)
                    .setUsername(user.getUsername())
                    .setNickname(user.getNickname())
                    .setAvatarFilename(user.getAvatarFilename())
                    .setLatestMessageContent(latestMessage.getContent())
                    .setNumNewMessage(numNewMessage);
            messageUserList.add(messageUserListItem);
        }
        return messageUserList;
    }
}
