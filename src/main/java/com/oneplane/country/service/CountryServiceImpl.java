// 공동 작성자: 방대혁, 오수경
package com.oneplane.country.service;

import com.oneplane.alert.dao.AlertLevelDao;
import com.oneplane.alert.dto.CountryAlertDTO;
import com.oneplane.country.dao.CountryDao;
import com.oneplane.country.domain.Country;
import com.oneplane.country.dto.CountrySummaryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Slf4j
@Service
public class CountryServiceImpl implements CountryService {
    private final CountryDao countryDao;
    private final AlertLevelDao alertLevelDao;

    // application-secret.yml 에 저장된 API 키 (원본 키, 인코딩 전 값)
    @Value("${api.country.key}")
    private String apiKey;

    // 외부 API 호출용 WebClient (기본 baseUrl 세팅)
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

    @Override
    public String getIsoCodeByName(String name) {
        Country country = countryDao.findByName(name);
        return country != null ? country.getIsoCode() : null;
    }

    @Override
    public Country findByName(String name) {
        return countryDao.findByName(name);
    }

    /**
     * 여행경보 레벨 및 검색 키워드 조건에 따라 국가 목록 조회
     *
     * @param levelValue 여행경보 단계 필터
     * @param keyword    국가명 검색 키워드
     * @return 국가 리스트
     * 작성자:방대혁
     */
    @Override
    public List<CountryAlertDTO> getCountries(String levelValue, String keyword) {
        return countryDao.findCountries(levelValue, keyword);
    }

    /**
     * 국가 ID로 특정 국가 상세 조회
     *
     * @param countryId 국가 ID
     * @return 국가 정보
     * 작성자:방대혁
     */
    @Override
    public CountryAlertDTO getCountryByIdAdmin(Long countryId) {
        return countryDao.findCountryById(countryId);
    }

    /**
     * 국가 및 여행경보 정보 업데이트
     * - CountryDao 와 AlertLevelDao 모두 업데이트
     *
     * @param country 업데이트할 국가 DTO
     * 작성자:방대혁
     */
    @Override
    public void updateCountry(CountryAlertDTO country) {
        countryDao.updateCountryAdmin(country);
        alertLevelDao.updateCountryLevelAdmin(country);
    }

    /**
     * 전체 국가 요약 통계 조회
     *
     * @return CountrySummaryDTO (총 국가 수, 안전 국가 수, 여행금지 국가 수)
     * 작성자:방대혁
     */
    @Override
    public CountrySummaryDTO getCountrySummary() {
        return countryDao.getCountrySummary();
    }
}
