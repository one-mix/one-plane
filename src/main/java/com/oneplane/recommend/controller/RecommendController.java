package com.oneplane.recommend.controller;

import com.oneplane.recommend.dto.RecommendRequestDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;
import com.oneplane.recommend.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping("/result")
    public String getRecommendations(@ModelAttribute RecommendRequestDTO request, Model model) {
        List<RecommendResultDTO> results = recommendService.getRecommendations(request);
        model.addAttribute("results", results);
        return "recommend/result"; // → result.jsp
    }
}