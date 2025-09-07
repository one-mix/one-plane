package com.oneplane.recommend.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Recommend {
    private Long recommendId;
    private Long userId;
    private Long travelHistoryId;
    private Long countryId;
    private String travelPurpose;
    private String companion;
    private Integer recommendRating;
    private String ratingContent;
    private String agreement;
    private Date createdAt;
    private Date deletedAt;
}