package com.oneplane.recommend.repository;

import com.oneplane.recommend.dto.RecommendDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecommendRepository {
    void insertAgreement(Integer userId);
    Integer getLatestRecommendId(Integer userId);
    String getLatestAgreement(Integer userId);
    void updateInput(RecommendDTO dto);
    RecommendDTO getLatestInput(Integer userId);
}