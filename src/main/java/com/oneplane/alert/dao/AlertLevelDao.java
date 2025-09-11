package com.oneplane.alert.dao;

import com.oneplane.alert.domain.AlertLevel;
import com.oneplane.alert.dto.AlertLevelDTO;
import com.oneplane.alert.dto.CountryAlertDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertLevelDao {

    // 국가별 여행경보 1개 조회
    AlertLevel selectAlertByCountryId(@Param("countryId") Long countryId);

    // 여행경보 추가
    void insertAlert(AlertLevel alertLevel);

    List<AlertLevel> selectAllAlerts();

    Integer existsByCountryId(Integer countryId);
    void insertAlertLevel(AlertLevelDTO alertLevel);
    void updateAlertLevel(AlertLevelDTO alertLevel);
    AlertLevelDTO findByIsoCode(String isoCode);
    void updateCountryLevelAdmin(CountryAlertDTO country);
}
