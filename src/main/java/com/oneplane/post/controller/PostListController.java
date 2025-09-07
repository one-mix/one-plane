package com.oneplane.post.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.post.domain.Category;
import com.oneplane.post.domain.Post;
import com.oneplane.post.domain.PostListResponse;
import com.oneplane.post.domain.PostSearchCondition;
import com.oneplane.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                           @RequestParam(value = "country", required = false) String country,
                           @RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "size", defaultValue = "6") int size,
                           @RequestParam(value = "searchType", required = false) String searchType,
                           @RequestParam(value = "search", required = false) String searchKeyword,
                           @RequestParam(value = "sortBy", defaultValue = "latest") String sortBy,
                           Model model) {

            // 검색 조건 객체 생성
            PostSearchCondition condition = PostSearchCondition.builder()
                    .category(category != null && !category.isEmpty() ? Category.valueOf(category) : null)
                    .country(country != null && !country.isEmpty() ? country : null)
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
            model.addAttribute("selectedCountry", country);
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

            // Layout 연결
            model.addAttribute("contentPage", "post/postList.jsp");
            model.addAttribute("activeMenu", "post");


            log.info("게시글 목록 조회 완료 - 총 {}개, 현재페이지: {}/{}",
                    response.getTotalElements(), response.getCurrentPage(), response.getTotalPages());

            return "layout/layout";


    }

    // 페이지네이션 정보
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

    // 게시글 작성 페이지
    @GetMapping("/write")
    public String postWrite(Model model) {
        log.info("게시글 작성 페이지 요청");

        model.addAttribute("contentPage", "post/postWrite.jsp");
        model.addAttribute("activeMenu", "post");
        model.addAttribute("categories", Category.values());

        return "layout/layout";
    }

    // 게시글 작성
    @PostMapping("/write")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> postWriteProcess(
            @RequestParam String category,
            @RequestParam String country,
            @RequestParam String title,
            @RequestParam String content) {

        log.info("게시글 작성 처리 요청 - 카테고리: {}, 제목: {}", category, title);

        Map<String, Object> response = new HashMap<>();

        try {
            // 인증된 사용자인지 확인
            if (!SecurityUtil.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(response);
            }

            // 유효성 검사
            if (category == null || category.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "카테고리를 선택해주세요.");
                return ResponseEntity.badRequest().body(response);
            }

            if (title == null || title.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "제목을 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }

            if (title.length() > 200) {
                response.put("success", false);
                response.put("message", "제목은 200자 이하로 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }

            if (content == null || content.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "내용을 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }

            // 게시글 생성
            Post newPost = Post.builder()
                    .userId(SecurityUtil.getCurrentUserId())
                    .title(title.trim())
                    .content(content)
                    .category(Category.valueOf(category))
                    .country(country)
                    .viewCount(0)
                    .likeCount(0)
                    .commentCount(0)
                    .build();

            // 게시글 저장
            Post savedPost = postService.createPost(newPost);

            response.put("success", true);
            response.put("message", "게시글이 성공적으로 등록되었습니다.");
            response.put("postId", savedPost.getPostId());

            log.info("게시글 작성 완료 - postId: {}, 작성자: {}",
                    savedPost.getPostId(), SecurityUtil.getCurrentUserId());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("게시글 작성 실패 - 잘못된 요청: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("게시글 작성 처리 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "게시글 작성 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
