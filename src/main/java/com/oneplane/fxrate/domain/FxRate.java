package com.oneplane.fxrate.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.util.Date;

@Data
public class FxRate {
    private Long fxRateId;
    private Long countryId;
    private String currency;
    private Double dealBasR;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date baseDate;
}
