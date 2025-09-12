package com.oneplane.travelHistory.dao;

import com.oneplane.travelHistory.dto.ContinentStatsDTO;
import com.oneplane.travelHistory.dto.MonthlyStatsDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TravelHistoryAdminDao {
    List<ContinentStatsDTO> getContinentStats();
    List<MonthlyStatsDTO> getMonthlyStats();
}
