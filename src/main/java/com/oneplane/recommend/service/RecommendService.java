package com.oneplane.recommend.service;

import com.oneplane.recommend.dto.RecommendRequestDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;

import java.util.List;

public interface RecommendService {
    List<RecommendResultDTO> getRecommendations(RecommendRequestDTO request);
}