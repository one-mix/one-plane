// 작성자: 김동현
package com.oneplane.post.dao;

import com.oneplane.post.domain.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommentDao {

    // 댓글 작성
    int insertComment(Comment comment);

    // 댓글 삭제
    int deleteComment(@Param("commentId") Integer commentId);

    // 댓글 ID로 조회
    Comment findCommentById(@Param("commentId") Integer commentId);

    // 게시글의 모든 댓글 조회
    List<Comment> findCommentsByPostId(@Param("postId") Integer postId,
                                       @Param("offset") int offset,
                                       @Param("size") int size);

    // 게시글의 총 댓글 수 조회
    int countCommentsByPostId(@Param("postId") Integer postId);

    // 사용자의 댓글 목록 조회
    List<Comment> findCommentsByUserId(@Param("userId") Integer userId,
                                       @Param("offset") int offset,
                                       @Param("size") int size);

    // 사용자의 총 댓글 수 조회
    int countCommentsByUserId(@Param("userId") Integer userId);

    // 게시글 삭제 시 모든 댓글 삭제
    int deleteCommentsByPostId(@Param("postId") Integer postId);

    // 관리자용 - 전체 댓글 목록 조회 (검색 포함)
    List<Comment> findAllComments(@Param("offset") int offset,
                                  @Param("size") int size,
                                  @Param("search") String search);

    // 관리자용 - 전체 댓글 수 조회 (검색 포함)
    int countAllComments(@Param("search") String search);

    int countAllComments();

    // 오늘 작성된 댓글 수
    int countTodayComments();

    // 최근 7일간 일별 댓글 수
    List<Map<String, Object>> getDailyCommentStats();
}
