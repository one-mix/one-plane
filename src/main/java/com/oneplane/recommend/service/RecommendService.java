package com.oneplane.recommend.service;

import com.oneplane.recommend.dto.RecommendDTO;

public interface RecommendService {
    Integer saveAgreement(Integer userId);
    String getLatestAgreement(Integer userId);

    void updateInput(RecommendDTO dto);

    void updateSelection(RecommendDTO dto);
    void updateFeedback(RecommendDTO dto);
}