// 작성자: 김동현
package com.oneplane.post.repository;

import com.oneplane.post.dao.PostLikeDao;
import com.oneplane.post.domain.PostLike;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class PostLikeDaoImpl implements PostLikeDao {

    @Autowired
    private SqlSession sqlSession;
    private final String namespace = "com.oneplane.post.mapper.PostLikeMapper.";

    /**
     * 좋아요 추가
     * 작성자 : 김동현
     */
    @Override
    public int insertPostLike(PostLike postLike) {
        log.debug("좋아요 추가 - userId: {}, postId: {}", postLike.getUserId(), postLike.getPostId());
        return sqlSession.insert(namespace + "insertPostLike", postLike);
    }

    /**
     * 좋아요 취소
     * 작성자 : 김동현
     */
    @Override
    public int deletePostLike(Integer userId, Integer postId) {
        log.debug("좋아요 취소 - userId: {}, postId: {}", userId, postId);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("postId", postId);
        return sqlSession.delete(namespace + "deletePostLike", params);
    }

    /**
     * 특정 사용자가 특정 게시글에 좋아요를 눌렀는지 확인
     * 작성자 : 김동현
     */
    @Override
    public boolean existsPostLike(Integer userId, Integer postId) {
        log.debug("좋아요 존재 확인 - userId: {}, postId: {}", userId, postId);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("postId", postId);
        Integer count = sqlSession.selectOne(namespace + "existsPostLike", params);
        return count != null && count > 0;
    }

    /**
     * 게시글의 총 좋아요 수 조회
     * 작성자 : 김동현
     */
    @Override
    public int countPostLikes(Integer postId) {
        log.debug("게시글 좋아요 수 조회 - postId: {}", postId);
        return sqlSession.selectOne(namespace + "countPostLikes", postId);
    }

    /**
     * 특정 게시글의 좋아요 목록 조회 (최근 순)
     * 작성자 : 김동현
     */
    @Override
    public List<PostLike> findPostLikesByPostId(Integer postId, int limit) {
        log.debug("게시글 좋아요 목록 조회 - postId: {}, limit: {}", postId, limit);
        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);
        params.put("limit", limit);
        return sqlSession.selectList(namespace + "findPostLikesByPostId", params);
    }

    /**
     * 특정 사용자가 좋아요한 게시글 목록 조회
     * 작성자 : 김동현
     */
    @Override
    public List<PostLike> findPostLikesByUserId(Integer userId, int offset, int size) {
        log.debug("사용자 좋아요 목록 조회 - userId: {}, offset: {}, size: {}", userId, offset, size);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", offset);
        params.put("size", size);
        return sqlSession.selectList(namespace + "findPostLikesByUserId", params);
    }

    /**
     * 특정 사용자가 좋아요한 게시글 수 조회
     * 작성자 : 김동현
     */
    @Override
    public int countPostLikesByUserId(Integer userId) {
        log.debug("사용자 좋아요 수 조회 - userId: {}", userId);
        return sqlSession.selectOne(namespace + "countPostLikesByUserId", userId);
    }

    /**
     * 전체 좋아요 수 조회
     * 작성자 : 김동현
     */
    @Override
    public int countAllPostLikes() {
        log.debug("전체 좋아요 수 조회");
        return sqlSession.selectOne(namespace + "countAllPostLikes");
    }

    /**
     * 오늘 작성된 좋아요 수
     * 작성자 : 김동현
     */
    @Override
    public int countTodayPostLikes() {
        log.debug("오늘 작성된 좋아요 수 조회");
        return sqlSession.selectOne(namespace + "countTodayPostLikes");
    }
}