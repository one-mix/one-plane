package com.oneplane.recommend.repository;

import com.oneplane.recommend.dto.RecommendResultDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CountryRepository {
    RecommendResultDTO findCountryInfo(String isoCode);
}