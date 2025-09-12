package com.oneplane.travelHistory.dto;

import lombok.Data;

@Data
public class MonthlyStatsDTO {
    private String month;
    private Long travelCount;
}