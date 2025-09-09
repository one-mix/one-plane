package com.oneplane.alert.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TravelWarningApiResponse {
    private Response response;

    @Data
    public static class Response {
        private Body body;
    }

    @Data
    public static class Body {
        private Items items;
    }

    @Data
    public static class Items {
        private List<Item> item;
    }

    @Data
    public static class Item {
        @JsonProperty("attention")
        private String attention;
        private String attentionNote;
        @JsonProperty("attention_partial")
        private String attentionPartial;
        private String banNote;
        @JsonProperty("ban_yn_partial")
        private String banYnPartial;
        @JsonProperty("ban_yna")
        private String banYna;
        private String continent;
        @JsonProperty("control")
        private String control;
        private String controlNote;
        @JsonProperty("control_partial")
        private String controlPartial;
        private String countryEnName;
        private String countryName;
        private String id;
        private String imgUrl;
        private String imgUrl2;
        @JsonProperty("iso_code")
        private String isoCode;
        @JsonProperty("limita")
        private String limita;
        private String limitaNote;
        @JsonProperty("limita_partial")
        private String limitaPartial;
        private String wrtDt;
    }
}
