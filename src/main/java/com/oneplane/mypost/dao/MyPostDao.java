package com.oneplane.mypost.dao;

import com.oneplane.post.domain.Post;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MyPostDao {

    // 내 게시글 목록 조회 - 기존 SQL ID 사용
    List<Post> selectPostsByAuthor(@Param("userId") Integer userId);


    List<Post> selectPostsByAuthorAndCategory(@Param("userId") Integer userId,
                                              @Param("category") String category);


}