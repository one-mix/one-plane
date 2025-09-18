// 작성자: 오수경
package com.oneplane.favorites.domain;

import com.oneplane.country.domain.Country;
import lombok.Data;

@Data
public class FavoritesCountry {
    private Long favoritesCountryId;
    private Long countryId;
    private Long userId;
    private Integer recommendId;

    private Country country;
}
