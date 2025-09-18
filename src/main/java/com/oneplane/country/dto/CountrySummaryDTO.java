//작성자: 방대혁
package com.oneplane.country.dto;

import lombok.Data;

/**
 * CountrySummaryDTO
 * - 전체 국가 요약 통계 정보를 담는 DTO
 * - 관리자 대시보드 사용
 */
@Data
public class CountrySummaryDTO {

    /**
     * 총 국가 수
     */
    private Long totalCountries;

    /**
     * 안전 국가 수
     */
    private Long safeCountries;

    /**
     * 여행 금지 국가 수
     */
    private Long bannedCountries;
}
