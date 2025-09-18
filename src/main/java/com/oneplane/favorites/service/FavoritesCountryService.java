// 작성자: 오수경
package com.oneplane.favorites.service;

import com.oneplane.favorites.domain.FavoritesCountry;
import java.util.List;

/**
 * 즐겨찾기 서비스 인터페이스
 * - 컨트롤러와 DAO 사이의 비즈니스 로직 정의
 */
public interface FavoritesCountryService {

    /**
     * 즐겨찾기 추가
     * @param countryId 국가 ID
     * @param userId    사용자 ID
     */
    void addFavorite(Long countryId, Long userId);

    /**
     * 즐겨찾기 삭제
     * @param favoritesCountryId 즐겨찾기 PK
     */
    void removeFavorite(Long favoritesCountryId);

    /**
     * 특정 사용자 ID의 즐겨찾기 목록 조회
     * @param userId 사용자 ID
     * @return 즐겨찾기 리스트
     */
    List<FavoritesCountry> getFavoritesByUser(Long userId);
}
