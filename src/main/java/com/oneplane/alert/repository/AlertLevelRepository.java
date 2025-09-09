package com.oneplane.alert.repository;

import com.oneplane.alert.dto.AlertLevelDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlertLevelRepository {
    Integer existsByCountryId(Integer countryId);
    void insertAlertLevel(AlertLevelDTO alertLevel);
    void updateAlertLevel(AlertLevelDTO alertLevel);
}
