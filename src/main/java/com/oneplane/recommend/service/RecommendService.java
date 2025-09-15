package com.oneplane.recommend.service;

import com.oneplane.recommend.dto.RecommendDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;

import java.util.List;
import java.util.Map;

public interface RecommendService {
    Integer saveAgreement(Integer userId);
    String getLatestAgreement(Integer userId);

    void insertInput(RecommendDTO dto);
    RecommendDTO getLatestInput(Integer userId);
    List<RecommendResultDTO> callFlaskRecommend(Integer userId, String purpose, String companion);

    // 국가, 도시 저장 (ISO3 코드로 countryId 찾아서 저장)
    Integer saveSelectedCountry(Integer userId, String countryIso3, String city);

    // 피드백 업데이트
    void updateFeedback(Integer recommendId, Integer rating, String content);
    List<RecommendResultDTO> getRecommendHistory(Integer userId, int page);
    int getTotalRecommendHistoryCount(Integer userId);
    Map<String, Object> getRecommendHistoryWithPagination(Integer userId, int page);
    boolean softDeleteRecommend(Long recommendId);
}