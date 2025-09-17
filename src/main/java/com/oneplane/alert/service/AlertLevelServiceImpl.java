package com.oneplane.alert.service;

import com.oneplane.alert.dao.AlertLevelDao;
import com.oneplane.alert.domain.AlertLevel;
import com.oneplane.alert.dto.AlertLevelDTO;
import com.oneplane.alert.dto.TravelWarningApiResponse;
import com.oneplane.country.dao.CountryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AlertLevelServiceImpl
 * - 공공데이터포털 여행경보 API 호출 및 DB 반영 서비스
 * - API에서 국가별 여행경보 데이터를 가져와 AlertLevel 테이블에 저장/갱신
 */
@Service
@RequiredArgsConstructor
public class AlertLevelServiceImpl implements AlertLevelService {

    private final CountryDao countryRepository;  // 국가 정보 조회용 DAO
    private final RestTemplate restTemplate;     // API 호출용 RestTemplate
    private final AlertLevelDao alertLevelDao;   // 여행경보 CRUD DAO

    @Value("${api.serviceKey}")
    private String serviceKey;   // 공공데이터 API 인증키

    @Value("${api.pageNo}")
    private int pageNo;          // API 요청 페이지 번호

    @Value("${api.numOfRows}")
    private int numOfRows;       // API 요청 데이터 수

    /**
     * API 응답 데이터를 기준으로 경보 레벨 문자열을 판별
     * @param item API에서 내려온 여행경보 항목
     * @return 경보 레벨 (여행금지 / 철수권고 / 여행자제 / 여행유의)
     */
    @Override
    public String determineLevelValue(TravelWarningApiResponse.Item item) {
        if (item.getBanYna() != null || item.getBanYnPartial() != null) {
            return "여행금지";
        }
        if (item.getLimita() != null || item.getLimitaPartial() != null) {
            return "철수권고";
        }
        if (item.getControl() != null || item.getControlPartial() != null) {
            return "여행자제";
        }
        if (item.getAttention() != null || item.getAttentionPartial() != null) {
            return "여행유의";
        }
        return null; // 경보가 없는 경우
    }

    /**
     * 공공데이터포털 여행경보 API 호출 후 DB에 저장/갱신
     */
    @Override
    public void fetchAndSaveAlertLevels() {
        // API URL 구성
        String apiUrl = String.format(
                "https://apis.data.go.kr/1262000/TravelWarningServiceV3/getTravelWarningListV3?serviceKey=%s&pageNo=%d&numOfRows=%d",
                serviceKey, pageNo, numOfRows);

        TravelWarningApiResponse apiResponse;

        try {
            // API 호출
            apiResponse = restTemplate.getForObject(apiUrl, TravelWarningApiResponse.class);

            // 응답 검증
            if (apiResponse == null ||
                    apiResponse.getResponse() == null ||
                    apiResponse.getResponse().getBody() == null ||
                    apiResponse.getResponse().getBody().getItems() == null) {
                System.out.println("API response is empty or malformed");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error occurred while calling API: " + e.getMessage());
            return;
        }

        // API 응답에서 item 리스트 추출
        List<TravelWarningApiResponse.Item> items =
                apiResponse.getResponse().getBody().getItems().getItem();

        if (items == null || items.isEmpty()) {
            System.out.println("No items found in API response");
            return;
        }

        // Item → AlertLevelDTO 변환
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

            // determineLevelValue 메서드로 경보 단계 판별
            alertLevel.setLevelValue(determineLevelValue(item));
            return alertLevel;
        }).collect(Collectors.toList());

        // DB 저장/갱신
        for (AlertLevelDTO alertLevel : alertLevels) {
            String isoCode = alertLevel.getIsoCode();

            // isoCode → countryId 매핑
            Integer countryId = countryRepository.findCountryIdByIsoCode(isoCode);

            if (countryId != null) {
                alertLevel.setCountryId(countryId);

                // 이미 존재하면 UPDATE, 없으면 INSERT
                Integer exists = alertLevelDao.existsByCountryId(countryId);
                if (exists != null && exists > 0) {
                    alertLevelDao.updateAlertLevel(alertLevel);
                } else {
                    alertLevelDao.insertAlertLevel(alertLevel);
                }
            } else {
                System.out.println("Invalid iso_code: " + isoCode);
            }
        }
    }

    @Override
    public List<AlertLevel> getAllAlerts() {
        return alertLevelDao.selectAllAlerts();
    }
}
