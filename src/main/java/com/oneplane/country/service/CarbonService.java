package com.oneplane.country.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarbonService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CarbonService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Map<String, Object>> getCarbonByCountry(String isoCode) {
        String response = webClient.get()
                .uri("/country/{iso}/indicator/EN.ATM.CO2E.KT?format=json", isoCode)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<Map<String, Object>> result = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode dataArray = root.get(1); // 두 번째 배열

            if (dataArray != null && dataArray.isArray()) {
                for (JsonNode node : dataArray) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("year", node.get("date").asText());
                    entry.put("emission", node.get("value").isNull() ? null : node.get("value").asDouble());
                    result.add(entry);
                }
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to parse carbon data: " + e.getMessage());
            result.add(error);
        }
        return result;
    }
}
