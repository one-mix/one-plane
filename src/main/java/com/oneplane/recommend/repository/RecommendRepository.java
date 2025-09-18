//작성자:방대혁,오수경
package com.oneplane.recommend.repository;

import com.oneplane.recommend.dto.RecommendDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;
import com.oneplane.travelHistory.dto.TopCountryStatsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 추천 관련 MyBatis Mapper
 * - 추천 입력, 이력 조회, 피드백 업데이트, 소프트 삭제 등 관리
 */
@Mapper
public interface RecommendRepository {

    /**
     * 추천 동의 저장
     * 작성자:방대혁
     */
    Integer insertAgreement(@Param("userId") Integer userId);

    /**
     * 최신 추천 입력 조회
     * 작성자:방대혁
     */
    RecommendDTO getLatestRecommend(@Param("userId") Integer userId);

    /**
     * 여행 목적/동행자 입력 저장
     * 작성자:방대혁
     */
    void insertInput(RecommendDTO dto);

    /**
     * 추천 결과 국가/도시 업데이트
     * 작성자:방대혁
     */
    void updateCountryAndCity(
            @Param("recommendId") Integer recommendId,
            @Param("countryId") Integer countryId,
            @Param("city") String city
    );

    /**
     * 추천 피드백(평점, 코멘트) 업데이트
     * 작성자:방대혁
     */
    void updateFeedback(
            @Param("recommendId") Integer recommendId,
            @Param("recommendRating") Integer recommendRating,
            @Param("ratingContent") String ratingContent
    );

    /**
     * 사용자별 추천 이력 조회 (페이징 지원)
     * 작성자:방대혁
     */
    List<RecommendResultDTO> findRecommendHistoryByUserId(
            @Param("userId") Integer userId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    /**
     * 사용자 추천 이력 전체 개수 조회
     * 작성자:방대혁
     */
    int getTotalRecommendHistoryCount(@Param("userId") Integer userId);

    /**
     * 상위 추천 국가 Top N 조회
     * 작성자:방대혁
     */
    List<TopCountryStatsDTO> getTopRecommendCountries();

    void softDeleteRecommend(Map<String, Object> params);

    /**
     * 추천 Soft Delete (마이페이지 전용)
     * 작성자:방대혁
     */
    int softDeleteRecommendMyPage(@Param("recommendId") Long recommendId);
}
