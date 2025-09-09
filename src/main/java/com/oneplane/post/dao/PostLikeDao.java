package com.oneplane.post.dao;

import com.oneplane.post.domain.PostLike;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostLikeDao {
    // 좋아요 추가
    int insertPostLike(PostLike postLike);

    // 좋아요 취소
    int deletePostLike(@Param("userId") Integer userId, @Param("postId") Integer postId);

    // 특정 사용자가 특정 게시글에 좋아요를 눌렀는지 확인
    boolean existsPostLike(@Param("userId") Integer userId, @Param("postId") Integer postId);

    // 게시글의 총 좋아요 수 조회
    int countPostLikes(@Param("postId") Integer postId);

    // 특정 게시글의 좋아요 목록 조회 (최근 순)
    List<PostLike> findPostLikesByPostId(@Param("postId") Integer postId,
                                         @Param("limit") int limit);

    // 특정 사용자가 좋아요한 게시글 목록 조회
    List<PostLike> findPostLikesByUserId(@Param("userId") Integer userId,
                                         @Param("offset") int offset,
                                         @Param("size") int size);

    // 특정 사용자가 좋아요한 게시글 수 조회
    int countPostLikesByUserId(@Param("userId") Integer userId);
}
