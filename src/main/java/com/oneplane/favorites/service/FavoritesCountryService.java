package com.oneplane.favorites.service;

import com.oneplane.favorites.domain.FavoritesCountry;
import java.util.List;

public interface FavoritesCountryService {
    void addFavorite(Long countryId, Long userId);
    void removeFavorite(Long favoritesCountryId);
    List<FavoritesCountry> getFavoritesByUser(Long userId);
}