package com.oneplane.recommend.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.recommend.dto.RecommendDTO;
import com.oneplane.recommend.service.RecommendService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
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

    /** 국가 선택 저장 */
    @PostMapping("/selection")
    @ResponseBody
    public String updateSelection(@RequestBody RecommendDTO dto) {
        dto.setUserId(SecurityUtil.getCurrentUserId());
        recommendService.insertSelection(dto);
        return "선택 완료";
    }

    /** 피드백 저장 */
    @PostMapping("/feedback")
    @ResponseBody
    public String updateFeedback(@RequestBody RecommendDTO dto) {
        dto.setUserId(SecurityUtil.getCurrentUserId());
        recommendService.insertFeedback(dto);
        return "피드백 저장 완료";
    }
}