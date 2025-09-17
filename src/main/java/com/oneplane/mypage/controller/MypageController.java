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

/**
 * 마이페이지 메인 대시보드 컨트롤러
 * 사용자의 여행 통계, 타임라인, 추천 여행지 등 종합적인 정보를 제공
 *
 * 작성자: 허겸
 * 버전: 1.0
 * 작성일: 2025
 */
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

    /**
     * 마이페이지 대시보드 메인 화면
     * 여행 통계, 타임라인, 추천 여행지 등을 종합하여 표시
     *
     * @param model 뷰에 전달할 데이터 모델
     * @param session HTTP 세션 (사용자 인증 확인)
     * @return 대시보드 페이지 또는 로그인 리다이렉트
     */
    @GetMapping
    public String mypage(Model model, HttpSession session) {
        // 사용자 로그인 확인
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        // --- 사용자 권한 정보 조회 ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");
        model.addAttribute("userRole", role);

        // --- 기본 여행 통계 정보 수집 ---
        int countryCount = certificationService.getVisitedCountryCount(userId);
        int totalDistance = certificationService.getVisitedTotalDistance(userId);
        int photoCount = travelHistoryService.getUploadedPhotoCount(userId);

        List<CertificationTimelineDto> timeline = certificationService.getTimelineData(userId);

        // --- 타임라인 사이클 처리 로직 ---
        // 최대 거리 30000km를 한 사이클로 설정하여 지구본 형태의 원형 타임라인 구현
        int maxDistance = 30000;

        if (!timeline.isEmpty()) {
            // 1. 각 여행 항목의 사이클 번호 계산 (누적 거리 / 30000km)
            int maxCycle = 0;
            for (CertificationTimelineDto item : timeline) {
                int cycle = item.getCumulativeDistance() / maxDistance;
                maxCycle = Math.max(maxCycle, cycle);
            }

            // 2. 최신 사이클에 속하는 항목들만 필터링하여 표시
            final int currentCycle = maxCycle;
            timeline = timeline.stream()
                    .filter(item -> item.getCumulativeDistance() / maxDistance == currentCycle)
                    .collect(Collectors.toList());

            // 3. wrap-around 적용: 사이클 내에서의 상대적 위치로 변환
            for (CertificationTimelineDto item : timeline) {
                int wrappedDistance = item.getCumulativeDistance() % maxDistance;
                item.setCumulativeDistance(wrappedDistance);
            }

            // 4. 현재 사이클 번호를 뷰에 전달 (UI에서 사이클 표시용)
            model.addAttribute("currentCycle", currentCycle);
        }

        // 타임라인 및 통계 데이터를 모델에 추가
        model.addAttribute("timeline", timeline);
        model.addAttribute("maxDistance", maxDistance);
        model.addAttribute("countryCount", countryCount);
        model.addAttribute("totalDistance", totalDistance);
        model.addAttribute("photoCount", photoCount);

        // --- 추천 여행지 정보 처리 ---
        // 사용자 맞춤 추천 여행지를 조회하고 뷰용 DTO로 변환
        List<RecommendResultDTO> recs = recommendService.getRecommendHistory(userId.intValue(), 1);
        List<PlaceView> recommendedPlaces = recs.stream()
                .map(dto -> new PlaceView(
                        dto.getCountryImg(),        // 국가 이미지 썸네일
                        dto.getCountryNameKo(),     // 한글 국가명
                        dto.getCity(),              // 추천 도시명
                        "여행 경보: " + dto.getAlertLevel()  // 안전도 정보
                ))
                .collect(Collectors.toList());
        model.addAttribute("recommendedPlaces", recommendedPlaces);

        // --- 페이지 레이아웃 설정 ---
        model.addAttribute("contentPage", "mypage/content.jsp");
        model.addAttribute("activeMenu", "dashboard");
        model.addAttribute("showSidebar", true);
        model.addAttribute("pageTitle", "마이페이지");

        return "layout/layout";
    }

    /**
     * 세션에서 userId를 안전하게 추출하는 헬퍼 메서드
     * Number 타입과 String 타입 모두 처리
     *
     * @param session HTTP 세션 객체
     * @return Long 타입의 userId 또는 null
     */
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

    /**
     * 추천 여행지 표시용 간단한 뷰 DTO 클래스
     * 대시보드에서 카드 형태로 표시할 최소한의 정보만 포함
     */
    public static class PlaceView {
        private final String thumbUrl;      // 썸네일 이미지 URL
        private final String country;       // 국가명
        private final String name;          // 도시명
        private final String description;   // 간단한 설명 (안전도 등)

        public PlaceView(String thumbUrl, String country, String name, String description) {
            this.thumbUrl = thumbUrl;
            this.country = country;
            this.name = name;
            this.description = description;
        }

        // Getter 메서드들
        public String getThumbUrl() { return thumbUrl; }
        public String getCountry() { return country; }
        public String getName() { return name; }
        public String getDescription() { return description; }
    }

    /**
     * 사용자의 추천 이력을 페이징하여 조회하는 메서드
     * 과거 추천받은 여행지들을 페이지 단위로 나누어 표시
     *
     * @param page 조회할 페이지 번호 (기본값: 1)
     * @param session HTTP 세션 (사용자 인증)
     * @param model 뷰 데이터 모델
     * @return 추천 이력 페이지 또는 로그인 리다이렉트
     */
    @GetMapping("/recommend")
    public String getRecommendationHistory(
            @RequestParam(value = "page", defaultValue = "1") int page,
            HttpSession session,
            Model model) {

        // 세션에서 사용자 ID 확인 (Integer 타입으로 저장된 경우)
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        // 페이징된 추천 이력 조회
        Map<String, Object> result = recommendService.getRecommendHistoryWithPagination(userId, page);

        // 페이징 정보 및 추천 이력을 모델에 추가
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