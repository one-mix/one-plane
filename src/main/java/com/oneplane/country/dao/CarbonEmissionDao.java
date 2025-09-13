package com.oneplane.country.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface CarbonEmissionDao {

    Map<String, Object> findByCountryAndYear(@Param("isoCode") String isoCode,
                                             @Param("year") int year);

}
