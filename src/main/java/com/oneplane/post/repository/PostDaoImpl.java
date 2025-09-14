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


    @Override
    public List<Post> findAllPosts() {
        log.debug("전체 게시글 목록 조회");
        return sqlSession.selectList(namespace + "findAllPosts");
    }

    @Override
    public List<Post> findPostsWithPaging(PostSearchCondition condition) {
        return sqlSession.selectList(namespace + "findPostsWithPaging", condition);
    }

    @Override
    public int countPosts(PostSearchCondition condition) {
        log.debug("게시글 총 개수 조회");
        return sqlSession.selectOne(namespace + "countPosts", condition);
    }

    @Override
    public List<Post> findPostsByCategory(String category, int offset, int size) {

        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("offset", offset);
        params.put("size", size);

        return sqlSession.selectList(namespace + "findPostsByCategory", params);
    }

    @Override
    public int countPostsByCategory(String category) {
        log.debug("카테고리별 게시글 수 조회 - 카테고리: {}", category);
        return sqlSession.selectOne(namespace + "countPostsByCategory", category);
    }

    @Override
    public int insertPost(Post post) {
        return sqlSession.insert(namespace + "insertPost", post);
    }

    @Override
    public Post findPostById(Integer postId) {
        log.debug("게시글 ID로 조회: {}", postId);
        return sqlSession.selectOne(namespace + "findPostById", postId);
    }

    @Override
    public int increaseViewCount(Integer postId) {
        log.debug("조회수 증가 - postId: {}", postId);
        return sqlSession.update(namespace + "increaseViewCount", postId);
    }

    @Override
    public int deletePost(Integer postId) {
        log.debug("게시글 삭제 (소프트 딜리트) - postId: {}", postId);
        return sqlSession.update(namespace + "deletePost", postId);
    }

    @Override
    public int updatePost(Post post) {
        log.debug("게시글 수정 - postId: {}", post.getPostId());
        return sqlSession.update(namespace + "updatePost", post);
    }

    @Override
    public List<Post> findLatestPostsForMain() {
        log.debug("메인페이지용 최신글 5개 조회");
        return sqlSession.selectList(namespace + "findLatestPostsForMain");
    }

    @Override
    public List<Post> findPopularPostsForMain() {
        log.debug("메인페이지용 인기글 5개 조회");
        return sqlSession.selectList(namespace + "findPopularPostsForMain");
    }

    @Override
    public List<Post> findPopularReviewsForMain() {
        log.debug("메인페이지용 인기 후기 9개 조회");
        return sqlSession.selectList(namespace + "findPopularReviewsForMain");
    }
    @Override
    public int updatePostLikeCount(Integer postId) {
        log.debug("게시글 좋아요 수 업데이트 - postId: {}", postId);
        return sqlSession.update(namespace + "updatePostLikeCount", postId);
    }

    @Override
    public int updatePostCommentCount(Integer postId) {
        log.debug("게시글 댓글 수 업데이트 - postId: {}", postId);
        return sqlSession.update(namespace + "updatePostCommentCount", postId);
    }

    @Override
    public int updatePostCounts(Integer postId) {
        log.debug("게시글 모든 카운트 업데이트 - postId: {}", postId);
        return sqlSession.update(namespace + "updatePostCounts", postId);
    }

    @Override
    public List<Post> findPopularPostsWithPaging(PostSearchCondition condition) {
        log.debug("인기 게시글 페이징 조회 - condition: {}", condition);
        return sqlSession.selectList(namespace + "findPopularPostsWithPaging", condition);
    }

    @Override
    public int countPopularPosts(PostSearchCondition condition) {
        log.debug("인기 게시글 총 개수 조회 - condition: {}", condition);
        return sqlSession.selectOne(namespace + "countPopularPosts", condition);
    }
}
