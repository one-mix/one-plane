package com.oneplane.post.controller;

import com.oneplane.post.domain.Category;
import com.oneplane.post.domain.Post;
import com.oneplane.post.domain.PostListResponse;
import com.oneplane.post.domain.PostSearchCondition;
import com.oneplane.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostListController {

    private final PostService postService;

    @GetMapping("/list")
    public String postList(@RequestParam(value = "category", required = false) String category,
                           @RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "size", defaultValue = "6") int size,
                           @RequestParam(value = "searchType", required = false) String searchType,
                           @RequestParam(value = "search", required = false) String searchKeyword,
                           @RequestParam(value = "sortBy", defaultValue = "latest") String sortBy,
                           Model model) {

        log.info("게시글 목록 페이지 요청 - 카테고리: {}, 페이지: {}, 크기: {}, 검색어: {}",
                category, page, size, searchKeyword);

        try {
            // 검색 조건 객체 생성
            PostSearchCondition condition = PostSearchCondition.builder()
                    .category(category != null && !category.isEmpty() ? Category.valueOf(category) : null)
                    .searchType(searchType)
                    .searchKeyword(searchKeyword)
                    .sortBy(sortBy)
                    .page(page)
                    .size(size)
                    .build();

            // 기본값 설정
            condition.setDefaults();

            // 페이징된 게시글 목록 조회
            PostListResponse response = postService.getPostsWithPaging(condition);

            // Model에 데이터 추가
            model.addAttribute("postResponse", response);
            model.addAttribute("posts", response.getPosts());
            model.addAttribute("categories", Category.values());
            model.addAttribute("selectedCategory", category);
            model.addAttribute("currentPage", response.getCurrentPage());
            model.addAttribute("totalPages", response.getTotalPages());
            model.addAttribute("totalCount", response.getTotalElements());
            model.addAttribute("hasNext", response.isHasNext());
            model.addAttribute("hasPrevious", response.isHasPrevious());

            // 검색 파라미터 유지
            model.addAttribute("searchType", searchType);
            model.addAttribute("searchKeyword", searchKeyword);
            model.addAttribute("sortBy", sortBy);

            // 페이지네이션을 위한 추가 정보
            addPaginationInfo(model, condition, response.getTotalPages());

            log.info("게시글 목록 조회 완료 - 총 {}개, 현재페이지: {}/{}",
                    response.getTotalElements(), response.getCurrentPage(), response.getTotalPages());

            return "post/postList";

        } catch (Exception e) {
            log.error("게시글 목록 조회 중 오류 발생", e);
            model.addAttribute("errorMessage", "게시글 목록을 불러오는데 실패했습니다.");
            return "error/500";
        }
    }

    /**
     * 페이지네이션 정보 추가
     */
    private void addPaginationInfo(Model model, PostSearchCondition condition, int totalPages) {
        // 페이지 그룹 계산
        int pageGroupStart = condition.getPageGroupStart();
        int pageGroupEnd = condition.getPageGroupEnd(totalPages);

        // 이전/다음 그룹 존재 여부
        boolean hasPrevGroup = pageGroupStart > 1;
        boolean hasNextGroup = pageGroupEnd < totalPages;

        // 이전/다음 그룹 페이지 번호
        int prevGroupPage = Math.max(1, pageGroupStart - 1);
        int nextGroupPage = Math.min(totalPages, pageGroupEnd + 1);

        model.addAttribute("pageGroupStart", pageGroupStart);
        model.addAttribute("pageGroupEnd", pageGroupEnd);
        model.addAttribute("hasPrevGroup", hasPrevGroup);
        model.addAttribute("hasNextGroup", hasNextGroup);
        model.addAttribute("prevGroupPage", prevGroupPage);
        model.addAttribute("nextGroupPage", nextGroupPage);

        // URL 파라미터 생성을 위한 정보
        Map<String, String> params = new HashMap<>();
        if (condition.getCategory() != null) {
            params.put("category", condition.getCategory().name());
        }
        if (condition.getSearchType() != null && !condition.getSearchType().isEmpty()) {
            params.put("searchType", condition.getSearchType());
        }
        if (condition.getSearchKeyword() != null && !condition.getSearchKeyword().isEmpty()) {
            params.put("search", condition.getSearchKeyword());
        }
        if (condition.getSortBy() != null && !condition.getSortBy().equals("latest")) {
            params.put("sortBy", condition.getSortBy());
        }

        model.addAttribute("urlParams", params);
    }

    @GetMapping("/api/categoryCounts")
    @ResponseBody
    public Map<String, Object> getCategoryCounts() {
        log.info("카테고리별 게시글 개수 조회 API 요청");

        Map<String, Object> response = new HashMap<>();
        Map<String, Integer> categoryCounts = new HashMap<>();

        try {
            // 각 카테고리별 개수 조회
            for (Category cat : Category.values()) {
                PostSearchCondition condition = PostSearchCondition.builder()
                        .category(cat)
                        .build();
                condition.setDefaults();

                int count = postService.getPostCount(condition);
                categoryCounts.put(cat.name(), count);
            }

            response.put("success", true);
            response.put("categoryCounts", categoryCounts);

            // 전체 개수도 조회
            PostSearchCondition totalCondition = new PostSearchCondition();
            totalCondition.setDefaults();
            response.put("totalCount", postService.getPostCount(totalCondition));

            log.info("카테고리별 개수 조회 완료: {}", categoryCounts);

        } catch (Exception e) {
            log.error("카테고리별 개수 조회 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "카테고리별 개수를 조회하는데 실패했습니다.");
        }

        return response;
    }
}
