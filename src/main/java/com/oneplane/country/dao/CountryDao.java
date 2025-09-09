package com.oneplane.country.dao;

import com.oneplane.country.domain.Country;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CountryDao {
    Country selectCountryById(Long id);
    List<Country> selectAllCountries();
    int insertCountry(Country country);
    int updateCountry(Country country);
    int deleteCountry(Long id);
    int deleteAllCountries();

    Country findByName(@Param("name") String name);

    Country findByIsoCode(@Param("isoCode") String isoCode);
}
