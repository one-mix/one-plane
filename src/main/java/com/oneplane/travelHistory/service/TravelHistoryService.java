package com.oneplane.travelHistory.service;

import com.oneplane.travelHistory.dto.ContinentStatsDTO;
import com.oneplane.travelHistory.dto.MonthlyStatsDTO;
import com.oneplane.travelHistory.dto.TopCountryStatsDTO;

import java.util.List;

public interface TravelHistoryService {
    List<ContinentStatsDTO> getContinentStats();
    List<MonthlyStatsDTO> getMonthlyStats();
    List<TopCountryStatsDTO> getTopRecommendCountries();
    List<TopCountryStatsDTO> getTopFavoriteCountries();
}
