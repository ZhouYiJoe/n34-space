package com.n34.space.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.n34.space.entity.Post;
import com.n34.space.entity.vo.PostVo;

import java.util.List;

public interface PostService extends IService<Post> {
    List<PostVo> findHot(String searchText, List<String> categories,
                         String filterConfig);

    List<Post> getFolloweePosts(String userId,
                                List<String> categories,
                                String filterConfig);

    int countFolloweePosts(String userId,
                           List<String> categories,
                           String filterConfig);
}
