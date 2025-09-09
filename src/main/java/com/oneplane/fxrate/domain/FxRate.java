package com.oneplane.fxrate.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;

@Data
public class FxRate {
    private Long fxRateId;
    private Long countryId;
    private String currency;
    private Double dealBasR;

    // LocalDate -> JSON 직렬화 설정
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate baseDate;
}
