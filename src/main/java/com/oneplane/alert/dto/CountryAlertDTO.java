package com.oneplane.alert.dto;

import lombok.Data;

@Data
public class CountryAlertDTO {
    private Integer countryId;
    private String countryName;
    private String countryEnName;
    private String isoCode;
    private String levelValue;
    private String countryImg;

    private Double countryLatitude;
    private Double countryLongitude;
}