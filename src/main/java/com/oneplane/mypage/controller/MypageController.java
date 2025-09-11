package com.oneplane.myPage.controller;

import com.oneplane.myPage.dto.CertificationTimelineDto;
import com.oneplane.myPage.service.CertificationService;
import com.oneplane.myPage.service.TravelHistoryService;
import com.oneplane.recommend.repository.RecommendRepository;
import com.oneplane.recommend.service.RecommendService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mypage/dashboard")
public class MyPageController extends BaseController{

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private TravelHistoryService travelHistoryService;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private RecommendRepository recommendRepository;

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

    // 추천 여행지 페이지
    @GetMapping("/recommend")
    public String getRecommendationHistory(@RequestParam(value = "page", defaultValue = "1") int page, HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");

        // 페이지네이션 정보를 포함한 추천 이력 조회
        Map<String, Object> result = recommendService.getRecommendHistoryWithPagination(userId, page);

        // 모델에 데이터 추가
        model.addAttribute("recommendHistory", result.get("recommendHistory"));
        model.addAttribute("currentPage", result.get("currentPage"));
        model.addAttribute("totalPages", result.get("totalPages"));
        model.addAttribute("totalCount", result.get("totalCount"));
        model.addAttribute("hasNext", result.get("hasNext"));
        model.addAttribute("hasPrevious", result.get("hasPrevious"));

        return "mypage/recommend";
    }
}
