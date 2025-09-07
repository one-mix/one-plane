package com.oneplane.recommend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendResultDTO {
    private Long countryId;
    private String countryName;
    private String continent;
    private String imageUrl;
    private int similarity;
}
