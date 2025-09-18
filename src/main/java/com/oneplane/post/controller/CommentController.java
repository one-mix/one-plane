// 작성자: 김동현
package com.oneplane.post.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.post.domain.Comment;
import com.oneplane.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     * 작성자 : 김동현
     */
    @PostMapping("/write")
    public ResponseEntity<Map<String, Object>> writeComment(
            @RequestParam Integer postId,
            @RequestParam String content) {

        log.info("댓글 작성 요청 - postId: {}, content: {}", postId, content);

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

            // 댓글 객체 생성
            Comment comment = Comment.builder()
                    .postId(postId)
                    .userId(userId)
                    .content(content)
                    .build();

            // 댓글 작성
            Integer commentId = commentService.createComment(comment);

            response.put("success", true);
            response.put("commentId", commentId);
            response.put("message", "댓글이 작성되었습니다.");

            log.info("댓글 작성 성공 - commentId: {}, userId: {}, postId: {}", commentId, userId, postId);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("댓글 작성 실패 - 잘못된 요청: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("댓글 작성 중 오류 발생 - postId: {}", postId, e);
            response.put("success", false);
            response.put("message", "댓글 작성 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 게시글의 댓글 목록 조회
     * 작성자 : 김동현
     */
    @GetMapping("/list/{postId}")
    public ResponseEntity<Map<String, Object>> getCommentList(
            @PathVariable Integer postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("댓글 목록 조회 - postId: {}, page: {}, size: {}", postId, page, size);

        Map<String, Object> response = new HashMap<>();

        try {
            List<Comment> comments = commentService.getCommentsByPostId(postId, page, size);
            int totalCount = commentService.getCommentCount(postId);
            int totalPages = (int) Math.ceil((double) totalCount / size);

            log.info("조회된 댓글 수: {}", comments.size());
            for (Comment comment : comments) {
                log.info("댓글 ID: {}, 사용자 ID: {}, 닉네임: {}, 내용: {}",
                        comment.getCommentId(), comment.getUserId(),
                        comment.getNickname(), comment.getContent());
            }

            response.put("success", true);
            response.put("comments", comments);
            response.put("currentPage", page);
            response.put("totalPages", totalPages);
            response.put("totalCount", totalCount);
            response.put("hasNext", page < totalPages);
            response.put("hasPrevious", page > 1);

            log.info("댓글 목록 조회 완료 - postId: {}, totalCount: {}", postId, totalCount);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("댓글 목록 조회 중 오류 발생 - postId: {}", postId, e);
            response.put("success", false);
            response.put("message", "댓글 목록 조회 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 댓글 삭제
     * 작성자 : 김동현
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Integer commentId) {

        log.info("댓글 삭제 요청 - commentId: {}", commentId);

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

            // 댓글 삭제
            boolean success = commentService.deleteComment(commentId, userId);

            if (success) {
                response.put("success", true);
                response.put("message", "댓글이 삭제되었습니다.");
                log.info("댓글 삭제 성공 - commentId: {}, userId: {}", commentId, userId);
            } else {
                response.put("success", false);
                response.put("message", "댓글 삭제에 실패했습니다.");
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("댓글 삭제 실패 - 잘못된 요청: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("댓글 삭제 중 오류 발생 - commentId: {}", commentId, e);
            response.put("success", false);
            response.put("message", "댓글 삭제 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 댓글 상세 조회
     * 작성자 : 김동현
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> getComment(@PathVariable Integer commentId) {

        log.debug("댓글 상세 조회 - commentId: {}", commentId);

        Map<String, Object> response = new HashMap<>();

        try {
            Comment comment = commentService.getComment(commentId);

            if (comment != null) {
                response.put("success", true);
                response.put("comment", comment);
            } else {
                response.put("success", false);
                response.put("message", "존재하지 않는 댓글입니다.");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("댓글 상세 조회 중 오류 발생 - commentId: {}", commentId, e);
            response.put("success", false);
            response.put("message", "댓글 조회 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 게시글의 댓글 수 조회
     * 작성자 : 김동현
     */
    @GetMapping("/count/{postId}")
    public ResponseEntity<Map<String, Object>> getCommentCount(@PathVariable Integer postId) {

        log.debug("댓글 수 조회 - postId: {}", postId);

        Map<String, Object> response = new HashMap<>();

        try {
            int count = commentService.getCommentCount(postId);

            response.put("success", true);
            response.put("postId", postId);
            response.put("commentCount", count);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("댓글 수 조회 중 오류 발생 - postId: {}", postId, e);
            response.put("success", false);
            response.put("message", "댓글 수 조회 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 사용자가 작성한 댓글 목록 조회
     * 작성자 : 김동현
     */
    @GetMapping("/my-comments")
    public ResponseEntity<Map<String, Object>> getMyComments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("내 댓글 목록 조회 - page: {}, size: {}", page, size);

        Map<String, Object> response = new HashMap<>();

        try {
            if (!SecurityUtil.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(response);
            }

            Integer userId = SecurityUtil.getCurrentUserId();

            List<Comment> comments = commentService.getCommentsByUserId(userId, page, size);
            int totalCount = commentService.getCommentCountByUserId(userId);
            int totalPages = (int) Math.ceil((double) totalCount / size);

            response.put("success", true);
            response.put("comments", comments);
            response.put("currentPage", page);
            response.put("totalPages", totalPages);
            response.put("totalCount", totalCount);
            response.put("hasNext", page < totalPages);
            response.put("hasPrevious", page > 1);

            log.info("내 댓글 목록 조회 완료 - userId: {}, totalCount: {}", userId, totalCount);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("내 댓글 목록 조회 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "댓글 목록 조회 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}