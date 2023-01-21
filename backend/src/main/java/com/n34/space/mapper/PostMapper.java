package com.n34.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.n34.space.entity.Post;
import com.n34.space.entity.vo.PostVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    List<PostVo> findHot(Integer offset, Integer limit);

    List<Post> getFolloweePosts(String userId, Integer offset, Integer limit);
    
    int countFolloweePosts(String userId);
}
