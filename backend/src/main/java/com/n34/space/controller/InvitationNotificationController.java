package com.n34.space.controller;

import com.n34.space.entity.Circle;
import com.n34.space.entity.InvitationNotification;
import com.n34.space.entity.User;
import com.n34.space.entity.vo.InvitationNotificationVo;
import com.n34.space.service.CircleService;
import com.n34.space.service.InvitationNotificationService;
import com.n34.space.service.SpringSecurityService;
import com.n34.space.service.UserService;
import com.n34.space.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invitation_notification")
public class InvitationNotificationController {
    private final SpringSecurityService springSecurityService;
    private final InvitationNotificationService invitationNotificationService;
    private final UserService userService;
    private final CircleService circleService;

    @PostMapping("/sendInvitation")
    public void sendInvitation(@RequestBody InvitationNotification invitationNotification) {

        String currentUserId = springSecurityService.getCurrentUserId();
        invitationNotification.setState(InvitationNotification.WAITING)
                .setInviterId(currentUserId)
                .setRead(false);
        invitationNotificationService.save(invitationNotification);
    }

    @PutMapping
    public void updateById(@RequestBody InvitationNotification invitationNotification) {
        invitationNotificationService.updateById(invitationNotification);
    }

    @GetMapping("/countNewNotification")
    public Integer countNewNotification() {
        String currentUserId = springSecurityService.getCurrentUserId();
        return invitationNotificationService.lambdaQuery()
                .eq(InvitationNotification::getInviteeId, currentUserId)
                .eq(InvitationNotification::getRead, false)
                .count();
    }

    @GetMapping("/getNewNotification")
    public List<InvitationNotificationVo> getNewNotification() {
        String currentUserId = springSecurityService.getCurrentUserId();
        List<InvitationNotification> invitationNotifications = invitationNotificationService.lambdaQuery()
                .eq(InvitationNotification::getInviteeId, currentUserId)
                .orderByDesc(InvitationNotification::getTimeCreated)
                .list();
        List<InvitationNotificationVo> invitationNotificationVos =
                BeanCopyUtils.copyList(invitationNotifications, InvitationNotificationVo.class);
        for (InvitationNotificationVo invitationNotificationVo : invitationNotificationVos) {
            User inviter = userService.getById(invitationNotificationVo.getInviterId());
            Circle circle = circleService.getById(invitationNotificationVo.getCircleId());
            invitationNotificationVo
                    .setInviterUsername(inviter.getUsername())
                    .setInviterNickname(inviter.getNickname())
                    .setInviterAvatarFilename(inviter.getAvatarFilename())
                    .setCircleName(circle.getName());
            if (!invitationNotificationVo.getRead()) {
                invitationNotificationService.lambdaUpdate()
                        .set(InvitationNotification::getRead, true)
                        .eq(InvitationNotification::getId, invitationNotificationVo.getId())
                        .update();
            }
        }
        return invitationNotificationVos;
    }
}
