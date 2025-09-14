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

@Controller
@RequestMapping("/myPost")  // 클래스-level 매핑을 /myPost로 수정
@RequiredArgsConstructor
@Slf4j
public class MyPostController {

    private final MyPostService myPostService;

    /**
     * 내 게시글 목록 페이지
     * URL: /myPost/myBoard
     */
    @GetMapping("/myPost")  // 메서드-level 매핑을 /myBoard로 수정
    public String myPostList(
            @RequestParam(value = "category", required = false) String category,  // ← 추가
            Model model) {

        Integer currentUserId;
        try {
            currentUserId = SecurityUtil.getCurrentUserId();
        } catch (Exception e) {
            log.error("사용자 ID 조회 실패: {}", e.getMessage());
            return "redirect:/oauth2/authorization/kakao";
        }

        try {
            // 내 게시글 목록 조회
            List<Post> posts = myPostService.getMyPostsByCategory(currentUserId, category);


            // Model에 데이터 추가
            model.addAttribute("posts", posts);
            model.addAttribute("totalCount", posts.size());
            model.addAttribute("selectedCategory", category);  // ← 추가 (JSP에서 active 탭 표시용)

            // Layout 연결: views/mypage/myPost/myPost.jsp
            model.addAttribute("contentPage", "mypage/myPost/myPost.jsp");
            model.addAttribute("activeMenu", "myPost");
            model.addAttribute("showSidebar", true);

            log.info("내 게시글 목록 조회 완료 - 총 {}개", posts.size());

        } catch (Exception e) {
            log.error("내 게시글 목록 조회 중 오류 발생", e);
            model.addAttribute("errorMessage", "게시글을 불러오는데 실패했습니다.");
            model.addAttribute("contentPage", "error/500.jsp");
        }

        return "layout/layout";
    }
}