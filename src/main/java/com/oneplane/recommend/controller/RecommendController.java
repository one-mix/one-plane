package com.oneplane.recommend.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.recommend.dto.RecommendDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;
import com.oneplane.recommend.service.RecommendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    /**
     * 로그인 여부 확인
     */
    @GetMapping("/check-login")
    public ResponseEntity<Map<String, Object>> checkLogin() {
        Map<String, Object> response = new HashMap<>();
        response.put("loggedIn", SecurityUtil.getCurrentUserId() != null);
        return ResponseEntity.ok(response);
    }

    /**
     * 추천 동의 여부 확인 (Y / N / null)
     */
    @GetMapping("/agreement")
    @ResponseBody
    public String getAgreement() {
        Integer userId = SecurityUtil.getCurrentUserId();
        return recommendService.getLatestAgreement(userId);
    }

    /**
     * 추천 동의 저장
     */
    @PostMapping("/agreement")
    @ResponseBody
    public String saveAgreement() {
        Integer userId = SecurityUtil.getCurrentUserId();
        recommendService.saveAgreement(userId);
        return "동의 완료";
    }

    /**
     * 여행 목적 + 동행자 저장
     */
    @PostMapping("/input")
    @ResponseBody
    public String saveInput(@RequestBody RecommendDTO dto) {
        dto.setUserId(SecurityUtil.getCurrentUserId());
        recommendService.insertInput(dto);
        return "여행 목적/동행자 저장 완료";
    }

    /**
     * 최신 여행 목적 + 동행자 조회
     */
    @GetMapping("/latest-input")
    @ResponseBody
    public RecommendDTO checkLatestInput() {
        Integer userId = SecurityUtil.getCurrentUserId();
        return recommendService.getLatestInput(userId);
    }

    /**
     * 추천 로딩 → Flask 호출 후 결과 상위 3개 추출 → result 페이지로 redirect
     */
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
            recs = recs.subList(0, 3);
        }

        redirectAttributes.addFlashAttribute("user", SecurityUtil.getCurrentUserDetails());
        redirectAttributes.addFlashAttribute("recommendations", recs);

        return "redirect:/recommend/result";
    }

    /**
     * 국가/도시 선택 저장
     */
    @PostMapping("/saveCountry")
    public ResponseEntity<Map<String, Object>> saveCountry(@RequestBody Map<String, String> request) {
        Integer userId = SecurityUtil.getCurrentUserId();
        String countryIso3 = request.get("countryIso3");
        String city = request.get("city");

        Integer recommendId = recommendService.saveSelectedCountry(userId, countryIso3, city);

        return ResponseEntity.ok(Map.of(
                "recommendId", recommendId,
                "message", "국가/도시 저장 완료"
        ));
    }

    /**
     * 추천 피드백 업데이트
     */
    @PostMapping("/updateFeedback")
    public ResponseEntity<String> updateFeedback(@RequestBody Map<String, Object> request) {
        Integer recommendId = (Integer) request.get("recommendId");
        Integer rating = (Integer) request.get("recommendRating");
        String content = (String) request.get("ratingContent");

        recommendService.updateFeedback(recommendId, rating, content);
        return ResponseEntity.ok("피드백 저장 완료");
    }

    /**
     * 추천 삭제 (soft delete: deleted_at 갱신) - 일반 삭제
     */
    @PostMapping("/remove")
    public ResponseEntity<Map<String, String>> removeRecommend(@RequestParam Integer recommendId) {
        Integer userId = SecurityUtil.getCurrentUserId();
        recommendService.softDeleteRecommend(recommendId, userId);
        return ResponseEntity.ok(Map.of("message", "추천 삭제 완료"));
    }

    /**
     * 추천 삭제 (마이페이지 전용 soft delete)
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> softDeleteRecommend(@PathVariable("id") Long id) {
        boolean success = recommendService.softDeleteRecommendMyPage(id);
        if (success) {
            return ResponseEntity.ok("삭제 완료");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제 실패: 대상 없음");
        }
    }
}
