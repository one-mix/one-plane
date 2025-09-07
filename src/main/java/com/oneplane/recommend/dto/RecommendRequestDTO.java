package com.oneplane.recommend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendRequestDTO {
    private Long userId;
    private String travelPurpose;
    private String companion;
}