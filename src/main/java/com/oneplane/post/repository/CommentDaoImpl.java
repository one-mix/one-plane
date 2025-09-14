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

    @Override
    public int insertComment(Comment comment) {
        return sqlSession.insert(NAMESPACE + "insertComment", comment);
    }

    @Override
    public int deleteComment(Integer commentId) {
        return sqlSession.update(NAMESPACE + "deleteComment", commentId);
    }

    @Override
    public Comment findCommentById(Integer commentId) {
        return sqlSession.selectOne(NAMESPACE + "findCommentById", commentId);
    }

    @Override
    public List<Comment> findCommentsByPostId(Integer postId, int offset, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);
        params.put("offset", offset);
        params.put("size", size);
        return sqlSession.selectList(NAMESPACE + "findCommentsByPostId", params);
    }

    @Override
    public int countCommentsByPostId(Integer postId) {
        return sqlSession.selectOne(NAMESPACE + "countCommentsByPostId", postId);
    }

    @Override
    public List<Comment> findCommentsByUserId(Integer userId, int offset, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", offset);
        params.put("size", size);
        return sqlSession.selectList(NAMESPACE + "findCommentsByUserId", params);
    }

    @Override
    public int countCommentsByUserId(Integer userId) {
        return sqlSession.selectOne(NAMESPACE + "countCommentsByUserId", userId);
    }

    @Override
    public int deleteCommentsByPostId(Integer postId) {
        return sqlSession.update(NAMESPACE + "deleteCommentsByPostId", postId);
    }

    @Override
    public List<Comment> findAllComments(int offset, int size, String search) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("size", size);
        params.put("search", search);
        return sqlSession.selectList(NAMESPACE + "findAllComments", params);
    }

    @Override
    public int countAllComments(String search) {
        return sqlSession.selectOne(NAMESPACE + "countAllComments", search);
    }
}
