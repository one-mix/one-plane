package com.oneplane;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // 메인 페이지 (지도)
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("contentPage", "map/content.jsp");
        model.addAttribute("activeMenu", "home");
        return "layout/layout";
    }

    // 추천 페이지
    @GetMapping("/recommend")
    public String recommend(Model model) {
        model.addAttribute("contentPage", "recommend/content.jsp");
        model.addAttribute("activeMenu", "recommend");

        return "layout/layout";
    }

    // 게시판 페이지
    @GetMapping("/board")
    public String board(Model model) {
        model.addAttribute("contentPage", "board/content.jsp");
        model.addAttribute("activeMenu", "board");
        return "layout/layout";
    }

    // 마이페이지
    @GetMapping("/mypage")
    public String mypage(Model model) {
        model.addAttribute("contentPage", "mypage/content.jsp");
        return "layout/layout";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("contentPage", "login/content.jsp");
        return "layout/layout";
    }
}
