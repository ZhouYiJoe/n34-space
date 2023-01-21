package com.n34.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.Post;
import com.n34.space.entity.vo.PostVo;
import com.n34.space.mapper.PostMapper;
import com.n34.space.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Override
    public List<PostVo> findHot(Integer offset, Integer limit) {
        return baseMapper.findHot(offset, limit);
    }

    @Override
    public List<Post> getFolloweePosts(String userId, Integer offset, Integer limit) {
        return baseMapper.getFolloweePosts(userId, offset, limit);
    }

    @Override
    public int countFolloweePosts(String userId) {
        return baseMapper.countFolloweePosts(userId);
    }
}
