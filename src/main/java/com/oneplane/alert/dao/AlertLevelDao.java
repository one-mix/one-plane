//작성자: 방대혁, 오수경
package com.oneplane.alert.dao;

import com.oneplane.alert.domain.AlertLevel;
import com.oneplane.alert.dto.AlertLevelDTO;
import com.oneplane.alert.dto.CountryAlertDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 여행 경보(AlertLevel) 관련 MyBatis DAO
 * - 국가별 여행 경보 조회, 추가, 수정, 검증 등의 기능 제공
 */
@Mapper
public interface AlertLevelDao {

    // 국가별 여행경보 1개 조회
    AlertLevel selectAlertByCountryId(@Param("countryId") Long countryId);

    // 여행경보 추가
    void insertAlert(AlertLevel alertLevel);

    List<AlertLevel> selectAllAlerts();

    /**
     * 특정 국가에 이미 여행경보가 등록되어 있는지 확인
     * @param countryId 국가 ID
     * @return 존재 여부 (0 = 없음, 1 = 있음)
     * 작성자: 방대혁
     */
    Integer existsByCountryId(Integer countryId);

    /**
     * 여행경보 추가 (DTO 기반)
     * @param alertLevel 여행경보 DTO
     * 작성자: 방대혁
     */
    void insertAlertLevel(AlertLevelDTO alertLevel);

    /**
     * 여행경보 수정 (DTO 기반)
     * @param alertLevel 여행경보 DTO
     * 작성자: 방대혁
     */
    void updateAlertLevel(AlertLevelDTO alertLevel);

    /**
     * 국가 ISO 코드 기반 여행경보 조회
     * @param isoCode 국가 ISO 코드
     * @return AlertLevelDTO
     * 작성자: 방대혁
     */
    AlertLevelDTO findByIsoCode(String isoCode);

    /**
     * 관리자 페이지에서 국가별 여행경보 수정
     * @param country 국가 + 경보 DTO
     * 작성자: 방대혁
     */
    void updateCountryLevelAdmin(CountryAlertDTO country);
}
