package com.oneplane.recommend.repository;

import com.oneplane.recommend.domain.Recommend;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecommendRepository {
    void save(Recommend recommend);
    List<Recommend> findByUserId(Long userId);
}