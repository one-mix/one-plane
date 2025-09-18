package com.oneplane.mypost.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.post.domain.Post;
import com.oneplane.mypost.service.MyPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * 사용자 개인 및 팔로워 게시글 조회 컨트롤러
 * 내 게시글 및 팔로워 게시글 목록을 카테고리별로 표시
 *
 * 작성자: 허겸
 */
@Controller
@RequestMapping("/myPost") // 클래스 레벨 매핑
@RequiredArgsConstructor
@Slf4j
public class MyPostController {

    private final MyPostService myPostService; // 서비스 레이어 의존성 주입

    /**
     * 로그인한 사용자의 게시글 목록 조회
     * URL: /myPost/myPost
     *
     * @param category 선택된 게시글 카테고리 (옵션)
     * @param model    뷰에 전달할 데이터 모델
     * @return 레이아웃 템플릿 뷰
     */
    @GetMapping("/myPost")
    public String myPostList(
            @RequestParam(value = "category", required = false) String category,
            Model model) {
        Integer currentUserId;
        try {
            // 현재 로그인된 사용자 ID 조회
            currentUserId = SecurityUtil.getCurrentUserId();
        } catch (Exception e) {
            log.error("사용자 ID 조회 실패: {}", e.getMessage());
            // 인증되지 않은 경우 OAuth 로그인 페이지로 리다이렉트
            return "redirect:/oauth2/authorization/kakao";
        }

        // 서비스 호출로 게시글 목록 조회
        List<Post> posts = myPostService.getMyPostsByCategory(currentUserId, category);
        // 뷰 모델 설정
        model.addAttribute("posts", posts);                      // 게시글 리스트
        model.addAttribute("totalCount", posts.size());         // 전체 개수
        model.addAttribute("selectedCategory", category);       // 선택된 카테고리
        model.addAttribute("contentPage", "mypage/myPost/myPost.jsp");
        model.addAttribute("activeMenu", "myPost");            // 사이드바 메뉴 활성화
        model.addAttribute("showSidebar", true);                // 사이드바 표시 여부
        log.info("내 게시글 목록 조회 완료 - 총 {}개", posts.size());
        return "layout/layout";
    }

    /**
     * 팔로워가 작성한 게시글 목록 조회
     * URL: /myPost/followerPost
     *
     * @param category 선택된 게시글 카테고리 (옵션)
     * @param model    뷰에 전달할 데이터 모델
     * @return 레이아웃 템플릿 뷰
     */
    @GetMapping("/followerPost")
    public String followerPostList(
            @RequestParam(value = "category", required = false) String category,
            Model model) {
        Integer currentUserId;
        try {
            currentUserId = SecurityUtil.getCurrentUserId();
        } catch (Exception e) {
            log.error("사용자 ID 조회 실패: {}", e.getMessage());
            return "redirect:/oauth2/authorization/kakao";
        }

        // 팔로워 게시글 조회
        List<Post> posts = myPostService.getFollowersPostsByCategory(currentUserId, category);
        model.addAttribute("posts", posts);
        model.addAttribute("totalCount", posts.size());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("contentPage", "mypage/myPost/followerPost.jsp");
        model.addAttribute("activeMenu", "myPost");
        model.addAttribute("showSidebar", true);
        log.info("팔로워 게시글 목록 조회 완료 - 총 {}개", posts.size());
        return "layout/layout";
    }

    /**
     * 통합 게시글 탭별 목록 조회
     * URL: /myPost/list
     * tab 파라미터 값에 따라 내 게시글 또는 팔로워 게시글 표시
     *
     * @param tab      표시할 탭 ("my" 또는 "follower")
     * @param category 선택된 카테고리
     * @param model    뷰에 전달할 데이터 모델
     * @return 레이아웃 템플릿 뷰
     */
    @GetMapping("/list")
    public String listPosts(
            @RequestParam(name = "tab", defaultValue = "my") String tab,
            @RequestParam(name = "category", required = false) String category,
            Model model) {
        Integer userId = SecurityUtil.getCurrentUserId();
        // 탭에 따라 서비스 호출 분기
        List<Post> posts;
        if ("follower".equals(tab)) {
            posts = myPostService.getFollowersPostsByCategory(userId, category);
        } else {
            posts = myPostService.getMyPostsByCategory(userId, category);
        }

        // 뷰 모델 설정
        model.addAttribute("posts", posts);
        model.addAttribute("totalCount", posts.size());
        model.addAttribute("tab", tab);                       // 현재 탭
        model.addAttribute("selectedCategory", category);
        // JSP 경로 분기 설정
        model.addAttribute("contentPage",
                tab.equals("follower")
                        ? "mypage/myPost/followerPost.jsp"
                        : "mypage/myPost/myPost.jsp");
        model.addAttribute("activeMenu", "myPost");
        model.addAttribute("showSidebar", true);
        log.info("{} 게시글 목록 조회 완료 - 총 {}개", tab, posts.size());
        return "layout/layout";
    }
}