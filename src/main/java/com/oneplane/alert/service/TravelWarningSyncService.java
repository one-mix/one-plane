package com.oneplane.alert.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.oneplane.alert.dao.AlertLevelDao;
import com.oneplane.alert.domain.AlertLevel;
import com.oneplane.country.dao.CountryDao;
import com.oneplane.country.domain.Country;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TravelWarningSyncService {

    private final RestTemplate restTemplate;
    private final CountryDao countryDao;
    private final AlertLevelDao alertLevelDao;

    @Value("${api.warning.key}")
    private String serviceKey;

    public TravelWarningSyncService(RestTemplateBuilder builder, CountryDao countryDao, AlertLevelDao alertLevelDao) {
        this.restTemplate = builder.build();
        this.countryDao = countryDao;
        this.alertLevelDao = alertLevelDao;
    }

    @Transactional
    public void syncTravelWarnings() {
        String url = "https://apis.data.go.kr/1262000/TravelWarningService/getTravelWarningListV3"
                + "?serviceKey=" + serviceKey
                + "&pageNo=1&numOfRows=200&returnType=JSON";

        JsonNode response = restTemplate.getForObject(url, JsonNode.class);
        JsonNode items = response.path("response").path("body").path("items").path("item");

        items.forEach(item -> {
            String isoCode = item.path("iso_code").asText().trim().toUpperCase(); // AFG, JPN, KOR...
            String level = null;

            if (item.hasNonNull("ban_yna") && !item.path("ban_yna").asText().isEmpty()) {
                level = item.path("ban_yna").asText();
            } else if (item.hasNonNull("control") && !item.path("control").asText().isEmpty()) {
                level = item.path("control").asText();
            } else if (item.hasNonNull("attention") && !item.path("attention").asText().isEmpty()) {
                level = item.path("attention").asText();
            }

            if (level != null) {
                Country country = countryDao.findByIsoCode(isoCode);
                if (country != null) {
                    AlertLevel alert = new AlertLevel();
                    alert.setCountryId(country.getCountryId());
                    alert.setLevelValue(level);
                    alertLevelDao.insertAlert(alert);
                } else {
                    System.out.println("국가 코드 매칭 실패: " + isoCode);
                }
            }
        });
    }
}
