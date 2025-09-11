package com.oneplane.country.service;

import com.oneplane.country.domain.Country;
import java.util.List;
import java.util.Map;

public interface CountryService {
    Country getCountryById(Long id);
    List<Country> getAllCountries();
    int addCountry(Country country);
    int updateCountry(Country country);
    int deleteCountry(Long id);

    // 나라 이름 반환
    Country getCountryByName(String name);

    // 외교부 국가별 기본정보 API -> DB 업데이트
    void updateCountriesFromApi();

    Map<String, Double> getWorldGdpShare(int year, String countryName);

    String getIsoCodeByName(String countryName);

    Country findByName(String name);
}
