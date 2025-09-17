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

    /** 추천 동의 저장 */
    Integer insertAgreement(@Param("userId") Integer userId);

    /** 최신 추천 입력 조회 */
    RecommendDTO getLatestRecommend(@Param("userId") Integer userId);

    /** 여행 목적/동행자 입력 저장 */
    void insertInput(RecommendDTO dto);

    /** 추천 결과 국가/도시 업데이트 */
    void updateCountryAndCity(
            @Param("recommendId") Integer recommendId,
            @Param("countryId") Integer countryId,
            @Param("city") String city
    );

    /** 추천 피드백(평점, 코멘트) 업데이트 */
    void updateFeedback(
            @Param("recommendId") Integer recommendId,
            @Param("recommendRating") Integer recommendRating,
            @Param("ratingContent") String ratingContent
    );

    /** 사용자별 추천 이력 조회 (페이징 지원) */
    List<RecommendResultDTO> findRecommendHistoryByUserId(
            @Param("userId") Integer userId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    /** 사용자 추천 이력 전체 개수 조회 */
    int getTotalRecommendHistoryCount(@Param("userId") Integer userId);

    /** 상위 추천 국가 Top N 조회 */
    List<TopCountryStatsDTO> getTopRecommendCountries();

    void softDeleteRecommend(Map<String, Object> params);

    /** 추천 Soft Delete (마이페이지 전용) */
    int softDeleteRecommendMyPage(@Param("recommendId") Long recommendId);
}
