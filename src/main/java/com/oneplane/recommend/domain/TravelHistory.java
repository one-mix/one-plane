package com.oneplane.recommend.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TravelHistory {
    private Long travelHistoryId;
    private Long userId;
    private Long countryId;
    private String title;
    private String content;
    private Date travelAt;
    private String city;
    private String travelPurpose;
    private String companion;
    private Integer rating;
}