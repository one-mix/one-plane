package com.oneplane.travelHistory.dto;

import lombok.Data;

@Data
public class ContinentStatsDTO {
    private String continentName;  // 대륙명
    private Long travelCount;      // 해당 대륙 여행 건수
}
