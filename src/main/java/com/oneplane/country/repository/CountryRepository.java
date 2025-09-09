package com.oneplane.country.repository;

import com.oneplane.recommend.dto.RecommendResultDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CountryRepository {
    RecommendResultDTO findCountryInfo(String isoCode);
    Integer findCountryIdByIsoCode(String isoCode);
}