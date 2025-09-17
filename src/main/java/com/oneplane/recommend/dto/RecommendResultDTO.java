package com.oneplane.recommend.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 추천 결과 DTO
 * - 추천된 국가 및 도시, 점수와 메타데이터를 담음
 * - Controller ↔ Service ↔ View 계층 간 전달 객체
 */
@Data
public class RecommendResultDTO {

    /** 추천 ID (FK: recommend) */
    private Long recommendId;

    /** 추천 국가명 (한국어) */
    private String countryNameKo;

    /** 추천 국가 코드 (ISO3) */
    private String countryIso3;

    /** 추천 도시명 */
    private String city;

    /** 추천 점수 (추천 모델 점수) */
    private double score;

    /** 대륙 정보 */
    private String continent;

    /** 국가 이미지 URL */
    private String countryImg;

    /** 여행 경보 단계 */
    private String alertLevel;

    /** 사용자의 여행 목적 */
    private String travelPurpose;

    /** 사용자의 동반자 유형 */
    private String companion;

    /** 추천 결과에 대한 사용자 평점 */
    private Integer recommendRating;

    /** 추천 결과에 대한 사용자 피드백 내용 */
    private String ratingContent;

    /** 생성 시각 */
    private LocalDateTime createdAt;
}
