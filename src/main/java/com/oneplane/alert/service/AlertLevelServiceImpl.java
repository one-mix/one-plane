package com.oneplane.alert.service;


import com.oneplane.alert.dto.AlertLevelDTO;
import com.oneplane.alert.dto.TravelWarningApiResponse;
import com.oneplane.alert.repository.AlertLevelRepository;
import com.oneplane.country.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertLevelServiceImpl implements AlertLevelService {

    private final AlertLevelRepository alertLevelRepository;
    private final CountryRepository countryRepository;
    private final RestTemplate restTemplate;

    @Value("${api.serviceKey}")
    private String serviceKey;

    @Value("${api.pageNo}")
    private int pageNo;

    @Value("${api.numOfRows}")
    private int numOfRows;

    @Override
    public String determineLevelValue(TravelWarningApiResponse.Item item) {
        // 1. 여행금지 (가장 높은 경보)
        if (item.getBanYna() != null || item.getBanYnPartial() != null) {
            return "여행금지";
        }

        // 2. 철수권고
        if (item.getLimita() != null || item.getLimitaPartial() != null) {
            return "철수권고";
        }

        // 3. 여행자제
        if (item.getControl() != null || item.getControlPartial() != null) {
            return "여행자제";
        }

        // 4. 여행유의
        if (item.getAttention() != null || item.getAttentionPartial() != null) {
            return "여행유의";
        }

        // 경보가 없으면 null 반환
        return null;
    }

    @Override
    public void fetchAndSaveAlertLevels() {
        String apiUrl = String.format("https://apis.data.go.kr/1262000/TravelWarningServiceV3/getTravelWarningListV3?serviceKey=%s&pageNo=%d&numOfRows=%d", serviceKey, pageNo, numOfRows);

        // RestTemplate을 통해 API 요청
        TravelWarningApiResponse apiResponse = null;

        try {
            // RestTemplate을 통해 API 요청
            apiResponse = restTemplate.getForObject(apiUrl, TravelWarningApiResponse.class);
            if (apiResponse == null || apiResponse.getResponse() == null || apiResponse.getResponse().getBody() == null || apiResponse.getResponse().getBody().getItems() == null) {
                System.out.println("API response is empty or malformed");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error occurred while calling API: " + e.getMessage());
            return;
        }

        // items 추출
        List<TravelWarningApiResponse.Item> items = apiResponse.getResponse().getBody().getItems().getItem();

        if (items == null || items.isEmpty()) {
            System.out.println("No items found in API response");
            return;
        }

        // items를 AlertLevelDTO로 변환
        List<AlertLevelDTO> alertLevels = items.stream().map(item -> {
            AlertLevelDTO alertLevel = new AlertLevelDTO();
            alertLevel.setIsoCode(item.getIsoCode());
            alertLevel.setAttention(item.getAttention());
            alertLevel.setAttentionPartial(item.getAttentionPartial());
            alertLevel.setBanYna(item.getBanYna());
            alertLevel.setBanYnPartial(item.getBanYnPartial());
            alertLevel.setControl(item.getControl());
            alertLevel.setControlPartial(item.getControlPartial());
            alertLevel.setLimita(item.getLimita());
            alertLevel.setLimitaPartial(item.getLimitaPartial());

            // levelValue를 determineLevelValue 메서드를 사용하여 설정
            String levelValue = determineLevelValue(item);
            alertLevel.setLevelValue(levelValue); // levelValue 필드에 경고 레벨 설정

            return alertLevel;
        }).collect(Collectors.toList());

        // 데이터 처리 및 저장
        for (AlertLevelDTO alertLevel : alertLevels) {
            String isoCode = alertLevel.getIsoCode();

            // isoCode를 이용해 countryId 조회
            Integer countryId = countryRepository.findCountryIdByIsoCode(isoCode);

            if (countryId != null) {
                // countryId 설정
                alertLevel.setCountryId(countryId);

                // 이미 존재하는 country_id에 대해 UPDATE 또는 새로운 country_id에 대해 INSERT
                Integer exists = alertLevelRepository.existsByCountryId(countryId);
                if (exists != null && exists > 0) {
                    alertLevelRepository.updateAlertLevel(alertLevel);
                } else {
                    alertLevelRepository.insertAlertLevel(alertLevel);
                }
            } else {
                // isoCode와 일치하는 countryId가 없을 경우 로그
                System.out.println("Invalid iso_code: " + isoCode);
            }
        }
    }
}