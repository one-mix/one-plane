package com.oneplane.country.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CarbonService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CountryService countryService;

    public CarbonService(WebClient webClient, CountryService countryService) {
        this.webClient = webClient;
        this.countryService = countryService;
    }

    public List<Map<String, Object>> getCarbonByCountry(String countryName) {
        // DB에서 ISO3 코드 가져오기 (한글/영문 둘 다 지원)
        String iso3 = countryService.getIsoCodeByName(countryName);
        if (iso3 == null) {
            return List.of(Map.of("error", "해당 국가를 찾을 수 없습니다: " + countryName));
        }

        String response = webClient.get()
                .uri("/country/{iso}/indicator/EN.ATM.CO2E.KT?format=json", iso3)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<Map<String, Object>> result = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode dataArray = root.get(1);

            if (dataArray != null && dataArray.isArray()) {
                for (JsonNode node : dataArray) {
                    String year = node.get("date").asText();
                    JsonNode valueNode = node.get("value");
                    result.add(Map.of(
                            "year", year,
                            "emission", valueNode.isNull() ? null : valueNode.asDouble()
                    ));
                }
            }
        } catch (Exception e) {
            result.add(Map.of("error", "Failed to parse carbon data: " + e.getMessage()));
        }
        return result;
    }
}
