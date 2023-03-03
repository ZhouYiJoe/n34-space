package com.n34.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.n34.space.entity.Post;
import com.n34.space.entity.vo.PostVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    List<PostVo> findHot(@Param("searchText") String searchText,
                         @Param("hashtag") String hashtag,
                         @Param("categories") List<String> categories,
                         @Param("filterConfig") String filterConfig);

    List<Post> getFolloweePosts(String userId,
                                @Param("categories") List<String> categories,
                                @Param("filterConfig") String filterConfig);

    int countFolloweePosts(String userId,
                           @Param("categories") List<String> categories,
                           @Param("filterConfig") String filterConfig);
}
