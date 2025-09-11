package com.oneplane.country.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.oneplane.alert.dao.AlertLevelDao;
import com.oneplane.alert.dto.CountryAlertDTO;
import com.oneplane.country.dao.CountryDao;
import com.oneplane.country.domain.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CountryServiceImpl implements CountryService {
    private final CountryDao countryDao;
    private final AlertLevelDao alertLevelDao;

    @Value("${api.country.key}")
    private String apiKey;   // application.yml에 원본 키(인코딩되지 않은 값) 저장

    private final WebClient webClient =
            WebClient.create("https://apis.data.go.kr/1262000/CountryBasicService");

    public CountryServiceImpl(CountryDao countryDao, AlertLevelDao alertLevelDao) {
        this.countryDao = countryDao;
        this.alertLevelDao = alertLevelDao;
    }

    @Override
    public Country getCountryById(Long id) {
        return countryDao.selectCountryById(id);
    }

    @Override
    public List<Country> getAllCountries() {
        return countryDao.selectAllCountries();
    }

    @Override
    public int addCountry(Country country) {
        return countryDao.insertCountry(country);
    }

    @Override
    public int updateCountry(Country country) {
        return countryDao.updateCountry(country);
    }

    @Override
    public int deleteCountry(Long id) {
        return countryDao.deleteCountry(id);
    }

    @Override
    public Country getCountryByName(String name) {
        return countryDao.findByName(name);
    }

    /**
     * 외교부 API에서 국가 데이터 조회 → DB 동기화
     */
    @Override
    @Transactional
    public void updateCountriesFromApi() {
        try {
            // URI를 안전하게 생성 (자동 재인코딩 방지)
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://apis.data.go.kr/1262000/CountryBasicService/getCountryBasicList")
                    .queryParam("serviceKey", apiKey)   // apiKey는 원본값 (Spring이 자동 인코딩)
                    .queryParam("numOfRows", 200)
                    .queryParam("pageNo", 1)
                    .queryParam("resultType", "xml")    // 실제 응답이 XML 이므로 xml로 고정
                    .build(true)   // true = 이미 인코딩된 값은 건드리지 않음
                    .toUri();

            log.info("최종 요청 URL: {}", uri);

            // API 호출
            String response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("API Response: {}", response);

            // XML 파싱
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode root = xmlMapper.readTree(response.getBytes(StandardCharsets.UTF_8));

            // 에러 코드 확인
            String code = root.path("header").path("resultCode").asText();
            if (!"00".equals(code)) {
                String msg = root.path("header").path("resultMsg").asText();
                log.error("외교부 API 오류: {} - {}", code, msg);
                return;
            }

            JsonNode items = root.path("body").path("items").path("item");

            // 단일 객체일 수도 있으니 리스트로 변환
            List<JsonNode> nodeList = new ArrayList<>();
            if (items.isArray()) {
                items.forEach(nodeList::add);
            } else if (!items.isMissingNode()) {
                nodeList.add(items);
            }

            // 기존 데이터 전체 삭제 (원하는 경우만 사용)
            // countryDao.deleteAllCountries();

            // DB 저장
            for (JsonNode item : nodeList) {
                Country country = new Country();
                country.setCountryName(item.path("countryName").asText());
                country.setCountryEnName(item.path("countryEnName").asText());
                country.setIsoCode(item.path("isoCode").asText());
                country.setContinent(item.path("continent").asText());
                country.setImg(item.path("imgUrl").asText());

                // 기본값
                country.setLatitude(0.0);
                country.setLongitude(0.0);
                country.setCurrency("UNKNOWN");

                countryDao.insertCountry(country);
            }

            log.info("국가 데이터 API 동기화 완료, 총 {}건 저장됨", nodeList.size());
        } catch (Exception e) {
            log.error("국가 데이터 API 동기화 실패", e);
        }
    }

    /**
     * 매일 새벽 3시에 자동 실행
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void scheduledUpdateCountries() {
        updateCountriesFromApi();
    }

    @Override
    public List<CountryAlertDTO> getCountries(String levelValue, String keyword) {
        return countryDao.findCountries(levelValue, keyword);
    }

    public CountryAlertDTO getCountryByIdAdmin(Long countryId) {
        return countryDao.findCountryById(countryId);
    }

    public void updateCountry(CountryAlertDTO country) {
        countryDao.updateCountryAdmin(country);
        alertLevelDao.updateCountryLevelAdmin(country);
    }
}
