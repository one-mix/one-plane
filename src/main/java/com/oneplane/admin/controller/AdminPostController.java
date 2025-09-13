package com.oneplane.admin.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.post.domain.Post;
import com.oneplane.post.domain.PostListResponse;
import com.oneplane.post.domain.PostSearchCondition;
import com.oneplane.post.service.PostLikeService;
import com.oneplane.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminPostController {

    private final PostService postService;
    private final PostLikeService postLikeService;

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

            // 게시글 목록 조회
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

}