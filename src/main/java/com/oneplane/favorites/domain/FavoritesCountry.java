// 작성자: 오수경
package com.oneplane.favorites.domain;

import com.oneplane.country.domain.Country;
import lombok.Data;

/**
 * 즐겨찾기 국가 도메인
 * 즐겨찾기 테이블과 매핑되는 엔티티
 */
@Data
public class FavoritesCountry {

    /** 즐겨찾기 PK */
    private Long favoritesCountryId;

    /** 국가 PK */
    private Long countryId;

    /** 사용자 PK */
    private Long userId;

    /** 추천 ID (추천 로직과 연계 시 사용) */
    private Integer recommendId;

    /** 연관된 국가 정보 (조인 시 활용) */
    private Country country;
}
