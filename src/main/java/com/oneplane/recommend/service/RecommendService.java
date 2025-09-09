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

    // 국가 선택 저장 (ISO3 코드로 countryId 찾아서 저장)
    Integer saveSelectedCountry(Integer userId, String countryIso3);

    // 피드백 업데이트
    void updateFeedback(Integer recommendId, Integer rating, String content);
}