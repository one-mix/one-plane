// 작성자: 김동현
package com.oneplane.post.dao;

import com.oneplane.post.domain.PostLike;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostLikeDao {

    /**
     * 좋아요 추가
     * 작성자 : 김동현
     */
    int insertPostLike(PostLike postLike);

    /**
     * 좋아요 취소
     * 작성자 : 김동현
     */
    int deletePostLike(@Param("userId") Integer userId, @Param("postId") Integer postId);

    /**
     * 특정 사용자가 특정 게시글에 좋아요를 눌렀는지 확인
     * 작성자 : 김동현
     */
    boolean existsPostLike(@Param("userId") Integer userId, @Param("postId") Integer postId);

    /**
     * 게시글의 총 좋아요 수 조회
     * 작성자 : 김동현
     */
    int countPostLikes(@Param("postId") Integer postId);

    /**
     * 특정 게시글의 좋아요 목록 조회 (최근 순)
     * 작성자 : 김동현
     */
    List<PostLike> findPostLikesByPostId(@Param("postId") Integer postId,
                                         @Param("limit") int limit);

    /**
     * 특정 사용자가 좋아요한 게시글 목록 조회
     * 작성자 : 김동현
     */
    List<PostLike> findPostLikesByUserId(@Param("userId") Integer userId,
                                         @Param("offset") int offset,
                                         @Param("size") int size);

    /**
     * 특정 사용자가 좋아요한 게시글 수 조회
     * 작성자 : 김동현
     */
    int countPostLikesByUserId(@Param("userId") Integer userId);

    /**
     * 전체 좋아요 수 조회
     * 작성자 : 김동현
     */
    int countAllPostLikes();

    /**
     * 오늘 작성된 좋아요 수
     * 작성자 : 김동현
     */
    int countTodayPostLikes();
}