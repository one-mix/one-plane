package com.oneplane.recommend.dto;

import lombok.Data;

@Data
public class RecommendResultDTO {
    private String countryNameKo;
    private String countryIso3;
    private String city;
    private double score;

    private String continent;
    private String countryImg;
    private String alertLevel;
}