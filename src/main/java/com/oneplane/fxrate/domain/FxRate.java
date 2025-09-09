package com.oneplane.fxrate.domain;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FxRate {
    private Long fxRateId;
    private Long countryId;
    private String currencyCode;
    private Double dealBasR;
    private LocalDate baseDate;
}
