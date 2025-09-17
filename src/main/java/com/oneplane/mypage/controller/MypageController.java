package com.oneplane.myPage.controller;

import com.oneplane.myPage.dto.CertificationTimelineDto;
import com.oneplane.myPage.service.CertificationService;
import com.oneplane.myPage.service.TravelHistoryService;
import com.oneplane.recommend.dto.RecommendResultDTO;
import com.oneplane.recommend.repository.RecommendRepository;
import com.oneplane.recommend.service.RecommendService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mypage/dashboard")
public class MyPageController extends BaseController {

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

        // --- 기존 마이페이지 통계 및 타임라인 ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");
        model.addAttribute("userRole", role);

        int countryCount = certificationService.getVisitedCountryCount(userId);
        int totalDistance = certificationService.getVisitedTotalDistance(userId);
        int photoCount = travelHistoryService.getUploadedPhotoCount(userId);

        List<CertificationTimelineDto> timeline = certificationService.getTimelineData(userId);

        // **새로 추가: 최신 사이클만 표시하는 로직**
        int maxDistance = 30000;

        if (!timeline.isEmpty()) {
            // 1. 각 항목의 사이클 번호 계산
            int maxCycle = 0;
            for (CertificationTimelineDto item : timeline) {
                int cycle = item.getCumulativeDistance() / maxDistance;
                maxCycle = Math.max(maxCycle, cycle);
            }

            // 2. 최신 사이클에 속하는 항목들만 필터링
            final int currentCycle = maxCycle;
            timeline = timeline.stream()
                    .filter(item -> item.getCumulativeDistance() / maxDistance == currentCycle)
                    .collect(Collectors.toList());

            // 3. 남은 항목들에 wrap-around 적용
            for (CertificationTimelineDto item : timeline) {
                int wrappedDistance = item.getCumulativeDistance() % maxDistance;
                item.setCumulativeDistance(wrappedDistance);
            }

            // 4. 현재 사이클 정보를 뷰에 전달 (선택사항)
            model.addAttribute("currentCycle", currentCycle);
        }

        model.addAttribute("timeline", timeline);
        model.addAttribute("maxDistance", maxDistance);
        model.addAttribute("countryCount", countryCount);
        model.addAttribute("totalDistance", totalDistance);
        model.addAttribute("photoCount", photoCount);
        // --- 통계 및 타임라인 END ---

        // --- 새로 추가: 추천 여행지 가져와 뷰용 DTO로 변환 ---
        List<RecommendResultDTO> recs = recommendService.getRecommendHistory(userId.intValue(), 1);
        List<PlaceView> recommendedPlaces = recs.stream()
                .map(dto -> new PlaceView(
                        dto.getCountryImg(),        // 썸네일 URL
                        dto.getCountryNameKo(),     // 국가명
                        dto.getCity(),              // 도시명
                        "여행 경보: " + dto.getAlertLevel()  // 간단 설명
                ))
                .collect(Collectors.toList());
        model.addAttribute("recommendedPlaces", recommendedPlaces);
        // --- 추천 여행지 END ---

        model.addAttribute("contentPage", "mypage/content.jsp");
        model.addAttribute("activeMenu", "dashboard");
        model.addAttribute("showSidebar", true);
        model.addAttribute("pageTitle", "마이페이지");
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

    // 뷰 전용 간단 DTO
    public static class PlaceView {
        private final String thumbUrl;
        private final String country;
        private final String name;
        private final String description;

        public PlaceView(String thumbUrl, String country, String name, String description) {
            this.thumbUrl = thumbUrl;
            this.country = country;
            this.name = name;
            this.description = description;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }
        public String getCountry() {
            return country;
        }
        public String getName() {
            return name;
        }
        public String getDescription() {
            return description;
        }
    }

    /**
     * 추천 이력 페이지 조회 (페이징 지원)
     */
    @GetMapping("/recommend")
    public String getRecommendationHistory(
            @RequestParam(value = "page", defaultValue = "1") int page,
            HttpSession session,
            Model model) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // 로그인 안 된 경우 처리
        }

        Map<String, Object> result = recommendService.getRecommendHistoryWithPagination(userId, page);

        model.addAttribute("recommendHistory", result.get("recommendHistory"));
        model.addAttribute("currentPage", result.get("currentPage"));
        model.addAttribute("totalPages", result.get("totalPages"));
        model.addAttribute("totalCount", result.get("totalCount"));
        model.addAttribute("hasNext", result.get("hasNext"));
        model.addAttribute("hasPrevious", result.get("hasPrevious"));
        model.addAttribute("contentPage", "mypage/recommend.jsp");

        return "layout/layout";
    }
}
