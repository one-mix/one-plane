package com.oneplane.myPage.controller;

import com.oneplane.myPage.dto.CertificationTimelineDto;
import com.oneplane.myPage.service.CertificationService;
import com.oneplane.myPage.service.TravelHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mypage/dashboard")
public class MyPageController extends BaseController{

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private TravelHistoryService travelHistoryService;

    @GetMapping
    public String mypage(Model model, HttpSession session) {
        Long userId = getUserIdFromSession(session);

        if (userId == null) {
            return "redirect:/login";
        }

        // 통계 데이터 조회
        int countryCount   = certificationService.getVisitedCountryCount(userId);
        int totalDistance  = certificationService.getVisitedTotalDistance(userId);
        int photoCount     = travelHistoryService.getUploadedPhotoCount(userId);

        // DTO 리스트 조회
        List<CertificationTimelineDto> timeline = certificationService.getTimelineData(userId);

        // 로그로 DTO 내용 출력
        System.out.println("=== Timeline Data Start ===");
        for (CertificationTimelineDto dto : timeline) {
            System.out.printf(
                    "Date: %s, FlagUrl: %s, Distance: %d, Cumulative: %d%n",
                    dto.getCertificationDate(),
                    dto.getCountryFlagUrl(),
                    dto.getDistance(),
                    dto.getCumulativeDistance()
            );
        }

        System.out.println("=== Timeline Data End ===");

        // 모델에 데이터 추가
//        List<CertificationTimelineDto> timeline = certificationService.getTimelineData(userId);
        model.addAttribute("timeline", timeline);
        model.addAttribute("maxDistance", 30000);
        model.addAttribute("countryCount",  countryCount);
        model.addAttribute("totalDistance", totalDistance);
        model.addAttribute("photoCount",    photoCount);
        model.addAttribute("contentPage",   "mypage/content.jsp");
        model.addAttribute("activeMenu", "dashboard");  // 수정 dashboard 대신 travelHistory
        model.addAttribute("showSidebar", true);  // 추가
        model.addAttribute("pageTitle", "마이페이지");  // 추가


        return "layout/layout";
    }

    public Long getUserIdFromSession(HttpSession session) {
        Object obj = session.getAttribute("userId");
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        if (obj instanceof String) {
            return Long.valueOf((String) obj);
        }
        return null;
    }
}
