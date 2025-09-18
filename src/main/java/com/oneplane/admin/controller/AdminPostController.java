// 작성자: 김동현
package com.oneplane.admin.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.post.domain.Comment;
import com.oneplane.post.domain.Post;
import com.oneplane.post.domain.PostListResponse;
import com.oneplane.post.domain.PostSearchCondition;
import com.oneplane.post.service.CommentService;
import com.oneplane.post.service.PostLikeService;
import com.oneplane.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminPostController {

    private final PostService postService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;

    /**
     * 전체 게시글 목록
     */
    @GetMapping("/posts")
    public String adminPostList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "category", required = false) String category,
            Model model) {

        log.info("관리자 게시글 목록 조회 - page: {}, size: {}, search: {}", page, size, search);

            // 검색 조건 설정
            PostSearchCondition condition = PostSearchCondition.builder()
                    .searchType("all") // 제목+내용+작성자
                    .searchKeyword(search)
                    .sortBy("latest")
                    .page(page)
                    .size(size)
                    .build();

            if (category != null && !category.isEmpty()) {
                try {
                    condition.setCategory(com.oneplane.post.domain.Category.valueOf(category));
                } catch (IllegalArgumentException e) {
                    log.warn("잘못된 카테고리: {}", category);
                }
            }

            condition.setDefaults();

            PostListResponse response = postService.getPostsWithPaging(condition);

            // 검색 파라미터 생성
            StringBuilder searchParams = new StringBuilder();
            if (search != null && !search.trim().isEmpty()) {
                searchParams.append("&search=").append(search);
            }
            if (category != null && !category.trim().isEmpty()) {
                searchParams.append("&category=").append(category);
            }

            model.addAttribute("posts", response.getPosts());
            model.addAttribute("currentPage", response.getCurrentPage());
            model.addAttribute("totalPages", response.getTotalPages());
            model.addAttribute("totalCount", response.getTotalElements());
            model.addAttribute("hasNext", response.isHasNext());
            model.addAttribute("hasPrevious", response.isHasPrevious());
            model.addAttribute("search", search);
            model.addAttribute("category", category);
            model.addAttribute("searchParams", searchParams.toString());

            // 페이지 범위 계산
            int startRow = (page - 1) * size + 1;
            int endRow = Math.min(startRow + size - 1, (int) response.getTotalElements());
            model.addAttribute("startRow", startRow);
            model.addAttribute("endRow", endRow);

            model.addAttribute("activeMenu", "posts");
            model.addAttribute("contentPage", "postList.jsp");

            log.info("관리자 게시글 목록 조회 완료 - 총 {}개", response.getTotalElements());

            return "admin/layout/adminLayout";

    }
    /**
     * 게시글 상세보기 페이지
     */
    @GetMapping("/postDetail/{postId}")
    public String postDetail(@PathVariable Integer postId, Model model, HttpServletRequest request) {
        log.info("게시글 상세보기 요청 - postId: {}", postId);

        // 게시글 조회
        Post post = postService.getPostById(postId);

        // 수정/삭제 권한 확인
        boolean canEdit = false;
        boolean canDelete = false;

        if (SecurityUtil.isAuthenticated()) {
            Integer currentUserId = SecurityUtil.getCurrentUserId();
            boolean isOwner = post.isOwner(currentUserId);
            boolean isAdmin = SecurityUtil.isCurrentUserAdmin();

            canEdit = isOwner || isAdmin;
            canDelete = isOwner || isAdmin;

        }

        // 좋아요 상태 확인 (로그인한 사용자만)
        boolean isLiked = false;
        if (SecurityUtil.isAuthenticated()) {
            Integer currentUserId = SecurityUtil.getCurrentUserId();
            isLiked = postLikeService.isPostLikedByUser(currentUserId, postId);
            log.debug("좋아요 상태 - userId: {}, postId: {}, isLiked: {}", currentUserId, postId, isLiked);
        }

        // 모델에 데이터 추가
        model.addAttribute("post", post);
        model.addAttribute("canEdit", canEdit);
        model.addAttribute("canDelete", canDelete);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("contentPage", "postDetail.jsp");
        model.addAttribute("activeMenu", "post");
        model.addAttribute("pageTitle", post.getTitle());

        return "admin/layout/adminLayout";

    }

    /**
     * 게시물 삭제 (논리 삭제)
     */
    @PostMapping("/post/{postId}/delete")
    public String deleteUser(@PathVariable Integer postId, Integer userId) {
        log.info("사용자 삭제 - userId: {}", postId);
        postService.deletePost(postId, userId);
        return "redirect:/admin/posts";
    }

    /**
     * 인기 게시글 목록 (조회수, 좋아요수, 댓글수 기준)
     */
    @GetMapping("/posts/popularity")
    public String adminPopularPosts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "period", defaultValue = "30") int period, // 기간 필터 (일)
            Model model) {

        log.info("관리자 인기 게시글 목록 조회 - page: {}, size: {}, period: {}일", page, size, period);

        // 검색 조건 설정 (인기순 정렬)
        PostSearchCondition condition = PostSearchCondition.builder()
                .searchType("all")
                .searchKeyword(search)
                .sortBy("popularity") // 인기순 정렬
                .page(page)
                .size(size)
                .build();

        if (category != null && !category.isEmpty()) {
            try {
                condition.setCategory(com.oneplane.post.domain.Category.valueOf(category));
            } catch (IllegalArgumentException e) {
                log.warn("잘못된 카테고리: {}", category);
            }
        }

        condition.setDefaults();

        // 인기 게시글 목록 조회
        PostListResponse response = postService.getPopularPostsWithPaging(condition, period);

        // 검색 파라미터 생성
        StringBuilder searchParams = new StringBuilder();
        if (search != null && !search.trim().isEmpty()) {
            searchParams.append("&search=").append(search);
        }
        if (category != null && !category.trim().isEmpty()) {
            searchParams.append("&category=").append(category);
        }
        searchParams.append("&period=").append(period);

        model.addAttribute("posts", response.getPosts());
        model.addAttribute("currentPage", response.getCurrentPage());
        model.addAttribute("totalPages", response.getTotalPages());
        model.addAttribute("totalCount", response.getTotalElements());
        model.addAttribute("hasNext", response.isHasNext());
        model.addAttribute("hasPrevious", response.isHasPrevious());
        model.addAttribute("search", search);
        model.addAttribute("category", category);
        model.addAttribute("period", period);
        model.addAttribute("searchParams", searchParams.toString());

        // 페이지 범위 계산
        int startRow = (page - 1) * size + 1;
        int endRow = Math.min(startRow + size - 1, (int) response.getTotalElements());
        model.addAttribute("startRow", startRow);
        model.addAttribute("endRow", endRow);

        model.addAttribute("activeMenu", "post-popularity");
        model.addAttribute("contentPage", "postPopularity.jsp");

        log.info("관리자 인기 게시글 목록 조회 완료 - 총 {}개", response.getTotalElements());

        return "admin/layout/adminLayout";
    }

    /**
     * 전체 댓글 목록 페이지
     */
    @GetMapping("/comments")
    public String adminCommentList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "search", required = false) String search,
            Model model) {

        log.info("관리자 댓글 목록 조회 - page: {}, size: {}, search: {}", page, size, search);

        try {
            // 댓글 목록 조회
            List<Comment> comments = commentService.getAllComments(page, size, search);
            int totalCount = commentService.getAllCommentCount(search);
            int totalPages = (int) Math.ceil((double) totalCount / size);

            // 검색 파라미터 생성
            StringBuilder searchParams = new StringBuilder();
            if (search != null && !search.trim().isEmpty()) {
                searchParams.append("&search=").append(search);
            }

            model.addAttribute("comments", comments);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("hasNext", page < totalPages);
            model.addAttribute("hasPrevious", page > 1);
            model.addAttribute("search", search);
            model.addAttribute("searchParams", searchParams.toString());

            // 페이지 범위 계산
            int startRow = (page - 1) * size + 1;
            int endRow = Math.min(startRow + size - 1, totalCount);
            model.addAttribute("startRow", startRow);
            model.addAttribute("endRow", endRow);

            model.addAttribute("activeMenu", "comments");
            model.addAttribute("contentPage", "commentList.jsp");

            log.info("관리자 댓글 목록 조회 완료 - 총 {}개", totalCount);

            return "admin/layout/adminLayout";

        } catch (Exception e) {
            log.error("관리자 댓글 목록 조회 중 오류 발생", e);
            model.addAttribute("errorMessage", "댓글 목록을 불러오는데 실패했습니다.");
            return "admin/layout/adminLayout";
        }
    }
}