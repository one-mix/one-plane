// 레이아웃 작성자: 오수경
// 도메인별 작성자: 공동
package com.oneplane;

import com.oneplane.post.domain.Post;
import com.oneplane.post.service.PostService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final PostService postService;

    // 메인 페이지 (지도)
    @GetMapping("/")
    public String home(Model model) {
        try {
            log.info("메인 페이지 요청 - 게시글 데이터 조회 시작");

            // 메인페이지용 게시글 데이터 조회
            List<Post> latestPosts = postService.getLatestPostsForMain();
            List<Post> popularPosts = postService.getPopularPostsForMain();
            List<Post> popularReviews = postService.getPopularReviewsForMain();

            // 모델에 데이터 추가
            model.addAttribute("latestPosts", latestPosts);
            model.addAttribute("popularPosts", popularPosts);
            model.addAttribute("popularReviews", popularReviews);

            log.info("메인 페이지 데이터 조회 완료 - 최신글: {}개, 인기글: {}개, 인기후기: {}개",
                    latestPosts.size(), popularPosts.size(), popularReviews.size());

        } catch (Exception e) {
            log.error("메인 페이지 데이터 조회 중 오류 발생", e);
            // 오류가 발생해도 페이지는 표시되도록 빈 리스트 설정
            model.addAttribute("latestPosts", List.of());
            model.addAttribute("popularPosts", List.of());
            model.addAttribute("popularReviews", List.of());
        }

        model.addAttribute("contentPage", "map/content.jsp");
        model.addAttribute("activeMenu", "home");
        return "layout/layout";
    }

    // 추천 페이지
    @GetMapping("/recommend")
    public String recommend(Model model) {
        model.addAttribute("contentPage", "recommend/main.jsp");
        model.addAttribute("activeMenu", "recommend");

        return "layout/layout";
    }

    // 추천 사용자 입력 페이지
    @GetMapping("/recommend/input")
    public String recommend_input(Model model) {
        model.addAttribute("contentPage", "recommend/input.jsp");
        model.addAttribute("activeMenu", "recommend");

        return "layout/layout";
    }


    // 추천 결과 페이지
    @GetMapping("/recommend/result")
    public String recommend_result(Model model) {
        model.addAttribute("contentPage", "recommend/result.jsp");
        model.addAttribute("activeMenu", "recommend");

        return "layout/layout";
    }

    // 게시판 페이지
    @GetMapping("/post")
    public String post(Model model) {
        model.addAttribute("contentPage", "post/main.jsp");
        model.addAttribute("activeMenu", "post");
        return "layout/layout";
    }

    // 마이페이지
    @GetMapping("/mypage")
    public String mypage(Model model) {
        model.addAttribute("contentPage", "mypage/content.jsp");
        return "layout/layout";
    }

}