package com.oneplane.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class RecommendDTO {
    private Integer recommendId;
    private Integer userId;
    private Integer travelHistoryId;
    private Integer countryId;
    private String travelPurpose;
    private String companion;
    private Integer recommendRating;
    private String ratingContent;
    private String agreement;
    private Date createdAt;
    private Date deletedAt;
}