package com.oneplane.recommend.service;

import com.oneplane.recommend.dto.RecommendDTO;

public interface RecommendService {
    void saveAgreement(Integer userId);
    String getLatestAgreement(Integer userId);

    void insertSelection(RecommendDTO dto);
    void insertFeedback(RecommendDTO dto);
}