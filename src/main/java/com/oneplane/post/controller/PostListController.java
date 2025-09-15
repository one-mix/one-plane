package com.oneplane.post.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.country.domain.Country;
import com.oneplane.country.service.CountryService;
import com.oneplane.post.domain.Category;
import com.oneplane.post.domain.Post;
import com.oneplane.post.domain.PostListResponse;
import com.oneplane.post.domain.PostSearchCondition;
import com.oneplane.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    private final CountryService countryService;

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

            // 국가 목록 조회 (드롭다운용)
            List<Country> countries = countryService.getAllCountries();

            // Model에 데이터 추가
            model.addAttribute("postResponse", response);
            model.addAttribute("posts", response.getPosts());
            model.addAttribute("categories", Category.values());
            model.addAttribute("countries", countries); // 국가 목록 추가
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

        // 국가 목록 조회
        List<Country> countries = countryService.getAllCountries();

        model.addAttribute("contentPage", "post/postWrite.jsp");
        model.addAttribute("activeMenu", "post");
        model.addAttribute("categories", Category.values());
        model.addAttribute("countries", countries);

        return "layout/layout";
    }

    // 게시글 작성
    @PostMapping("/write")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> postWriteProcess(
            @RequestParam String category,
            @RequestParam Long countryId,
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
                    .countryId(countryId)
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

    /**
     * 게시글 상세보기 페이지
     */
    @GetMapping("/detail/{postId}")
    public String postDetail(@PathVariable Integer postId, Model model, HttpServletRequest request) {
        log.info("게시글 상세보기 요청 - postId: {}", postId);

        try {
            // 게시글 조회
            Post post = postService.getPostById(postId);

            if (post == null) {
                log.warn("존재하지 않는 게시글 - postId: {}", postId);
                model.addAttribute("errorMessage", "존재하지 않는 게시글입니다.");
                return "error/404";
            }

            if (post.isDeleted()) {
                log.warn("삭제된 게시글 접근 시도 - postId: {}", postId);
                model.addAttribute("errorMessage", "삭제된 게시글입니다.");
                return "error/404";
            }

            // 1시간 간격 조회수 증가
            HttpSession session = request.getSession();
            String sessionKey = "viewed_post_" + postId;
            Long lastViewTime = (Long) session.getAttribute(sessionKey);

            boolean shouldIncreaseView = false;
            long currentTime = System.currentTimeMillis();

            if (lastViewTime == null) {
                // 처음 조회하는 경우
                shouldIncreaseView = true;
                log.debug("첫 조회 - postId: {}", postId);
            } else {
                // 마지막 조회로부터 1시간 이후에만 다시 카운트
                long timeDiff = currentTime - lastViewTime;
                long cooldownTime = 60 * 60 * 1000;

                if (timeDiff > cooldownTime) {
                    shouldIncreaseView = true;
                    log.debug("1시간 경과로 조회수 증가 허용 - postId: {}, 경과시간: {}분",
                            postId, timeDiff / (60 * 1000));
                } else {
                    long remainingTime = cooldownTime - timeDiff;
                    long remainingMinutes = remainingTime / (60 * 1000);
                    log.debug("조회수 증가 대기 중 - postId: {}, 남은시간: {}분", postId, remainingMinutes);
                }
            }

            // 조회수 증가 처리
            if (shouldIncreaseView) {
                boolean viewIncreased = postService.increaseViewCount(postId);
                if (viewIncreased) {
                    post.increaseViewCount();
                    // 세션에 현재 조회 시간 기록
                    session.setAttribute(sessionKey, currentTime);
                    log.info("조회수 증가 완료 - postId: {}, 새로운 조회수: {}", postId, post.getViewCount());
                }
            }

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

            model.addAttribute("post", post);
            model.addAttribute("canEdit", canEdit);
            model.addAttribute("canDelete", canDelete);
            model.addAttribute("contentPage", "post/postDetail.jsp");
            model.addAttribute("activeMenu", "post");

            log.info("게시글 상세보기 완료 - postId: {}, 제목: {}, 조회수: {}",
                    postId, post.getTitle(), post.getViewCount());

            return "layout/layout";

        } catch (Exception e) {
            log.error("게시글 상세보기 중 오류 발생 - postId: {}", postId, e);
            model.addAttribute("errorMessage", "게시글을 불러오는데 실패했습니다.");
            return "error/500";
        }
    }
    /**
     * 게시글 수정 페이지
     */
    @GetMapping("/edit/{postId}")
    public String postEdit(@PathVariable Integer postId, Model model) {
        log.info("게시글 수정 페이지 요청 - postId: {}", postId);

        try {
            // 인증된 사용자인지 확인
            if (!SecurityUtil.isAuthenticated()) {
                return "redirect:/oauth2/authorization/kakao";
            }

            Post post = postService.getPostById(postId);

            if (post == null || post.isDeleted()) {
                model.addAttribute("errorMessage", "존재하지 않는 게시글입니다.");
                return "error/404";
            }

            // 수정 권한 확인
            Integer currentUserId = SecurityUtil.getCurrentUserId();
            if (!post.isOwner(currentUserId) && !SecurityUtil.isCurrentUserAdmin()) {
                model.addAttribute("errorMessage", "게시글 수정 권한이 없습니다.");
                return "error/403";
            }

            List<Country> countries = countryService.getAllCountries();

            // 카테고리 목록도 함께 전달
            model.addAttribute("post", post);
            model.addAttribute("categories", Category.values());
            model.addAttribute("countries", countries);
            model.addAttribute("contentPage", "post/postEdit.jsp");
            model.addAttribute("activeMenu", "post");

            log.info("게시글 수정 페이지 로드 완료 - postId: {}, 제목: {}", postId, post.getTitle());

            return "layout/layout";

        } catch (Exception e) {
            log.error("게시글 수정 페이지 로드 중 오류 발생 - postId: {}", postId, e);
            model.addAttribute("errorMessage", "게시글을 불러오는데 실패했습니다.");
            return "error/500";
        }
    }

    /**
     * 게시글 수정 처리 (POST)
     */
    @PostMapping("/edit/{postId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updatePost(
            @PathVariable Integer postId,
            @RequestParam String category,
            @RequestParam Long countryId,
            @RequestParam String title,
            @RequestParam String content) {

        log.info("게시글 수정 처리 요청 - postId: {}, 제목: {}", postId, title);

        Map<String, Object> response = new HashMap<>();

        try {
            // 인증된 사용자인지 확인
            if (!SecurityUtil.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(response);
            }

            // 기존 게시글 조회
            Post existingPost = postService.getPostById(postId);
            if (existingPost == null || existingPost.isDeleted()) {
                response.put("success", false);
                response.put("message", "존재하지 않는 게시글입니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 수정 권한 확인
            Integer currentUserId = SecurityUtil.getCurrentUserId();
            if (!existingPost.isOwner(currentUserId) && !SecurityUtil.isCurrentUserAdmin()) {
                response.put("success", false);
                response.put("message", "게시글 수정 권한이 없습니다.");
                return ResponseEntity.status(403).body(response);
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

            // 수정할 게시글 객체 생성
            Post updatePost = Post.builder()
                    .postId(postId)
                    .title(title.trim())
                    .content(content)
                    .category(Category.valueOf(category))
                    .countryId(countryId)
                    .build();

            // 게시글 수정
            Post updatedPost = postService.updatePost(postId, updatePost);

            response.put("success", true);
            response.put("message", "게시글이 성공적으로 수정되었습니다.");
            response.put("postId", updatedPost.getPostId());

            log.info("게시글 수정 완료 - postId: {}, 제목: {}, 수정자: {}",
                    updatedPost.getPostId(), updatedPost.getTitle(), currentUserId);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("게시글 수정 실패 - 잘못된 요청: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("게시글 수정 처리 중 오류 발생 - postId: {}", postId, e);
            response.put("success", false);
            response.put("message", "게시글 수정 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 게시글 삭제 처리
     */
    @PostMapping("/delete/{postId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Integer postId) {
        log.info("게시글 삭제 요청 - postId: {}", postId);

        Map<String, Object> response = new HashMap<>();

        try {
            // 인증된 사용자인지 확인
            if (!SecurityUtil.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(response);
            }

            Post post = postService.getPostById(postId);

            if (post == null || post.isDeleted()) {
                response.put("success", false);
                response.put("message", "존재하지 않는 게시글입니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 삭제 권한 확인
            Integer currentUserId = SecurityUtil.getCurrentUserId();
            if (!post.isOwner(currentUserId) && !SecurityUtil.isCurrentUserAdmin()) {
                response.put("success", false);
                response.put("message", "게시글 삭제 권한이 없습니다.");
                return ResponseEntity.status(403).body(response);
            }

            // 게시글 삭제
            boolean deleted = postService.deletePost(postId, currentUserId);

            if (deleted) {
                response.put("success", true);
                response.put("message", "게시글이 삭제되었습니다.");
                log.info("게시글 삭제 완료 - postId: {}, 삭제자: {}", postId, currentUserId);
            } else {
                response.put("success", false);
                response.put("message", "게시글 삭제에 실패했습니다.");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("게시글 삭제 중 오류 발생 - postId: {}", postId, e);
            response.put("success", false);
            response.put("message", "게시글 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

}
