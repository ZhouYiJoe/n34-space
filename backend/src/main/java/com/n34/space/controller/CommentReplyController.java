package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.n34.space.entity.CommentReply;
import com.n34.space.entity.CommentReplyLike;
import com.n34.space.entity.User;
import com.n34.space.entity.dto.CommentReplyDto;
import com.n34.space.entity.vo.CommentReplyVo;
import com.n34.space.service.CommentReplyLikeService;
import com.n34.space.service.CommentReplyService;
import com.n34.space.service.SpringSecurityService;
import com.n34.space.service.UserService;
import com.n34.space.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment_replies")
public class CommentReplyController {
    private final SpringSecurityService springSecurityService;
    private final UserService userService;
    private final CommentReplyService commentReplyService;
    private final CommentReplyLikeService commentReplyLikeService;

    @PostMapping
    public Boolean postNew(@RequestBody CommentReplyDto commentReplyDto) {
        Assert.notNull(commentReplyDto.getUserId(), "userId为null");
        Assert.notNull(commentReplyDto.getCommentId(), "commentId为null");
        Assert.isNull(commentReplyDto.getId(), "无权访问");
        Assert.isTrue(commentReplyDto.getUserId().equals(springSecurityService.getCurrentUserId()), "无权访问");
        return commentReplyService.save(BeanCopyUtils.copyObject(commentReplyDto, CommentReply.class));
    }

    @GetMapping
    public IPage<CommentReplyVo> findPage(@NotNull @RequestParam Long commentId,
                                          @NotNull @RequestParam Integer pageNo,
                                          @NotNull @RequestParam Integer pageSize) {
        LambdaQueryWrapper<CommentReply> cond = new LambdaQueryWrapper<>();
        cond.eq(CommentReply::getCommentId, commentId);
        cond.orderByDesc(CommentReply::getTimeCreated);
        IPage<CommentReply> commentReplyPage = new Page<>(pageNo, pageSize);
        commentReplyService.page(commentReplyPage, cond);
        IPage<CommentReplyVo> commentReplyVoPage = BeanCopyUtils.copyPage(commentReplyPage, CommentReplyVo.class);

        for (CommentReplyVo commentReplyVo : commentReplyVoPage.getRecords()) {
            Long userId = commentReplyVo.getUserId();
            Assert.notNull(userId, "userId为null");
            User user = userService.getById(userId);
            Assert.notNull(user, "user为null");
            commentReplyVo.setUsername(user.getUsername());
            commentReplyVo.setNickname(user.getNickname());

            LambdaQueryWrapper<CommentReplyLike> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(CommentReplyLike::getCommentReplyId, commentReplyVo.getId());
            commentReplyVo.setNumLike(commentReplyLikeService.count(cond2));

            cond2 = new LambdaQueryWrapper<>();
            cond2.eq(CommentReplyLike::getCommentReplyId, commentReplyVo.getId());
            cond2.eq(CommentReplyLike::getUserId, springSecurityService.getCurrentUserId());
            commentReplyVo.setLikedByMe(commentReplyLikeService.count(cond2) == 1);
        }

        return commentReplyVoPage;
    }

    @PutMapping
    public Boolean update(@RequestBody CommentReplyDto commentReplyDto) {
        Assert.notNull(commentReplyDto.getId(), "id为null");
        CommentReply commentReply = commentReplyService.getById(commentReplyDto.getId());
        Long currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(commentReply.getUserId()), "无权访问");
        Assert.isTrue(currentUserId.equals(commentReplyDto.getUserId()), "无权访问");
        Assert.isNull(commentReply.getCommentId(), "无权访问");
        return commentReplyService.updateById(BeanCopyUtils.copyObject(commentReplyDto, CommentReply.class));
    }

    @DeleteMapping("/{id}")
    public Boolean remove(@NotNull @PathVariable Long id) {
        CommentReply commentReply = commentReplyService.getById(id);
        Assert.notNull(commentReply, "回复不存在");
        Long currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(commentReply.getUserId()), "无权访问");
        return commentReplyService.removeById(id);
    }
}
