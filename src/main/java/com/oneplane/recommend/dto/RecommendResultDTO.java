package com.oneplane.recommend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RecommendResultDTO {
    private Long recommendId;
    private String countryNameKo;
    private String countryIso3;
    private String city;
    private double score;

    private String continent;
    private String countryImg;
    private String alertLevel;

    private String travelPurpose;
    private String companion;
    private Integer recommendRating;
    private String ratingContent;
    private Date createdAt;
}