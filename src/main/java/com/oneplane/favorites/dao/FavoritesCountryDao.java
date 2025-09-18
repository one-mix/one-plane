// 작성자: 오수경
package com.oneplane.favorites.dao;

import com.oneplane.favorites.domain.FavoritesCountry;
import com.oneplane.travelHistory.dto.TopCountryStatsDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 즐겨찾기 국가 DAO (MyBatis 매퍼)
 * DB CRUD 및 통계 조회
 */
@Mapper
public interface FavoritesCountryDao {

    /**
     * 즐겨찾기 추가
     * @param favoritesCountry 즐겨찾기 엔티티
     * @return INSERT된 행 수
     */
    int insertFavorites(FavoritesCountry favoritesCountry);

    /**
     * 즐겨찾기 삭제
     * @param favoritesCountryId 즐겨찾기 PK
     * @return DELETE된 행 수
     */
    int deleteFavorites(Long favoritesCountryId);

    /**
     * 특정 사용자 ID의 즐겨찾기 목록 조회
     * @param userId 사용자 ID
     * @return FavoritesCountry 리스트
     */
    List<FavoritesCountry> selectFavoritesByUser(Long userId);

    /**
     * 즐겨찾기 상위 국가 통계 조회
     * @return 상위 인기 국가 DTO 리스트
     */
    List<TopCountryStatsDTO> getTopFavoriteCountries();
}
