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
    @GetMapping("/myPost")
    public String myPostList(
            @RequestParam(value = "category", required = false) String category,
            Model model) {

        Integer currentUserId;
        try {
            currentUserId = SecurityUtil.getCurrentUserId();
        } catch (Exception e) {
            log.error("사용자 ID 조회 실패: {}", e.getMessage());
            return "redirect:/oauth2/authorization/kakao";
        }

        List<Post> posts = myPostService.getMyPostsByCategory(currentUserId, category);
        model.addAttribute("posts", posts);
        model.addAttribute("totalCount", posts.size());
        model.addAttribute("selectedCategory", category);

        model.addAttribute("contentPage", "mypage/myPost/myPost.jsp");
        model.addAttribute("activeMenu", "myPost");
        model.addAttribute("showSidebar", true);

        log.info("내 게시글 목록 조회 완료 - 총 {}개", posts.size());
        return "layout/layout";
    }

    /**
     * 팔로워가 작성한 게시글 목록 페이지
     * URL: /myPost/followerPost
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

    @GetMapping("/list")
    public String listPosts(
            @RequestParam(name = "tab", defaultValue = "my") String tab,
            @RequestParam(name = "category", required = false) String category,
            Model model) {

        Integer userId = SecurityUtil.getCurrentUserId();
        List<Post> posts;

        if ("follower".equals(tab)) {
            posts = myPostService.getFollowersPostsByCategory(userId, category);
        } else {
            posts = myPostService.getMyPostsByCategory(userId, category);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("totalCount", posts.size());
        model.addAttribute("tab", tab);
        model.addAttribute("selectedCategory", category);
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