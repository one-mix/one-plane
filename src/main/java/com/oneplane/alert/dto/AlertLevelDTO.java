package com.oneplane.alert.dto;

import lombok.Data;

@Data
public class AlertLevelDTO {
    private Integer countryId;
    private String isoCode;
    private String attention;
    private String attentionPartial;
    private String banYna;
    private String banYnPartial;
    private String control;
    private String controlPartial;
    private String limita;
    private String limitaPartial;

    private String levelValue;
}
