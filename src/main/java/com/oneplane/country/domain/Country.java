// 작성자: 오수경
package com.oneplane.country.domain;

import lombok.Data;

/**
 * Country 엔티티
 * - 국가 기본 정보와 좌표·거리 등의 속성을 담는 도메인 객체
 * - DB 테이블 매핑용 + 서비스/컨트롤러 간 데이터 전달
 */
@Data
public class Country {
    /** 국가 PK */
    private Long countryId;
    /** 국가명 (한글) */
    private String countryName;
    /** 국가명 (영문) */
    private String countryEnName;
    /** ISO 3자리 코드 */
    private String isoCode;
    /** 대륙명 (예: Asia, Europe) */
    private String continent;
    /** 국가 이미지 경로/URL */
    private String img;
    /** 위도 */
    private Double latitude;
    /** 경도 */
    private Double longitude;
    /** 특정 기준 지점과의 거리 (km 단위 등) */
    private Double distance;
}
