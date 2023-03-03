package com.n34.space.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.n34.space.entity.Hashtag;
import com.n34.space.entity.Post;
import com.n34.space.entity.vo.HashtagStatisticItem;
import com.n34.space.service.HashtagService;
import com.n34.space.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hashtag")
@RequiredArgsConstructor
public class HashtagController {
    private final HashtagService hashtagService;
    private final PostService postService;

    @RequestMapping("/top")
    public List<HashtagStatisticItem> getTopN(@RequestParam(required = false) Integer n) {
        List<Hashtag> hashtags = hashtagService.list();
        List<HashtagStatisticItem> statistic = new ArrayList<>();
        for (Hashtag hashtag : hashtags) {
            LambdaQueryWrapper<Post> cond = new LambdaQueryWrapper<>();
            cond.like(Post::getContent, "#" + hashtag.getName() + "#");
            int count = postService.count(cond);
            HashtagStatisticItem hashtagStatisticItem = new HashtagStatisticItem()
                    .setHashtag(hashtag.getName())
                    .setCount(count);
            statistic.add(hashtagStatisticItem);
        }
        statistic.sort((a, b) -> -Integer.compare(a.getCount(), b.getCount()));
        return n == null ? statistic : statistic.subList(0, Math.min(n, statistic.size()));
    }
}
