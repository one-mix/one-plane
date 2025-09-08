package com.oneplane.alert.dao;

import com.oneplane.alert.domain.AlertLevel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AlertLevelDao {

    // 국가별 여행경보 1개 조회
    AlertLevel selectAlertByCountryId(@Param("countryId") Long countryId);

    // 여행경보 추가
    void insertAlert(AlertLevel alertLevel);
}
