package com.oneplane.travelHistory.dto;

import lombok.Data;

@Data
public class TopCountryStatsDTO {
    private String countryName;
    private Long count;
}