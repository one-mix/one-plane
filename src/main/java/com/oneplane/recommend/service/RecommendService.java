package com.oneplane.recommend.service;

import com.oneplane.recommend.dto.RecommendDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;

import java.util.List;

public interface RecommendService {
    Integer saveAgreement(Integer userId);
    String getLatestAgreement(Integer userId);

    void updateInput(RecommendDTO dto);
    RecommendDTO getLatestInput(Integer userId);
    List<RecommendResultDTO> callFlaskRecommend(Integer userId, String purpose, String companion);
}