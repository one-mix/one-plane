package com.oneplane.travelHistory.service;

import com.oneplane.favorites.dao.FavoritesCountryDao;
import com.oneplane.recommend.repository.RecommendRepository;
import com.oneplane.travelHistory.dao.TravelHistoryAdminDao;
import com.oneplane.travelHistory.dto.ContinentStatsDTO;
import com.oneplane.travelHistory.dto.MonthlyStatsDTO;
import com.oneplane.travelHistory.dto.TopCountryStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelHistoryServiceImpl implements TravelHistoryService {
    private final TravelHistoryAdminDao travelHistoryAdminDao;
    private final FavoritesCountryDao favoritesCountryDao;
    private final RecommendRepository recommendRepository;

    @Override
    public List<ContinentStatsDTO> getContinentStats() {
        return travelHistoryAdminDao.getContinentStats();
    }

    @Override
    public List<MonthlyStatsDTO> getMonthlyStats() {
        return travelHistoryAdminDao.getMonthlyStats();
    }

    @Override
    public List<TopCountryStatsDTO> getTopRecommendCountries() {
        return recommendRepository.getTopRecommendCountries();
    }

    @Override
    public List<TopCountryStatsDTO> getTopFavoriteCountries() {
        return favoritesCountryDao.getTopFavoriteCountries();
    }
}
