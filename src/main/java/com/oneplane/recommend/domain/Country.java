package com.oneplane.recommend.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Country {
    private Long countryId;
    private String countryName;
    private String countryEnName;
    private String isoCode;
    private String continent;
    private String img;
    private Double latitude;
    private Double longitude;
    private String currency;
}
