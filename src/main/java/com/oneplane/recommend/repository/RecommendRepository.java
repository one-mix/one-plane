package com.oneplane.recommend.repository;

import com.oneplane.recommend.dto.RecommendDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecommendRepository {
    void insertAgreement(Integer userId);

    String findLatestAgreement(Integer userId);

    void insertSelection(RecommendDTO dto);

    void insertFeedback(RecommendDTO dto);
}