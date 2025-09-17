package com.oneplane.recommend.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 추천 관련 데이터 전송 객체 (DTO)
 * - 사용자 입력 및 추천 결과를 담음
 * - DB 매핑 및 Controller ↔ Service ↔ Repository 계층 간 데이터 전달에 활용
 */
@Data
public class RecommendDTO {

    /** 추천 ID (PK) */
    private Integer recommendId;

    /** 사용자 ID (FK: user) */
    private Integer userId;

    /** 여행 이력 ID (FK: travel_history) */
    private Integer travelHistoryId;

    /** 국가 ID (FK: country) */
    private Integer countryId;

    /** 여행 목적 */
    private String travelPurpose;

    /** 동반자 */
    private String companion;

    /** 추천 결과 평점 (1~5) */
    private Integer recommendRating;

    /** 추천 피드백 내용 */
    private String ratingContent;

    /** 개인정보 활용 동의 여부 (Y/N) */
    private String agreement;

    /** 생성 시각 */
    private LocalDateTime createdAt;

    /** 삭제 시각 (soft delete용) */
    private LocalDateTime deletedAt;
}
