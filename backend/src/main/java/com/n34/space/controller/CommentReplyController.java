package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.n34.space.entity.*;
import com.n34.space.entity.dto.CommentReplyDto;
import com.n34.space.entity.vo.CommentReplyVo;
import com.n34.space.entity.vo.PostVo;
import com.n34.space.service.CommentReplyLikeService;
import com.n34.space.service.CommentReplyService;
import com.n34.space.service.SpringSecurityService;
import com.n34.space.service.UserService;
import com.n34.space.utils.AiUtils;
import com.n34.space.utils.BeanCopyUtils;
import com.n34.space.utils.RegexUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment_replies")
public class CommentReplyController {
    private final SpringSecurityService springSecurityService;
    private final UserService userService;
    private final CommentReplyService commentReplyService;
    private final CommentReplyLikeService commentReplyLikeService;

    @GetMapping("/{id}")
    public CommentReplyVo getById(@PathVariable String id) {
        CommentReply commentReply = commentReplyService.getById(id);
        CommentReplyVo commentReplyVo = BeanCopyUtils.copyObject(commentReply, CommentReplyVo.class);
        User user = userService.getById(commentReplyVo.getUserId());
        commentReplyVo.setNickname(user.getNickname());
        commentReplyVo.setUsername(user.getUsername());
        commentReplyVo.setAvatarFilename(user.getAvatarFilename());

        LambdaQueryWrapper<CommentReplyLike> cond2 = new LambdaQueryWrapper<>();
        cond2.eq(CommentReplyLike::getCommentReplyId, commentReplyVo.getId());
        commentReplyVo.setNumLike(commentReplyLikeService.count(cond2));

        cond2 = new LambdaQueryWrapper<>();
        cond2.eq(CommentReplyLike::getCommentReplyId, commentReplyVo.getId());
        cond2.eq(CommentReplyLike::getUserId, springSecurityService.getCurrentUserId());
        commentReplyVo.setLikedByMe(commentReplyLikeService.count(cond2) == 1);

        commentReplyVo.setHtml(RegexUtils.parseAtSymbol(commentReplyVo.getContent()));
        return commentReplyVo;
    }

    @PostMapping
    public Boolean postNew(@RequestBody CommentReplyDto commentReplyDto) {
        Assert.notNull(commentReplyDto.getUserId(), "userId为null");
        Assert.notNull(commentReplyDto.getCommentId(), "commentId为null");
        Assert.isNull(commentReplyDto.getId(), "无权访问");
        Assert.isTrue(commentReplyDto.getUserId().equals(springSecurityService.getCurrentUserId()), "无权访问");
        CommentReply commentReply = BeanCopyUtils.copyObject(commentReplyDto, CommentReply.class);
        commentReply.setCategory(AiUtils.getCategory(commentReply.getContent()));
        commentReply.setExtreme(AiUtils.getSentiment(commentReply.getContent()));
        return commentReplyService.save(commentReply);
    }

    @GetMapping
    public List<CommentReplyVo> getList(@RequestParam(required = false) String commentId,
                                        @RequestParam(required = false) String timeSortOrder) {
        String currentUserId = springSecurityService.getCurrentUserId();
        String filterConfig = userService.getById(currentUserId).getFilterConfig();
        LambdaQueryWrapper<CommentReply> cond = new LambdaQueryWrapper<>();
        cond.eq(CommentReply::getCommentId, commentId);
        if ("asc".equals(timeSortOrder)) {
            cond.orderByAsc(CommentReply::getTimeCreated);
        } else if ("desc".equals(timeSortOrder)) {
            cond.orderByDesc(CommentReply::getTimeCreated);
        }
        List<CommentReply> commentReplies = commentReplyService.list(cond);
        List<String> categories = AiUtils.getAllCategories();
        commentReplies = commentReplies.stream().filter(commentReply -> !commentReply.getExtreme()
                        || commentReply.getUserId().equals(currentUserId)
                        || filterConfig.charAt(categories.indexOf(commentReply.getCategory())) == '0')
                .collect(Collectors.toList());
        List<CommentReplyVo> commentReplyVos = BeanCopyUtils.copyList(commentReplies, CommentReplyVo.class);
        for (CommentReplyVo commentReplyVo : commentReplyVos) {
            String userId = commentReplyVo.getUserId();
            Assert.notNull(userId, "userId为null");
            User user = userService.getById(userId);
            Assert.notNull(user, "user为null");
            commentReplyVo.setUsername(user.getUsername());
            commentReplyVo.setNickname(user.getNickname());
            commentReplyVo.setAvatarFilename(user.getAvatarFilename());

            LambdaQueryWrapper<CommentReplyLike> cond2 = new LambdaQueryWrapper<>();
            cond2.eq(CommentReplyLike::getCommentReplyId, commentReplyVo.getId());
            commentReplyVo.setNumLike(commentReplyLikeService.count(cond2));

            cond2 = new LambdaQueryWrapper<>();
            cond2.eq(CommentReplyLike::getCommentReplyId, commentReplyVo.getId());
            cond2.eq(CommentReplyLike::getUserId, springSecurityService.getCurrentUserId());
            commentReplyVo.setLikedByMe(commentReplyLikeService.count(cond2) == 1);

            commentReplyVo.setHtml(RegexUtils.parseAtSymbol(commentReplyVo.getContent()));
        }

        return commentReplyVos;
    }

    @PutMapping
    public Boolean update(@RequestBody CommentReplyDto commentReplyDto) {
        Assert.notNull(commentReplyDto.getId(), "id为null");
        CommentReply commentReply = commentReplyService.getById(commentReplyDto.getId());
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(commentReply.getUserId()), "无权访问");
        Assert.isTrue(currentUserId.equals(commentReplyDto.getUserId()), "无权访问");
        Assert.isNull(commentReply.getCommentId(), "无权访问");
        CommentReply commentReply1 = BeanCopyUtils.copyObject(commentReplyDto, CommentReply.class);
        commentReply1.setCategory(AiUtils.getCategory(commentReply1.getContent()));
        commentReply1.setExtreme(AiUtils.getSentiment(commentReply1.getContent()));
        return commentReplyService.updateById(commentReply1);
    }

    @DeleteMapping("/{id}")
    public Boolean remove(@NotNull @PathVariable String id) {
        CommentReply commentReply = commentReplyService.getById(id);
        Assert.notNull(commentReply, "回复不存在");
        String currentUserId = springSecurityService.getCurrentUserId();
        Assert.isTrue(currentUserId.equals(commentReply.getUserId()), "无权访问");
        LambdaQueryWrapper<CommentReplyLike> cond = new LambdaQueryWrapper<>();
        cond.eq(CommentReplyLike::getCommentReplyId, id);
        commentReplyLikeService.remove(cond);
        return commentReplyService.removeById(id);
    }
}
