package com.oneplane.recommend.repository;

import com.oneplane.recommend.domain.Country;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CountryRepository {
    Country findById(Long countryId);
    List<Country> findAll();
}
