// 작성자: 김동현
package com.oneplane.post.repository;

import com.oneplane.post.dao.PostDao;
import com.oneplane.post.domain.Post;
import com.oneplane.post.domain.PostSearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class PostDaoImpl implements PostDao {
    @Autowired
    private SqlSession sqlSession;
    private final String namespace = "com.oneplane.post.mapper.PostMapper.";

    /**
     * 게시글 전체 조회
     * 작성자 : 김동현
     */
    @Override
    public List<Post> findAllPosts() {
        log.debug("전체 게시글 목록 조회");
        return sqlSession.selectList(namespace + "findAllPosts");
    }

    /**
     * 페이징된 게시글 목록 조회
     * 작성자 : 김동현
     */
    @Override
    public List<Post> findPostsWithPaging(PostSearchCondition condition) {
        return sqlSession.selectList(namespace + "findPostsWithPaging", condition);
    }

    /**
     * 검색 조건에 따른 총 게시글 수 조회
     * 작성자 : 김동현
     */
    @Override
    public int countPosts(PostSearchCondition condition) {
        log.debug("게시글 총 개수 조회");
        return sqlSession.selectOne(namespace + "countPosts", condition);
    }

    /**
     * 카테고리별 게시글 목록 조회
     * 작성자 : 김동현
     */
    @Override
    public List<Post> findPostsByCategory(String category, int offset, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("offset", offset);
        params.put("size", size);

        return sqlSession.selectList(namespace + "findPostsByCategory", params);
    }

    /**
     * 카테고리별 게시글 수 조회
     * 작성자 : 김동현
     */
    @Override
    public int countPostsByCategory(String category) {
        log.debug("카테고리별 게시글 수 조회 - 카테고리: {}", category);
        return sqlSession.selectOne(namespace + "countPostsByCategory", category);
    }

    /**
     * 게시글 등록
     * 작성자 : 김동현
     */
    @Override
    public int insertPost(Post post) {
        return sqlSession.insert(namespace + "insertPost", post);
    }

    /**
     * 게시글 ID로 조회
     * 작성자 : 김동현
     */
    @Override
    public Post findPostById(Integer postId) {
        log.debug("게시글 ID로 조회: {}", postId);
        return sqlSession.selectOne(namespace + "findPostById", postId);
    }

    /**
     * 조회수 증가
     * 작성자 : 김동현
     */
    @Override
    public int increaseViewCount(Integer postId) {
        log.debug("조회수 증가 - postId: {}", postId);
        return sqlSession.update(namespace + "increaseViewCount", postId);
    }

    /**
     * 게시글 삭제 (소프트 삭제)
     * 작성자 : 김동현
     */
    @Override
    public int deletePost(Integer postId) {
        log.debug("게시글 삭제 (소프트 삭제) - postId: {}", postId);
        return sqlSession.update(namespace + "deletePost", postId);
    }

    /**
     * 게시글 수정
     * 작성자 : 김동현
     */
    @Override
    public int updatePost(Post post) {
        log.debug("게시글 수정 - postId: {}", post.getPostId());
        return sqlSession.update(namespace + "updatePost", post);
    }

    /**
     * 메인페이지용 최신글 조회 (5개)
     * 작성자 : 김동현
     */
    @Override
    public List<Post> findLatestPostsForMain() {
        log.debug("메인페이지용 최신글 5개 조회");
        return sqlSession.selectList(namespace + "findLatestPostsForMain");
    }

    /**
     * 메인페이지용 인기글 조회 (5개)
     * 작성자 : 김동현
     */
    @Override
    public List<Post> findPopularPostsForMain() {
        log.debug("메인페이지용 인기글 5개 조회");
        return sqlSession.selectList(namespace + "findPopularPostsForMain");
    }

    /**
     * 메인페이지용 인기 후기 조회 (9개)
     * 작성자 : 김동현
     */
    @Override
    public List<Post> findPopularReviewsForMain() {
        log.debug("메인페이지용 인기 후기 9개 조회");
        return sqlSession.selectList(namespace + "findPopularReviewsForMain");
    }

    /**
     * 게시글의 좋아요 수 업데이트
     * 작성자 : 김동현
     */
    @Override
    public int updatePostLikeCount(Integer postId) {
        log.debug("게시글 좋아요 수 업데이트 - postId: {}", postId);
        return sqlSession.update(namespace + "updatePostLikeCount", postId);
    }

    /**
     * 게시글의 댓글 수 업데이트
     * 작성자 : 김동현
     */
    @Override
    public int updatePostCommentCount(Integer postId) {
        log.debug("게시글 댓글 수 업데이트 - postId: {}", postId);
        return sqlSession.update(namespace + "updatePostCommentCount", postId);
    }

    /**
     * 게시글의 모든 카운트 업데이트
     * 작성자 : 김동현
     */
    @Override
    public int updatePostCounts(Integer postId) {
        log.debug("게시글 모든 카운트 업데이트 - postId: {}", postId);
        return sqlSession.update(namespace + "updatePostCounts", postId);
    }

    /**
     * 인기 게시글 페이징 조회
     * 작성자 : 김동현
     */
    @Override
    public List<Post> findPopularPostsWithPaging(PostSearchCondition condition) {
        log.debug("인기 게시글 페이징 조회 - condition: {}", condition);
        return sqlSession.selectList(namespace + "findPopularPostsWithPaging", condition);
    }

    /**
     * 인기 게시글 총 개수 조회
     * 작성자 : 김동현
     */
    @Override
    public int countPopularPosts(PostSearchCondition condition) {
        log.debug("인기 게시글 총 개수 조회 - condition: {}", condition);
        return sqlSession.selectOne(namespace + "countPopularPosts", condition);
    }

    /**
     * 전체 게시글 수 조회 (삭제되지 않은)
     * 작성자 : 김동현
     */
    @Override
    public int countAllPosts() {
        log.debug("전체 게시글 수 조회");
        return sqlSession.selectOne(namespace + "countAllPosts");
    }

    /**
     * 오늘 작성된 게시글 수
     * 작성자 : 김동현
     */
    @Override
    public int countTodayPosts() {
        log.debug("오늘 작성된 게시글 수 조회");
        return sqlSession.selectOne(namespace + "countTodayPosts");
    }

    /**
     * 전체 좋아요 수 합계
     * 작성자 : 김동현
     */
    @Override
    public int countTotalLikes() {
        log.debug("전체 좋아요 수 합계 조회");
        Integer result = sqlSession.selectOne(namespace + "countTotalLikes");
        return result != null ? result : 0;
    }

    /**
     * 카테고리별 게시글 수 (차트용)
     * 작성자 : 김동현
     */
    @Override
    public List<Map<String, Object>> getCategoryStats() {
        log.debug("카테고리별 게시글 수 통계 조회");
        return sqlSession.selectList(namespace + "getCategoryStats");
    }

    /**
     * 월별 게시글 작성 추이 (최근 6개월)
     * 작성자 : 김동현
     */
    @Override
    public List<Map<String, Object>> getMonthlyPostTrend() {
        log.debug("월별 게시글 작성 추이 조회 (최근 6개월)");
        return sqlSession.selectList(namespace + "getMonthlyPostTrend");
    }

    /**
     * 인기 게시글 TOP 10
     * 작성자 : 김동현
     */
    @Override
    public List<Post> getTop10PopularPosts() {
        log.debug("인기 게시글 TOP 10 조회");
        return sqlSession.selectList(namespace + "getTop10PopularPosts");
    }

    /**
     * 최근 7일간 일별 게시글 수
     * 작성자 : 김동현
     */
    @Override
    public List<Map<String, Object>> getDailyPostStats() {
        log.debug("최근 7일간 일별 게시글 수 조회");
        return sqlSession.selectList(namespace + "getDailyPostStats");
    }
}