package com.oneplane.recommend.dao;

import com.oneplane.recommend.domain.Country;
import com.oneplane.recommend.domain.TravelHistory;
import com.oneplane.recommend.domain.User;

import java.util.List;

public interface RecommendDAO {
    User getUserProfile(Long userId);
    List<TravelHistory> getUserTravelHistory(Long userId);
    List<Country> getAllCountries();
}