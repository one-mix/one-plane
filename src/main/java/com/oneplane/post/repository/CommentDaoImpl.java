// 작성자: 김동현
package com.oneplane.post.repository;

import com.oneplane.post.dao.CommentDao;
import com.oneplane.post.domain.Comment;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentDaoImpl implements CommentDao {

    @Autowired
    private SqlSession sqlSession;
    private static final String NAMESPACE = "com.oneplane.post.dao.CommentDao.";

    /**
     * 댓글 작성
     * 작성자 : 김동현
     */
    @Override
    public int insertComment(Comment comment) {
        return sqlSession.insert(NAMESPACE + "insertComment", comment);
    }

    /**
     * 댓글 삭제
     * 작성자 : 김동현
     */
    @Override
    public int deleteComment(Integer commentId) {
        return sqlSession.update(NAMESPACE + "deleteComment", commentId);
    }

    /**
     * 댓글 ID로 조회
     * 작성자 : 김동현
     */
    @Override
    public Comment findCommentById(Integer commentId) {
        return sqlSession.selectOne(NAMESPACE + "findCommentById", commentId);
    }

    /**
     * 게시글의 모든 댓글 조회 (페이징)
     * 작성자 : 김동현
     */
    @Override
    public List<Comment> findCommentsByPostId(Integer postId, int offset, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);
        params.put("offset", offset);
        params.put("size", size);
        return sqlSession.selectList(NAMESPACE + "findCommentsByPostId", params);
    }

    /**
     * 게시글의 총 댓글 수 조회
     * 작성자 : 김동현
     */
    @Override
    public int countCommentsByPostId(Integer postId) {
        return sqlSession.selectOne(NAMESPACE + "countCommentsByPostId", postId);
    }

    /**
     * 사용자의 댓글 목록 조회 (페이징)
     * 작성자 : 김동현
     */
    @Override
    public List<Comment> findCommentsByUserId(Integer userId, int offset, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", offset);
        params.put("size", size);
        return sqlSession.selectList(NAMESPACE + "findCommentsByUserId", params);
    }

    /**
     * 사용자의 총 댓글 수 조회
     * 작성자 : 김동현
     */
    @Override
    public int countCommentsByUserId(Integer userId) {
        return sqlSession.selectOne(NAMESPACE + "countCommentsByUserId", userId);
    }

    /**
     * 게시글 삭제 시 모든 댓글 삭제
     * 작성자 : 김동현
     */
    @Override
    public int deleteCommentsByPostId(Integer postId) {
        return sqlSession.update(NAMESPACE + "deleteCommentsByPostId", postId);
    }

    /**
     * 관리자용 - 전체 댓글 목록 조회 (검색 포함)
     * 작성자 : 김동현
     */
    @Override
    public List<Comment> findAllComments(int offset, int size, String search) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("size", size);
        params.put("search", search);
        return sqlSession.selectList(NAMESPACE + "findAllComments", params);
    }

    /**
     * 관리자용 - 전체 댓글 수 조회 (검색 포함)
     * 작성자 : 김동현
     */
    @Override
    public int countAllComments(String search) {
        return sqlSession.selectOne(NAMESPACE + "countAllComments", search);
    }

    /**
     * 전체 댓글 수 조회 (통계용)
     * 작성자 : 김동현
     */
    @Override
    public int countAllComments() {
        return sqlSession.selectOne(NAMESPACE + "countAllCommentsForStats");
    }

    /**
     * 오늘 작성된 댓글 수
     * 작성자 : 김동현
     */
    @Override
    public int countTodayComments() {
        return sqlSession.selectOne(NAMESPACE + "countTodayComments");
    }

    /**
     * 최근 7일간 일별 댓글 수
     * 작성자 : 김동현
     */
    @Override
    public List<Map<String, Object>> getDailyCommentStats() {
        return sqlSession.selectList(NAMESPACE + "getDailyCommentStats");
    }
}