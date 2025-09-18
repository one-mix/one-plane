// 작성자: 오수경
package com.oneplane.favorites.service;

import com.oneplane.favorites.dao.FavoritesCountryDao;
import com.oneplane.favorites.domain.FavoritesCountry;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * 즐겨찾기 서비스 구현체
 * DAO를 통해 DB에 접근
 */
@Service
public class FavoritesCountryServiceImpl implements FavoritesCountryService {

    private final FavoritesCountryDao favoritesCountryDao;

    /** DAO 생성자 주입 */
    @Autowired
    public FavoritesCountryServiceImpl(FavoritesCountryDao favoritesCountryDao) {
        this.favoritesCountryDao = favoritesCountryDao;
    }

    /** 즐겨찾기 추가 */
    @Override
    public void addFavorite(Long countryId, Long userId) {
        FavoritesCountry fav = new FavoritesCountry();
        fav.setCountryId(countryId);
        fav.setUserId(userId);
        favoritesCountryDao.insertFavorites(fav);
    }

    /** 즐겨찾기 삭제 */
    @Override
    public void removeFavorite(Long favoritesCountryId) {
        favoritesCountryDao.deleteFavorites(favoritesCountryId);
    }

    /** 사용자별 즐겨찾기 목록 조회 */
    @Override
    public List<FavoritesCountry> getFavoritesByUser(Long userId) {
        return favoritesCountryDao.selectFavoritesByUser(userId);
    }
}
