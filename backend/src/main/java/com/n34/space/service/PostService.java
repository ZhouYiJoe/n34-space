package com.n34.space.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.n34.space.entity.Post;
import com.n34.space.entity.vo.PostVo;

import java.util.List;

public interface PostService extends IService<Post> {
    List<PostVo> findHot(Integer offset, Integer limit);

    List<Post> getFolloweePosts(String userId, Integer offset, Integer limit);

    int countFolloweePosts(String userId);
}
