package com.oneplane.recommend.repository;

import com.oneplane.recommend.dto.RecommendDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecommendRepository {
    Integer insertAgreement(Integer userId);
    RecommendDTO getLatestRecommend(Integer userId);
    void updateInput(RecommendDTO dto);
}