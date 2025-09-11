package com.oneplane.myPage.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/section")
public class SectionController extends BaseController {

    @GetMapping("/recommend")
    public String showRecommend(HttpSession session, Model model) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("contentPage", "mypage/recommend.jsp");
        model.addAttribute("activeMenu", "recommend");  // 추가
        model.addAttribute("showSidebar", true);  // 추가
        model.addAttribute("pageTitle", "여행 추천");  // 추가
        return "layout/layout";  // 수정 (빈 문자열 -> layout/layout)
    }
}
