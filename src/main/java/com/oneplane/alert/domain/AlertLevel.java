package com.oneplane.alert.domain;

import lombok.Data;

@Data
public class AlertLevel {
    private Long alertId;
    private Long countryId;
    private String levelValue;

    // 조인 시 Country 객체까지 가져올 수 있도록
    private com.oneplane.country.domain.Country country;
}
