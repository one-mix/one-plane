package com.oneplane.favorites.dao;

import com.oneplane.favorites.domain.FavoritesCountry;
import com.oneplane.travelHistory.dto.TopCountryStatsDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface FavoritesCountryDao {
    int insertFavorites(FavoritesCountry favoritesCountry);
    int deleteFavorites(Long favoritesCountryId);
    List<FavoritesCountry> selectFavoritesByUser(Long userId);
    List<TopCountryStatsDTO> getTopFavoriteCountries();
}