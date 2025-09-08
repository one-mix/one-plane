package com.oneplane.country.service;

import com.oneplane.country.domain.Country;
import java.util.List;

public interface CountryService {
    Country getCountryById(Long id);
    List<Country> getAllCountries();
    int addCountry(Country country);
    int updateCountry(Country country);
    int deleteCountry(Long id);

    // 외교부 국가별 기본정보 API -> DB 업데이트
    void updateCountriesFromApi();
}
