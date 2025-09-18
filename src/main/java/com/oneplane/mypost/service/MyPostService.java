package com.oneplane.mypost.service;

import com.oneplane.mypost.dao.MyPostDao;
import com.oneplane.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 내 게시글 및 팔로워 게시글 조회 서비스
 * MyPostController에서 호출하여 DB 조회 결과를 반환
 *
 * 작성자: 허겸
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MyPostService {

    private final MyPostDao myPostDao; // DAO for post operations

    /**
     * 로그인한 사용자의 전체 게시글 조회
     * 읽기 전용 트랜잭션
     *
     * @param userId 사용자 ID
     * @return 사용자가 작성한 게시글 리스트
     */
    @Transactional(readOnly = true)
    public List<Post> getMyPosts(Integer userId) {
        log.info("내 게시글 전체 목록 조회 요청 - userId: {}", userId);
        try {
            List<Post> posts = myPostDao.selPostsByAuthor(userId);
            log.info("내 게시글 전체 조회 완료 - 조회 건수: {}", posts.size());
            return posts;
        } catch (Exception e) {
            log.error("내 게시글 전체 조회 실패", e);
            return new ArrayList<>(); // 실패 시 빈 리스트 반환
        }
    }

    /**
     * 로그인한 사용자의 카테고리별 게시글 조회
     * 카테고리 미지정 시 전체 조회
     *
     * @param userId   사용자 ID
     * @param category 게시글 카테고리
     * @return 카테고리 필터링된 게시글 리스트
     */
    @Transactional(readOnly = true)
    public List<Post> getMyPostsByCategory(Integer userId, String category) {
        log.info("내 게시글 카테고리별 조회 요청 - userId: {}, category: {}", userId, category);
        if (category == null || category.isBlank()) {
            return getMyPosts(userId);
        }
        try {
            List<Post> posts = myPostDao.selectPostsByAuthorAndCategory(userId, category);
            log.info("내 게시글 카테고리 조회 완료 - 조회 건수: {}", posts.size());
            return posts;
        } catch (Exception e) {
            log.error("내 게시글 카테고리별 조회 실패", e);
            return new ArrayList<>();
        }
    }

    /**
     * 팔로워가 작성한 전체 게시글 조회
     *
     * @param userId 사용자 ID
     * @return 팔로워 게시글 리스트
     */
    @Transactional(readOnly = true)
    public List<Post> getFollowersPosts(Integer userId) {
        log.info("팔로워 작성 게시글 전체 조회 요청 - userId: {}", userId);
        try {
            List<Post> posts = myPostDao.selectPostsByFollowers(userId);
            log.info("팔로워 작성 게시글 전체 조회 완료 - 조회 건수: {}", posts.size());
            return posts;
        } catch (Exception e) {
            log.error("팔로워 작성 게시글 전체 조회 실패", e);
            return new ArrayList<>();
        }
    }

    /**
     * 팔로워 작성 게시글 카테고리별 조회
     * 카테고리 미지정 시 전체 조회
     *
     * @param userId   사용자 ID
     * @param category 게시글 카테고리
     * @return 카테고리 필터링된 팔로워 게시글 리스트
     */
    @Transactional(readOnly = true)
    public List<Post> getFollowersPostsByCategory(Integer userId, String category) {
        log.info("팔로워 작성 게시글 카테고리별 조회 요청 - userId: {}, category: {}", userId, category);
        if (category == null || category.isBlank()) {
            return getFollowersPosts(userId);
        }
        try {
            List<Post> posts = myPostDao.selectPostsByFollowersAndCategory(userId, category);
            log.info("팔로워 작성 게시글 카테고리 조회 완료 - 조회 건수: {}", posts.size());
            return posts;
        } catch (Exception e) {
            log.error("팔로워 작성 게시글 카테고리별 조회 실패", e);
            return new ArrayList<>();
        }
    }
}