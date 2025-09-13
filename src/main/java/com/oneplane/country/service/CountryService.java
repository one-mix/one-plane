package com.oneplane.country.service;

import com.oneplane.alert.dto.CountryAlertDTO;
import com.oneplane.country.domain.Country;
import com.oneplane.country.dto.CountrySummaryDTO;

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

    String getIsoCodeByName(String countryName);

    Country findByName(String name);

    List<CountryAlertDTO> getCountries(String levelValue, String keyword);
    CountryAlertDTO getCountryByIdAdmin(Long countryId);
    void updateCountry(CountryAlertDTO country);
    CountrySummaryDTO getCountrySummary();
}
