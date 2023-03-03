package com.n34.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.n34.space.entity.Post;
import com.n34.space.entity.vo.PostVo;
import com.n34.space.mapper.PostMapper;
import com.n34.space.service.PostService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Override
    public List<PostVo> findHot(String searchText,
                                String hashtag,
                                List<String> categories,
                                String filterConfig) {
        return baseMapper.findHot(searchText, hashtag, categories, filterConfig);
    }

    @Override
    public List<Post> getFolloweePosts(String userId,
                                       List<String> categories,
                                       String filterConfig) {
        return baseMapper.getFolloweePosts(userId, categories, filterConfig);
    }

    @Override
    public int countFolloweePosts(String userId,
                                  List<String> categories,
                                  String filterConfig) {
        return baseMapper.countFolloweePosts(userId, categories, filterConfig);
    }
}
