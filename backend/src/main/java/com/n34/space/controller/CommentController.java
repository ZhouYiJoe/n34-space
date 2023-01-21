package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.n34.space.entity.Comment;
import com.n34.space.entity.CommentLike;
import com.n34.space.entity.CommentReply;
import com.n34.space.entity.User;
import com.n34.space.entity.dto.CommentDto;
import com.n34.space.entity.vo.CommentVo;
import com.n34.space.service.*;
import com.n34.space.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Validated
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    private final SpringSecurityService springSecurityService;
    private final UserService userService;
    private final CommentReplyService commentReplyService;

    @PostMapping
    public Boolean postNew(@RequestBody CommentDto commentDto) {
        Assert.notNull(commentDto.getUserId(), "userId为null");
        Assert.notNull(commentDto.getPostId(), "postId为null");
        Assert.isNull(commentDto.getId(), "无权访问");
        Assert.isTrue(commentDto.getUserId().equals(springSecurityService.getCurrentUserId()), "无权访问");
        return commentService.save(BeanCopyUtils.copyObject(commentDto, Comment.class));
    }

    @GetMapping
    public IPage<CommentVo> findPage(@NotNull @RequestParam String postId,
                                     @NotNull @RequestParam Integer pageNo,
                                     @NotNull @RequestParam Integer pageSize) {
        LambdaQueryWrapper<Comment> cond = new LambdaQueryWrapper<>();
        cond.eq(Comment::getPostId, postId);
        cond.orderByDesc(Comment::getTimeCreated);
        IPage<Comment> commentPage = new Page<>(pageNo, pageSize);
        commentService.page(commentPage, cond);
        IPage<CommentVo> commentVoPage = BeanCopyUtils.copyPage(commentPage, CommentVo.class);

        for (CommentVo commentVo : commentVoPage.getRecords()) {
            String userId = commentVo.getUserId();
            Assert.notNull(userId, "userId为null");
            User user = userService.getById(userId);
            Assert.notNull(user, "user为null");
            commentVo.setUsername(user.getUsername());
            commentVo.setNickname(user.getNickname());
            commentVo.setAvatarFilename(user.getAvatarFilename());

            LambdaQueryWrapper<CommentLike> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(CommentLike::getCommentId, commentVo.getId());
            commentVo.setNumLike(commentLikeService.count(cond2));

            cond2 = new LambdaQueryWrapper<>();
            cond2.eq(CommentLike::getCommentId, commentVo.getId());
            cond2.eq(CommentLike::getUserId, springSecurityService.getCurrentUserId());
            commentVo.setLikedByMe(commentLikeService.count(cond2) == 1);

            LambdaQueryWrapper<CommentReply> cond3 = new LambdaQueryWrapper<>();
            cond3.eq(CommentReply::getCommentId, commentVo.getId());
            commentVo.setNumReply(commentReplyService.count(cond3));
        }

        return commentVoPage;
    }

    @PutMapping
    public Boolean update(@RequestBody CommentDto commentDto) {
        Assert.notNull(commentDto.getId(), "id为null");
        Comment comment = commentService.getById(commentDto.getId());
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(comment.getUserId()), "无权访问");
        Assert.isTrue(currentUserId.equals(commentDto.getUserId()), "无权访问");
        Assert.isNull(commentDto.getPostId(), "无权访问");
        return commentService.updateById(BeanCopyUtils.copyObject(commentDto, Comment.class));
    }

    @DeleteMapping("/{id}")
    public Boolean remove(@NotNull @PathVariable String id) {
        Comment comment = commentService.getById(id);
        Assert.notNull(comment, "评论不存在");
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(comment.getUserId()), "无权访问");
        return commentService.removeById(id);
    }
}
