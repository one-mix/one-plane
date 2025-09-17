package com.oneplane.alert.dto;

import lombok.Data;

/**
 * CountryAlertDTO
 * - 국가별 여행경보와 국가 정보를 담는 DTO
 * - 관리자(Admin) 및 사용자 화면에서 국가 정보 + 경보 단계를 함께 전달할 때 사용
 */
@Data
public class CountryAlertDTO {

    /** 국가 ID (PK) */
    private Integer countryId;

    /** 국가명 (한글명) */
    private String countryName;

    /** 국가명 (영문명) */
    private String countryEnName;

    /** ISO 코드 */
    private String isoCode;

    /** 여행경보 단계 값 */
    private String levelValue;

    /** 국가 이미지 경로 */
    private String countryImg;

    /** 국가 위도 */
    private Double countryLatitude;

    /** 국가 경도 */
    private Double countryLongitude;
}
