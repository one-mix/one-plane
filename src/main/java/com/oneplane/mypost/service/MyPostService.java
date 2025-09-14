package com.oneplane.mypost.service;

import com.oneplane.mypost.dao.MyPostDao;
import com.oneplane.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MyPostService {
    private final MyPostDao myPostDao;

    @Transactional(readOnly = true)
    public List<Post> getMyPosts(Integer userId) {
        log.info("내 게시글 전체 목록 조회 요청 - userId: {}", userId);
        try {
            List<Post> posts = myPostDao.selectPostsByAuthor(userId);
            log.info("내 게시글 전체 조회 완료 - 조회 건수: {}", posts.size());
            return posts;
        } catch (Exception e) {
            log.error("내 게시글 전체 조회 실패", e);
            return new ArrayList<>();
        }
    }

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
     * 팔로워가 작성한 게시글 전체 조회
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
     * 팔로워가 작성한 게시글 카테고리별 조회
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