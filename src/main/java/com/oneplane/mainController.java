package com.oneplane;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // 메인 페이지 (지도)
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("contentPage", "map/main.jsp");
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

    // 추천 페이지
    @GetMapping("/recommend/loading")
    public String recommend_loading(Model model) {
        model.addAttribute("contentPage", "recommend/loading.jsp");
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
        model.addAttribute("contentPage", "mypage/main.jsp");
        return "layout/layout";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("contentPage", "login/main.jsp");
        return "layout/layout";
    }
}