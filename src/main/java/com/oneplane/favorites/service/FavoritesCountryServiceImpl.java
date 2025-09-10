package com.oneplane.favorites.service;

import com.oneplane.favorites.dao.FavoritesCountryDao;
import com.oneplane.favorites.domain.FavoritesCountry;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class FavoritesCountryServiceImpl implements FavoritesCountryService {

    private final FavoritesCountryDao favoritesCountryDao;

    @Autowired
    public FavoritesCountryServiceImpl(FavoritesCountryDao favoritesCountryDao) {
        this.favoritesCountryDao = favoritesCountryDao;
    }

    @Override
    public void addFavorite(Long countryId, Long userId) {
        FavoritesCountry fav = new FavoritesCountry();
        fav.setCountryId(countryId);
        fav.setUserId(userId);
        favoritesCountryDao.insertFavorites(fav);
    }

    @Override
    public void removeFavorite(Long favoritesCountryId) {
        favoritesCountryDao.deleteFavorites(favoritesCountryId);
    }

    @Override
    public List<FavoritesCountry> getFavoritesByUser(Long userId) {
        return favoritesCountryDao.selectFavoritesByUser(userId);
    }
}