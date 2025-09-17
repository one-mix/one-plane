package com.oneplane.recommend.service;

import com.oneplane.recommend.dto.RecommendDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;

import java.util.List;
import java.util.Map;

/**
 * 여행 추천 관련 서비스 인터페이스
 * - 추천 동의, 입력 저장, 추천 결과 요청/저장, 피드백 관리, 추천 이력 관리 포함
 */
public interface RecommendService {

    /** 사용자 추천 동의 저장 */
    Integer saveAgreement(Integer userId);

    /** 최신 추천 동의 여부 조회 (Y/N/null) */
    String getLatestAgreement(Integer userId);

    /** 여행 목적/동행자 입력 저장 */
    void insertInput(RecommendDTO dto);

    /** 최신 입력 정보 조회 */
    RecommendDTO getLatestInput(Integer userId);

    /**
     * Flask 추천 API 호출
     * @param userId 사용자 ID
     * @param purpose 여행 목적
     * @param companion 동행자
     * @return 추천 결과 리스트
     */
    List<RecommendResultDTO> callFlaskRecommend(Integer userId, String purpose, String companion);

    /**
     * 추천 결과에서 사용자가 선택한 국가/도시 저장
     * @param userId 사용자 ID
     * @param countryIso3 ISO3 국가 코드
     * @param city 도시명
     * @return 저장된 recommendId
     */
    Integer saveSelectedCountry(Integer userId, String countryIso3, String city);

    /** 피드백 업데이트 */
    void updateFeedback(Integer recommendId, Integer rating, String content);

    /** 추천 이력 조회 (페이지네이션 적용) */
    List<RecommendResultDTO> getRecommendHistory(Integer userId, int page);

    /** 추천 이력 전체 개수 조회 */
    int getTotalRecommendHistoryCount(Integer userId);

    /** 추천 이력 + 페이지네이션 정보 조회 */
    Map<String, Object> getRecommendHistoryWithPagination(Integer userId, int page);

    void softDeleteRecommend(Integer recommendId, Integer userId);

    /** 추천 소프트 삭제 (마이페이지 전용, recommendId 기준) */
    boolean softDeleteRecommendMyPage(Long recommendId);
}
