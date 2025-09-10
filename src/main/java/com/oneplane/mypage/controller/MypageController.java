package com.oneplane.mypage.controller;

import com.oneplane.recommend.dto.RecommendResultDTO;
import com.oneplane.recommend.repository.RecommendRepository;
import com.oneplane.recommend.service.RecommendService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final RecommendService recommendService;
    private final RecommendRepository recommendRepository;

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
