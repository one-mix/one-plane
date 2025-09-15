package com.oneplane.recommend.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.recommend.dto.RecommendDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;
import com.oneplane.recommend.service.RecommendService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping({"/api/recommend", "/recommend"})
public class RecommendController {

    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    @GetMapping("/check-login")
    public ResponseEntity<Map<String, Object>> checkLogin(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        // 세션에 로그인 정보가 있으면 로그인된 상태
        if (session.getAttribute("user") != null) {
            response.put("loggedIn", true);
        } else {
            response.put("loggedIn", false);
        }

        return ResponseEntity.ok(response);
    }

    /** 추천 메인 */
    @GetMapping("/agreement")
    @ResponseBody
    public String getAgreement() {
        Integer userId = SecurityUtil.getCurrentUserId();
        return recommendService.getLatestAgreement(userId); // Y / N / null
    }

    /** 동의 저장 */
    @PostMapping("/agreement")
    @ResponseBody
    public String saveAgreement() {
        Integer userId = SecurityUtil.getCurrentUserId();
        recommendService.saveAgreement(userId);
        return "동의 완료";
    }

    /** 여행 목적 + 동행자 저장 */
    @PostMapping("/input")
    @ResponseBody

    public String saveInput(@RequestBody RecommendDTO dto) {
        dto.setUserId(SecurityUtil.getCurrentUserId());
        recommendService.insertInput(dto);
        return "여행 목적/동행자 저장 완료";
    }

    @GetMapping("/latest-input")
    @ResponseBody
    public RecommendDTO checkLatestInput() {
        Integer userId = SecurityUtil.getCurrentUserId();
        return recommendService.getLatestInput(userId);
    }

    @GetMapping("/loading")
    public String recommendLoading(RedirectAttributes redirectAttributes) {
        Integer userId = SecurityUtil.getCurrentUserId();
        RecommendDTO latest = recommendService.getLatestInput(userId);

        List<RecommendResultDTO> recs = recommendService.callFlaskRecommend(
                userId,
                latest.getTravelPurpose(),
                latest.getCompanion()
        );

        if (recs.size() > 3) {
            recs = recs.subList(0, 3); // 상위 3개만
        }

        redirectAttributes.addFlashAttribute("user", SecurityUtil.getCurrentUserDetails());
        redirectAttributes.addFlashAttribute("recommendations", recs);

        return "redirect:/recommend/result";
    }

    // 국가 도시 선택 저장
    @PostMapping("/saveCountry")
    public ResponseEntity<?> saveCountry(@RequestBody Map<String, String> request,
                                         HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        String countryIso3 = request.get("countryIso3");
        String city = request.get("city");

        // ISO3 코드로 countryId 조회 후 저장
        // 디버깅
        System.out.printf("saveCountry 호출: iso3=%s, city=%s%n", countryIso3, city);
        Integer recommendId = recommendService.saveSelectedCountry(userId, countryIso3, city);

        return ResponseEntity.ok(Map.of("recommendId", recommendId, "message", "국가/도시 저장 완료"));
    }

    // 피드백 업데이트
    @PostMapping("/updateFeedback")
    public ResponseEntity<?> updateFeedback(@RequestBody Map<String, Object> request) {
        Integer recommendId = (Integer) request.get("recommendId");
        Integer rating = (Integer) request.get("recommendRating");
        String content = (String) request.get("ratingContent");

        recommendService.updateFeedback(recommendId, rating, content);

        return ResponseEntity.ok("피드백 저장 완료");
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> softDeleteRecommend(@PathVariable("id") Long id) {
        boolean success = recommendService.softDeleteRecommend(id);
        if (success) {
            return ResponseEntity.ok("삭제 완료");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제 실패: 대상 없음");
        }
    }

}