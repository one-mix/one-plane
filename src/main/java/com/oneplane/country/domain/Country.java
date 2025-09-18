// 작성자: 오수경
package com.oneplane.country.domain;

import lombok.Data;

@Data
public class Country {
    private Long countryId;
    private String countryName;
    private String countryEnName;
    private String isoCode;
    private String continent;
    private String img;
    private Double latitude;
    private Double longitude;
    private Double distance;
}
