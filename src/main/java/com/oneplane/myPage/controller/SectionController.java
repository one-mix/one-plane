package com.oneplane.myPage.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 섹션 관련 컨트롤러
 * 여행 추천 섹션 등 페이지 전환을 처리
 *
 * @author 허겸
 */
@Controller
@RequestMapping("/section")
public class SectionController extends BaseController {

    /**
     * 여행 추천 페이지를 표시
     * 로그인 확인 후 뷰로 이동
     *
     * @param session HTTP 세션 (사용자 인증 확인용)
     * @param model 뷰에 전달할 데이터 모델
     * @return 추천 페이지 뷰 또는 로그인 페이지로 리다이렉트
     */
    @GetMapping("/recommend")
    public String showRecommend(HttpSession session, Model model) {
        // 사용자 로그인 여부 확인
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            // 로그인되지 않은 경우 로그인 페이지로 이동
            return "redirect:/login";
        }

        // 뷰에 사용할 속성 설정
        model.addAttribute("contentPage", "mypage/recommend.jsp"); // 포함할 JSP 경로
        model.addAttribute("activeMenu", "recommend");         // 사이드바 활성 메뉴
        model.addAttribute("showSidebar", true);                // 사이드바 표시 여부
        model.addAttribute("pageTitle", "여행 추천");          // 페이지 제목

        // 레이아웃 템플릿 반환
        return "layout/layout";
    }
}