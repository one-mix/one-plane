package com.oneplane.alert.domain;

import lombok.Data;
import com.oneplane.country.domain.Country;

/**
 * AlertLevel 도메인 클래스
 * - 특정 국가의 여행경보 단계 정보를 담는 엔티티
 */
@Data
public class AlertLevel {

    /** 여행경보 고유 ID */
    private Long alertId;

    /** 국가 ID (FK: Country.countryId) */
    private Long countryId;

    /** 여행경보 단계 값 (예: 1단계, 2단계, ... 혹은 LOW/MEDIUM/HIGH) */
    private String levelValue;

    // 조인 시 Country 객체까지 가져올 수 있도록
    private Country country;
}
