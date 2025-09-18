// 작성자: 김동현
package com.oneplane.post.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.post.domain.PostLike;
import com.oneplane.post.domain.PostLikeInfo;
import com.oneplane.post.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post/like")
@RequiredArgsConstructor
@Slf4j
public class PostLikeController {

    private final PostLikeService postLikeService;

    /**
     * 좋아요 토글 (좋아요/취소)
     * 작성자 : 김동현
     */
    @PostMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> togglePostLike(@PathVariable Integer postId) {
        log.info("좋아요 토글 요청 - postId: {}", postId);

        Map<String, Object> response = new HashMap<>();

        try {
            // 로그인 확인
            if (!SecurityUtil.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                response.put("needLogin", true);
                return ResponseEntity.status(401).body(response);
            }

            Integer userId = SecurityUtil.getCurrentUserId();

            // 좋아요 토글 처리
            boolean isLiked = postLikeService.togglePostLike(userId, postId);

            // 최신 좋아요 수 조회
            int likeCount = postLikeService.getPostLikeCount(postId);

            response.put("success", true);
            response.put("isLiked", isLiked);
            response.put("likeCount", likeCount);
            response.put("message", isLiked ? "좋아요를 눌렀습니다." : "좋아요를 취소했습니다.");

            log.info("좋아요 토글 성공 - userId: {}, postId: {}, isLiked: {}, likeCount: {}",
                    userId, postId, isLiked, likeCount);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("좋아요 토글 실패 - 잘못된 요청: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("좋아요 토글 중 오류 발생 - postId: {}", postId, e);
            response.put("success", false);
            response.put("message", "좋아요 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 게시글의 좋아요 상태 및 수 조회
     * 작성자 : 김동현
     */
    @GetMapping("/status/{postId}")
    public ResponseEntity<Map<String, Object>> getPostLikeStatus(@PathVariable Integer postId) {
        log.debug("좋아요 상태 조회 - postId: {}", postId);

        Map<String, Object> response = new HashMap<>();

        try {
            Integer userId = SecurityUtil.isAuthenticated() ? SecurityUtil.getCurrentUserId() : null;

            // 좋아요 정보 조회
            PostLikeInfo likeInfo = postLikeService.getPostLikeInfo(postId, userId);

            response.put("success", true);
            response.put("postId", postId);
            response.put("likeCount", likeInfo.getLikeCount());
            response.put("isLiked", likeInfo.isLiked());
            response.put("isLoggedIn", userId != null);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("좋아요 상태 조회 중 오류 발생 - postId: {}", postId, e);
            response.put("success", false);
            response.put("message", "좋아요 상태 조회 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 게시글의 좋아요 목록 조회 (최근 순)
     * 작성자 : 김동현
     */
    @GetMapping("/list/{postId}")
    public ResponseEntity<Map<String, Object>> getPostLikeList(
            @PathVariable Integer postId,
            @RequestParam(defaultValue = "10") int limit) {

        log.debug("좋아요 목록 조회 - postId: {}, limit: {}", postId, limit);

        Map<String, Object> response = new HashMap<>();

        try {
            List<PostLike> likes = postLikeService.getPostLikes(postId, limit);
            int totalCount = postLikeService.getPostLikeCount(postId);

            response.put("success", true);
            response.put("postId", postId);
            response.put("likes", likes);
            response.put("totalCount", totalCount);
            response.put("displayCount", likes.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("좋아요 목록 조회 중 오류 발생 - postId: {}", postId, e);
            response.put("success", false);
            response.put("message", "좋아요 목록 조회 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 사용자가 좋아요한 게시글 목록 조회
     * 작성자 : 김동현
     */
    @GetMapping("/my-likes")
    public ResponseEntity<Map<String, Object>> getMyLikedPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("내 좋아요 목록 조회 - page: {}, size: {}", page, size);

        Map<String, Object> response = new HashMap<>();

        try {
            // 로그인 확인
            if (!SecurityUtil.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(response);
            }

            Integer userId = SecurityUtil.getCurrentUserId();

            List<PostLike> likedPosts = postLikeService.getUserLikedPosts(userId, page, size);
            int totalCount = postLikeService.getUserLikedPostCount(userId);
            int totalPages = (int) Math.ceil((double) totalCount / size);

            response.put("success", true);
            response.put("likedPosts", likedPosts);
            response.put("currentPage", page);
            response.put("totalPages", totalPages);
            response.put("totalCount", totalCount);
            response.put("hasNext", page < totalPages);
            response.put("hasPrevious", page > 1);

            log.info("내 좋아요 목록 조회 완료 - userId: {}, totalCount: {}", userId, totalCount);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("내 좋아요 목록 조회 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "좋아요 목록 조회 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 여러 게시글의 좋아요 상태 일괄 조회 (목록 페이지용)
     * 작성자 : 김동현
     */
    @PostMapping("/batch-status")
    public ResponseEntity<Map<String, Object>> getBatchLikeStatus(
            @RequestBody Map<String, List<Integer>> request) {

        List<Integer> postIds = request.get("postIds");
        log.debug("좋아요 상태 일괄 조회 - postIds: {}", postIds);

        Map<String, Object> response = new HashMap<>();

        try {
            if (postIds == null || postIds.isEmpty()) {
                response.put("success", false);
                response.put("message", "게시글 ID 목록이 필요합니다.");
                return ResponseEntity.badRequest().body(response);
            }

            Integer userId = SecurityUtil.isAuthenticated() ? SecurityUtil.getCurrentUserId() : null;
            Map<Integer, PostLikeInfo> likeInfoMap = new HashMap<>();

            for (Integer postId : postIds) {
                PostLikeInfo likeInfo = postLikeService.getPostLikeInfo(postId, userId);
                likeInfoMap.put(postId, likeInfo);
            }

            response.put("success", true);
            response.put("likeInfoMap", likeInfoMap);
            response.put("isLoggedIn", userId != null);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("좋아요 상태 일괄 조회 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "좋아요 상태 조회 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}