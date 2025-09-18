// 작성자: 방대혁
package com.oneplane.alert.dto;

import lombok.Data;

/**
 * AlertLevelDTO
 * - 국가별 여행경보 정보를 주고받기 위한 DTO
 * - 주로 관리자(Admin) 기능에서 사용
 */
@Data
public class AlertLevelDTO {

    /** 국가 ID */
    private Integer countryId;

    /** 국가 ISO 코드 */
    private String isoCode;

    /** 여행 자제 권고 */
    private String attention;

    /** 여행 자제 권고 (부분 적용) */
    private String attentionPartial;

    /** 여행 금지 여부 */
    private String banYna;

    /** 여행 금지 여부 (부분 적용) */
    private String banYnPartial;

    /** 여행 통제 여부 */
    private String control;

    /** 여행 통제 여부 (부분 적용) */
    private String controlPartial;

    /** 여행 제한 여부 */
    private String limita;

    /** 여행 제한 여부 (부분 적용) */
    private String limitaPartial;

    /** 최종 산출된 여행경보 단계 값 */
    private String levelValue;
}
