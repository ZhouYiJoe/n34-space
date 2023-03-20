package com.n34.space.controller;

import com.n34.space.entity.Circle;
import com.n34.space.entity.CircleMembership;
import com.n34.space.entity.User;
import com.n34.space.entity.dto.CircleDto;
import com.n34.space.entity.vo.CircleVo;
import com.n34.space.service.CircleMembershipService;
import com.n34.space.service.CircleService;
import com.n34.space.service.SpringSecurityService;
import com.n34.space.service.UserService;
import com.n34.space.service.impl.MinioService;
import com.n34.space.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/circle")
public class CircleController {
    private final CircleService circleService;
    private final UserService userService;
    private final CircleMembershipService circleMembershipService;
    private final MinioService minioService;
    private final SpringSecurityService springSecurityService;

    private CircleVo doToVo(Circle circle, boolean requireNumMember) {
        CircleVo circleVo = BeanCopyUtils.copyObject(circle, CircleVo.class);
        User creator = userService.getById(circleVo.getCreatorId());
        circleVo.setCreatorUsername(creator.getUsername());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(circle.getTimeCreated());
        circleVo.setTimeCreatedYear(calendar.get(Calendar.YEAR));
        circleVo.setTimeCreatedMonth(calendar.get(Calendar.MONTH));
        if (requireNumMember) {
            Integer numMember = circleMembershipService.lambdaQuery()
                    .eq(CircleMembership::getCircleId, circleVo.getId())
                    .count();
            circleVo.setNumMember(numMember);
        }
        return circleVo;
    }

    @PostMapping
    public CircleVo save(@RequestBody CircleDto circleDto) {
        Circle circle = BeanCopyUtils.copyObject(circleDto, Circle.class);
        if (!circleService.save(circle)) return null;
        newMembership(circle.getId(), circleDto.getCreatorId());
        return doToVo(circle, false);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable String id) {
        circleService.removeById(id);
    }

    @PutMapping
    public CircleVo update(@RequestBody CircleDto circleDto) {
        Circle circle = BeanCopyUtils.copyObject(circleDto, Circle.class);
        if (!circleService.updateById(circle)) return null;
        return doToVo(circle, false);
    }

    @PostMapping("/{id}/avatar")
    public String uploadAvatar(MultipartFile avatarFile, @PathVariable String id) {
        List<String> avatarFilenames = minioService.upload(new MultipartFile[]{avatarFile});
        Circle circle = circleService.getById(id);
        minioService.removeObjects(Collections.singletonList(circle.getAvatarFilename()));
        circleService.lambdaUpdate()
                .set(Circle::getAvatarFilename, avatarFilenames.get(0))
                .eq(Circle::getId, id)
                .update();
        return avatarFilenames.get(0);
    }

    @PostMapping("/{id}/wallpaper")
    public String uploadWallpaper(MultipartFile wallpaperFile, @PathVariable String id) {
        List<String> wallpaperFilenames = minioService.upload(new MultipartFile[]{wallpaperFile});
        Circle circle = circleService.getById(id);
        minioService.removeObjects(Collections.singletonList(circle.getWallpaperFilename()));
        circleService.lambdaUpdate()
                .set(Circle::getWallpaperFilename, wallpaperFilenames.get(0))
                .eq(Circle::getId, id)
                .update();
        return wallpaperFilenames.get(0);
    }

    @GetMapping("/newMembership")
    public void newMembership(@RequestParam String circleId, @RequestParam String memberId) {
        try {
            CircleMembership circleMembership = new CircleMembership()
                    .setCircleId(circleId)
                    .setMemberId(memberId);
            circleMembershipService.save(circleMembership);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("/removeMembership")
    public void removeMembership(@RequestParam String circleId, @RequestParam String memberId) {
        circleMembershipService.lambdaUpdate()
                .eq(CircleMembership::getCircleId, circleId)
                .eq(CircleMembership::getMemberId, memberId)
                .remove();
    }

    @GetMapping("/{id}")
    public CircleVo findById(@PathVariable String id) {
        Circle circle = circleService.getById(id);
        return doToVo(circle, true);
    }

    @GetMapping("/circlesIJoined")
    public List<CircleVo> getCirclesIJoined() {
        String currentUserId = springSecurityService.getCurrentUserId();
        List<Circle> circlesOwnedByMe = circleService.lambdaQuery()
                .eq(Circle::getCreatorId, currentUserId)
                .list();
        List<Circle> circles = new ArrayList<>(circlesOwnedByMe);
        List<CircleMembership> circleMemberships = circleMembershipService.lambdaQuery()
                .eq(CircleMembership::getMemberId, currentUserId)
                .list();
        if (!circleMemberships.isEmpty()) {
            List<String> circleIds = circleMemberships.stream()
                    .map(CircleMembership::getCircleId)
                    .collect(Collectors.toList());
            List<Circle> circlesNotOwnedByMe = circleService.lambdaQuery()
                    .in(Circle::getId, circleIds)
                    .ne(Circle::getCreatorId, currentUserId)
                    .list();
            circles.addAll(circlesNotOwnedByMe);
        }
        return circles.stream().map(circle -> doToVo(circle, false)).collect(Collectors.toList());
    }
}
