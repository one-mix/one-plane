package com.oneplane.country.dto;

import lombok.Data;

@Data
public class CountrySummaryDTO {
    private Long totalCountries;
    private Long safeCountries;
    private Long bannedCountries;
}