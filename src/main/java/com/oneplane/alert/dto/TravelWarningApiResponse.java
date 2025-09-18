//작성자: 방대혁
package com.oneplane.alert.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * TravelWarningApiResponse
 * - 공공데이터포털 "여행경보 API" 응답을 매핑하기 위한 DTO
 * - API 응답 구조(Response → Body → Items → Item)를 그대로 반영
 * - @JsonProperty 어노테이션을 통해 필드명 매핑
 */
@Data
public class TravelWarningApiResponse {

    /** 최상위 응답 객체 */
    private Response response;

    @Data
    public static class Response {
        /** 본문(body) */
        private Body body;
    }

    @Data
    public static class Body {
        /** 여행경보 항목 리스트 */
        private Items items;
    }

    @Data
    public static class Items {
        /** 여행경보 개별 항목(item) 리스트 */
        private List<Item> item;
    }

    /**
     * 여행경보 API 단일 항목
     * - 각 국가별 여행경보 단계 및 관련 정보
     */
    @Data
    public static class Item {

        /** 여행유의 단계 */
        @JsonProperty("attention")
        private String attention;

        /** 여행유의 상세 비고 */
        private String attentionNote;

        /** 여행유의 지역 부분 지정 여부 */
        @JsonProperty("attention_partial")
        private String attentionPartial;

        /** 여행금지 상세 비고 */
        private String banNote;

        /** 여행금지 지역 부분 지정 여부 */
        @JsonProperty("ban_yn_partial")
        private String banYnPartial;

        /** 여행금지 여부(Y/N) */
        @JsonProperty("ban_yna")
        private String banYna;

        /** 대륙명 */
        private String continent;

        /** 여행자제 단계 */
        @JsonProperty("control")
        private String control;

        /** 여행자제 상세 비고 */
        private String controlNote;

        /** 여행자제 지역 부분 지정 여부 */
        @JsonProperty("control_partial")
        private String controlPartial;

        /** 국가명(영문) */
        private String countryEnName;

        /** 국가명(한글) */
        private String countryName;

        /** 고유 ID */
        private String id;

        /** 국가 대표 이미지 URL */
        private String imgUrl;

        /** 국가 보조 이미지 URL */
        private String imgUrl2;

        /** ISO 3자리 국가 코드 */
        @JsonProperty("iso_code")
        private String isoCode;

        /** 철수권고 단계 */
        @JsonProperty("limita")
        private String limita;

        /** 철수권고 상세 비고 */
        private String limitaNote;

        /** 철수권고 지역 부분 지정 여부 */
        @JsonProperty("limita_partial")
        private String limitaPartial;

        /** 데이터 작성일자 */
        private String wrtDt;
    }
}
