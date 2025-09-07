package com.oneplane.recommend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecommendFeatureDTO {
    private String gender;
    private int age;
    private String disease;
    private String disability;
    private String medication;
    private String travelPurpose;
    private String companion;
    private List<Integer> pastRatings;
}
