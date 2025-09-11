package com.oneplane.recommend.repository;

import com.oneplane.recommend.dto.RecommendDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecommendRepository {
    Integer insertAgreement(Integer userId);
    RecommendDTO getLatestRecommend(Integer userId);
    void insertInput(RecommendDTO dto);
    void updateCountryAndCity(Integer recommendId, Integer countryId, String city);
    void updateFeedback(Integer recommendId, Integer recommendRating, String ratingContent);
    List<RecommendResultDTO> findRecommendHistoryByUserId(
            @Param("userId") Integer userId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );
    int getTotalRecommendHistoryCount(@Param("userId") Integer userId);
}