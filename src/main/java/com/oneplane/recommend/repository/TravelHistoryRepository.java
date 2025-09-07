package com.oneplane.recommend.repository;

import com.oneplane.recommend.domain.TravelHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TravelHistoryRepository {
    List<TravelHistory> findByUserId(Long userId);
}
