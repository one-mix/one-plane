package com.oneplane.recommend.repository;

import com.oneplane.recommend.dto.RecommendDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RecommendRepository {
    Integer insertAgreement(Integer userId);
    RecommendDTO getLatestRecommend(Integer userId);
    void updateInput(RecommendDTO dto);
    void updateCountryId(Integer recommendId, Integer countryId);
    void updateFeedback(Integer recommendId, Integer recommendRating, String ratingContent);
}