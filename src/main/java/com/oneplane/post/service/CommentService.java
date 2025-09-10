package com.oneplane.post.service;

import com.oneplane.post.dao.CommentDao;
import com.oneplane.post.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private PostService postService;

    // 댓글 작성
    public Integer createComment(Comment comment) {
        // 댓글 내용 검증
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }

        // 댓글 내용 길이 검증 (100자 제한)
        if (comment.getContent().length() > 100) {
            throw new IllegalArgumentException("댓글은 100자 이내로 입력해주세요.");
        }

        // 필수 필드 검증
        if (comment.getPostId() == null) {
            throw new IllegalArgumentException("게시글 ID가 필요합니다.");
        }

        if (comment.getUserId() == null) {
            throw new IllegalArgumentException("사용자 ID가 필요합니다.");
        }

        // 댓글 내용 트림 처리
        int result = commentDao.insertComment(comment);
        if (result > 0) {
            // 게시글의 댓글 수 업데이트
            postService.updatePostCommentCount(comment.getPostId());
            return comment.getCommentId();
        } else {
            throw new RuntimeException("댓글 작성에 실패했습니다.");
        }
    }

    // 댓글 삭제
    public boolean deleteComment(Integer commentId, Integer userId) {
        if (commentId == null) {
            throw new IllegalArgumentException("댓글 ID가 필요합니다.");
        }

        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID가 필요합니다.");
        }

        // 기존 댓글 조회 및 권한 체크
        Comment existingComment = commentDao.findCommentById(commentId);
        if (existingComment == null) {
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }

        if (!existingComment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        boolean deleted = commentDao.deleteComment(commentId) > 0;
        if (deleted) {
            // 게시글의 댓글 수 업데이트
            postService.updatePostCommentCount(existingComment.getPostId());
        }
        return deleted;
    }

    // 댓글 1건 조회
    @Transactional(readOnly = true)
    public Comment getComment(Integer commentId) {
        if (commentId == null) {
            throw new IllegalArgumentException("댓글 ID가 필요합니다.");
        }

        return commentDao.findCommentById(commentId);
    }

    // 게시글의 댓글 목록 조회 (페이징)
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPostId(Integer postId, int page, int size) {
        if (postId == null) {
            throw new IllegalArgumentException("게시글 ID가 필요합니다.");
        }

        if (page < 1) {
            page = 1;
        }

        if (size < 1) {
            size = 10;
        }

        int offset = (page - 1) * size;
        return commentDao.findCommentsByPostId(postId, offset, size);
    }

    // 게시글의 총 댓글 수 조회
    @Transactional(readOnly = true)
    public int getCommentCount(Integer postId) {
        if (postId == null) {
            throw new IllegalArgumentException("게시글 ID가 필요합니다.");
        }

        return commentDao.countCommentsByPostId(postId);
    }

    /**
     * 사용자의 댓글 목록 조회 (페이징)
     * @param userId 사용자 ID
     * @param page 페이지 번호 (1부터 시작)
     * @param size 페이지 크기
     * @return 댓글 목록
     */
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByUserId(Integer userId, int page, int size) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID가 필요합니다.");
        }

        if (page < 1) {
            page = 1;
        }

        if (size < 1) {
            size = 10;
        }

        int offset = (page - 1) * size;
        return commentDao.findCommentsByUserId(userId, offset, size);
    }

    // 사용자의 총 댓글 수 조회
    @Transactional(readOnly = true)
    public int getCommentCountByUserId(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID가 필요합니다.");
        }

        return commentDao.countCommentsByUserId(userId);
    }
}
