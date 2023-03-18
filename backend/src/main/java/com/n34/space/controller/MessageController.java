package com.n34.space.controller;

import com.n34.space.entity.Message;
import com.n34.space.entity.User;
import com.n34.space.entity.vo.MessageUserListItem;
import com.n34.space.service.MessageService;
import com.n34.space.service.SpringSecurityService;
import com.n34.space.service.UserService;
import com.n34.space.utils.AiUtils;
import lombok.RequiredArgsConstructor;
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
                .select(Message::getContent)
                .eq(Message::getReceiverId, currentUserId)
                .list();
        return messages.stream().filter(message -> !message.getExtreme()
                        || filterConfig.charAt(categories.indexOf(message.getCategory())) == '0')
                .count();
    }

    @GetMapping("/getMessageUserList")
    public List<MessageUserListItem> getMessageUserList() {
        String currentUserId = springSecurityService.getCurrentUserId();
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
        for (User sender : senders) {
            messagesReceivedByMe = messageService.getMessagesBetweenTwoUsers(sender.getId(), currentUserId);
            Message latestMessage = messagesReceivedByMe.get(messagesReceivedByMe.size() - 1);
            Integer numNewMessage = messageService.lambdaQuery()
                    .eq(Message::getSenderId, sender.getId())
                    .eq(Message::getReceiverId, currentUserId)
                    .eq(Message::getRead, false)
                    .count();
            MessageUserListItem messageUserListItem = new MessageUserListItem()
                    .setUserId(sender.getId())
                    .setUsername(sender.getUsername())
                    .setNickname(sender.getNickname())
                    .setAvatarFilename(sender.getAvatarFilename())
                    .setLatestMessageContent(latestMessage.getContent())
                    .setNumNewMessage(numNewMessage);
            messageUserList.add(messageUserListItem);
        }
        return messageUserList;
    }
}
