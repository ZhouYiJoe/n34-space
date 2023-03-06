package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.n34.space.entity.Follow;
import com.n34.space.entity.User;
import com.n34.space.entity.dto.UserDto;
import com.n34.space.entity.vo.UserVo;
import com.n34.space.service.FollowService;
import com.n34.space.service.SpringSecurityService;
import com.n34.space.service.UserService;
import com.n34.space.service.impl.MinioService;
import com.n34.space.utils.BeanCopyUtils;
import com.n34.space.utils.RegexUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SpringSecurityService springSecurityService;
    private final MinioService minioService;
    private final FollowService followService;

    @GetMapping("/self")
    public UserVo getSelfInfo() {
        String userId = springSecurityService.getCurrentUserId();
        User user = userService.getById(userId);
        Assert.notNull(user, "用户不存在");
        return BeanCopyUtils.copyObject(user, UserVo.class);
    }

    @GetMapping("/{username}")
    public UserVo getUserInfo(@PathVariable String username) {
        LambdaQueryWrapper<User> cond = new LambdaQueryWrapper<>();
        cond.eq(User::getUsername, username);
        User user = userService.getOne(cond);
        if (user == null) return null;
        UserVo userVo = BeanCopyUtils.copyObject(user, UserVo.class);
        LambdaQueryWrapper<Follow> cond2 = new LambdaQueryWrapper<>();
        cond2.eq(Follow::getFollowerId, springSecurityService.getCurrentUserId())
                .eq(Follow::getFolloweeId, user.getId());
        userVo.setFollowedByMe(followService.count(cond2) == 1);
        return userVo;
    }

    @PostMapping("/avatar")
    public String uploadAvatar(MultipartFile avatarFile) {
        List<String> avatarFilenames = minioService.upload(new MultipartFile[]{avatarFile});
        String currentUserId = springSecurityService.getCurrentUserId();
        User user = userService.getById(currentUserId);
        minioService.removeObjects(Collections.singletonList(user.getAvatarFilename()));
        LambdaUpdateWrapper<User> cond = new LambdaUpdateWrapper<>();
        cond.set(User::getAvatarFilename, avatarFilenames.get(0)).eq(User::getId, currentUserId);
        userService.update(cond);
        return avatarFilenames.get(0);
    }

    @PostMapping("/wallpaper")
    public String uploadWallpaper(MultipartFile wallpaperFile) {
        List<String> wallpaperFilenames = minioService.upload(new MultipartFile[]{wallpaperFile});
        String currentUserId = springSecurityService.getCurrentUserId();
        User user = userService.getById(currentUserId);
        minioService.removeObjects(Collections.singletonList(user.getWallpaperFilename()));
        LambdaUpdateWrapper<User> cond = new LambdaUpdateWrapper<>();
        cond.set(User::getWallpaperFilename, wallpaperFilenames.get(0)).eq(User::getId, currentUserId);
        userService.update(cond);
        return wallpaperFilenames.get(0);
    }

    @PostMapping("/nickname")
    public Boolean changeNickname(@RequestBody UserDto userDto) {
        LambdaUpdateWrapper<User> cond = new LambdaUpdateWrapper<>();
        cond.set(User::getNickname, userDto.getNickname());
        cond.eq(User::getUsername, userDto.getUsername());
        return userService.update(cond);
    }

    @GetMapping("/my_followees")
    public List<UserVo> getFollowees() {
        LambdaQueryWrapper<Follow> cond = new LambdaQueryWrapper<>();
        cond.eq(Follow::getFollowerId, springSecurityService.getCurrentUserId());
        List<Follow> follows = followService.list(cond);
        List<UserVo> followees = new ArrayList<>();
        for (Follow follow : follows) {
            User user = userService.getById(follow.getFolloweeId());
            UserVo userVo = BeanCopyUtils.copyObject(user, UserVo.class);
            followees.add(userVo.setFollowedByMe(true));
        }
        return followees;
    }

    @PostMapping("/filterConfig")
    public Boolean changeFilterConfig(@RequestBody UserDto userDto) {
        String currentUserId = springSecurityService.getCurrentUserId();
        LambdaUpdateWrapper<User> cond = new LambdaUpdateWrapper<>();
        cond.set(User::getFilterConfig, userDto.getFilterConfig());
        cond.eq(User::getId, currentUserId);
        return userService.update(cond);
    }

    @GetMapping
    public List<UserVo> getList(@RequestParam(required = false) String searchText,
                                @RequestParam(required = false) Boolean sortByFollower,
                                @RequestParam(required = false) Integer topN,
                                @RequestParam(required = false) Boolean includeFollowed) {
        if (searchText != null) {
            searchText = RegexUtils.correctSearchText(searchText);
        }
        LambdaQueryWrapper<User> cond = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(searchText)) {
            cond.like(User::getUsername, searchText).or().like(User::getNickname, searchText);
        }
        List<User> users = userService.list(cond);
        List<UserVo> userVos = BeanCopyUtils.copyList(users, UserVo.class);
        String currentUserId = springSecurityService.getCurrentUserId();
        for (UserVo userVo : userVos) {
            LambdaQueryWrapper<Follow> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(Follow::getFolloweeId, userVo.getId());
            int count = followService.count(cond2);
            userVo.setNumFollower(count);
            cond2.eq(Follow::getFollowerId, currentUserId);
            count = followService.count(cond2);
            userVo.setFollowedByMe(count == 1);
        }
        if (sortByFollower != null && sortByFollower) {
            userVos.sort((a, b) -> -Integer.compare(a.getNumFollower(), b.getNumFollower()));
        }
        if (includeFollowed != null && !includeFollowed) {
            userVos = userVos.stream().filter((userVo) -> !userVo.getFollowedByMe()).collect(Collectors.toList());
        }
        return topN == null ? userVos : userVos.subList(0, Math.min(topN, userVos.size()));
    }
}
