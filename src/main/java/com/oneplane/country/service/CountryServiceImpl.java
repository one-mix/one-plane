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

/**
 * CountryService 구현체
 * 방대혁 작성 메서드 주석 유지, 그 외 메서드 작성자: 오수경
 */
@Slf4j
@Service
public class CountryServiceImpl implements CountryService {

    private final CountryDao countryDao;
    private final AlertLevelDao alertLevelDao;

    /** 외부 API Key – 작성자: 오수경 */
    @Value("${api.country.key}")
    private String apiKey;

    /** 외부 공공데이터 API 호출용 WebClient – 작성자: 오수경 */
    private final WebClient webClient =
            WebClient.create("https://apis.data.go.kr/1262000/CountryBasicService");

    public CountryServiceImpl(CountryDao countryDao, AlertLevelDao alertLevelDao) {
        this.countryDao = countryDao;
        this.alertLevelDao = alertLevelDao;
    }

    /** ID로 국가 조회 – 작성자: 오수경 */
    @Override
    public Country getCountryById(Long id) {
        return countryDao.selectCountryById(id);
    }

    /** 전체 국가 조회 – 작성자: 오수경 */
    @Override
    public List<Country> getAllCountries() {
        return countryDao.selectAllCountries();
    }

    /** 국가 신규 등록 – 작성자: 오수경 */
    @Override
    public int addCountry(Country country) {
        return countryDao.insertCountry(country);
    }

    /** 국가 정보 수정 – 작성자: 오수경 */
    @Override
    public int updateCountry(Country country) {
        return countryDao.updateCountry(country);
    }

    /** 국가 삭제 – 작성자: 오수경 */
    @Override
    public int deleteCountry(Long id) {
        return countryDao.deleteCountry(id);
    }

    /** 국가명으로 단일 조회 – 작성자: 오수경 */
    @Override
    public Country getCountryByName(String name) {
        return countryDao.findByName(name);
    }

    /** 국가명으로 ISO 코드 조회 – 작성자: 오수경 */
    @Override
    public String getIsoCodeByName(String name) {
        Country country = countryDao.findByName(name);
        return country != null ? country.getIsoCode() : null;
    }

    /** 국가명으로 단일 조회(별칭) – 작성자: 오수경 */
    @Override
    public Country findByName(String name) {
        return countryDao.findByName(name);
    }

    /** 여행경보 조건으로 국가 목록 조회 – 작성자: 방대혁 */
    @Override
    public List<CountryAlertDTO> getCountries(String levelValue, String keyword) {
        return countryDao.findCountries(levelValue, keyword);
    }

    /** 관리자용 국가 상세 조회 – 작성자: 방대혁 */
    @Override
    public CountryAlertDTO getCountryByIdAdmin(Long countryId) {
        return countryDao.findCountryById(countryId);
    }

    /** 국가·경보 정보 업데이트 – 작성자: 방대혁 */
    @Override
    public void updateCountry(CountryAlertDTO country) {
        countryDao.updateCountryAdmin(country);
        alertLevelDao.updateCountryLevelAdmin(country);
    }

    /** 전체 국가 요약 통계 – 작성자: 방대혁 */
    @Override
    public CountrySummaryDTO getCountrySummary() {
        return countryDao.getCountrySummary();
    }
}
