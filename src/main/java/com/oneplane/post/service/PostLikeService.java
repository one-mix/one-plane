package com.oneplane.post.service;

import com.oneplane.post.dao.PostDao;
import com.oneplane.post.dao.PostLikeDao;
import com.oneplane.post.domain.Post;
import com.oneplane.post.domain.PostLike;
import com.oneplane.post.domain.PostLikeInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostLikeService {
    private final PostLikeDao postLikeDao;
    private final PostDao postDao;

    /**
     * 좋아요 토글 (좋아요/취소)
     */
    public boolean togglePostLike(Integer userId, Integer postId) {
        log.info("좋아요 토글 요청 - userId: {}, postId: {}", userId, postId);

        try {
            // 게시글 존재 여부 확인
            Post post = postDao.findPostById(postId);
            if (post == null || post.isDeleted()) {
                throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
            }

            // 현재 좋아요 상태 확인
            boolean isLiked = postLikeDao.existsPostLike(userId, postId);

            if (isLiked) {
                // 좋아요 취소 (물리적 삭제)
                int deleted = postLikeDao.deletePostLike(userId, postId);
                if (deleted > 0) {
                    log.info("좋아요 취소 성공 - userId: {}, postId: {}", userId, postId);
                    return false; // 좋아요 취소됨
                } else {
                    log.warn("좋아요 취소 실패 - userId: {}, postId: {}", userId, postId);
                    throw new RuntimeException("좋아요 취소에 실패했습니다.");
                }
            } else {
                // 좋아요 추가
                PostLike postLike = PostLike.create(userId, postId);
                int inserted = postLikeDao.insertPostLike(postLike);
                if (inserted > 0) {
                    log.info("좋아요 추가 성공 - userId: {}, postId: {}", userId, postId);
                    return true; // 좋아요 추가됨
                } else {
                    log.warn("좋아요 추가 실패 - userId: {}, postId: {}", userId, postId);
                    throw new RuntimeException("좋아요 추가에 실패했습니다.");
                }
            }

        } catch (Exception e) {
            log.error("좋아요 토글 중 오류 발생 - userId: {}, postId: {}", userId, postId, e);
            throw new RuntimeException("좋아요 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 특정 사용자가 특정 게시글에 좋아요를 눌렀는지 확인
     */
    @Transactional(readOnly = true)
    public boolean isPostLikedByUser(Integer userId, Integer postId) {
        if (userId == null || postId == null) {
            return false;
        }
        return postLikeDao.existsPostLike(userId, postId);
    }

    /**
     * 게시글의 총 좋아요 수 조회
     */
    @Transactional(readOnly = true)
    public int getPostLikeCount(Integer postId) {
        return postLikeDao.countPostLikes(postId);
    }

    /**
     * 게시글의 좋아요 목록 조회 (최근 순)
     */
    @Transactional(readOnly = true)
    public List<PostLike> getPostLikes(Integer postId, int limit) {
        return postLikeDao.findPostLikesByPostId(postId, limit);
    }

    /**
     * 사용자가 좋아요한 게시글 목록 조회
     */
    @Transactional(readOnly = true)
    public List<PostLike> getUserLikedPosts(Integer userId, int page, int size) {
        int offset = (page - 1) * size;
        return postLikeDao.findPostLikesByUserId(userId, offset, size);
    }

    /**
     * 사용자가 좋아요한 게시글 수 조회
     */
    @Transactional(readOnly = true)
    public int getUserLikedPostCount(Integer userId) {
        return postLikeDao.countPostLikesByUserId(userId);
    }

    /**
     * 게시글별 좋아요 상태 및 수 정보 조회
     */
    @Transactional(readOnly = true)
    public PostLikeInfo getPostLikeInfo(Integer postId, Integer userId) {
        int likeCount = getPostLikeCount(postId);
        boolean isLiked = userId != null ? isPostLikedByUser(userId, postId) : false;

        return PostLikeInfo.builder()
                .postId(postId)
                .likeCount(likeCount)
                .isLiked(isLiked)
                .build();
    }
}
