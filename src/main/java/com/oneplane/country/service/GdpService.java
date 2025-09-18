// 작성자: 오수경
package com.oneplane.country.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class GdpService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GdpService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Map<String, Object> getGdpByCountryAndYear(String isoCode, int year) {
        String response = webClient.get()
                .uri("/country/{iso}/indicator/NY.GDP.MKTP.CD?date={year}&format=json", isoCode, year)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode dataArray = root.get(1); // 두 번째 배열

            if (dataArray != null && dataArray.isArray() && dataArray.size() > 0) {
                JsonNode gdpNode = dataArray.get(0);

                // GDP 값 (nullable 대응)
                JsonNode valueNode = gdpNode.get("value");
                BigDecimal gdpValue = (valueNode != null && !valueNode.isNull())
                        ? valueNode.decimalValue()
                        : null;

                result.put("isoCode", isoCode);
                result.put("year", year);
                result.put("countryName", gdpNode.get("country").get("value").asText());

                if (gdpValue != null) {
                    result.put("gdpRaw", gdpValue); // 원본 값 (BigDecimal)
                    result.put("gdpFormatted", NumberFormat.getInstance(Locale.US).format(gdpValue)); // 보기 좋은 문자열
                } else {
                    result.put("gdpRaw", null);
                    result.put("gdpFormatted", "N/A");
                }
            } else {
                result.put("error", "No GDP data found");
            }

        } catch (Exception e) {
            result.put("error", "Failed to parse GDP response: " + e.getMessage());
        }

        return result;
    }
}
