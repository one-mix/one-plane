//작성자:방대혁,오수경
package com.oneplane.country.service;

import com.oneplane.alert.dto.CountryAlertDTO;
import com.oneplane.country.domain.Country;
import com.oneplane.country.dto.CountrySummaryDTO;

import java.util.List;
import java.util.Map;

public interface CountryService {
    Country getCountryById(Long id);
    List<Country> getAllCountries();
    int addCountry(Country country);
    int updateCountry(Country country);
    int deleteCountry(Long id);

    // 나라 이름 반환
    Country getCountryByName(String name);

    String getIsoCodeByName(String countryName);

    Country findByName(String name);
    /**
     * 여행경보 단계와 키워드를 기준으로 국가 목록을 조회합니다.
     *
     * @param levelValue 여행경보 단계 필터 (예: 여행금지, 철수권고 등)
     * @param keyword    국가명 검색 키워드
     * @return 국가 DTO 리스트
     * 작성자:방대혁
     */
    List<CountryAlertDTO> getCountries(String levelValue, String keyword);

    /**
     * 관리자 화면에서 특정 국가 상세 정보를 조회합니다.
     *
     * @param countryId 국가 ID
     * @return CountryAlertDTO 객체
     * 작성자:방대혁
     */
    CountryAlertDTO getCountryByIdAdmin(Long countryId);

    /**
     * 국가 및 여행경보 정보를 업데이트합니다.
     *
     * @param country 업데이트할 CountryAlertDTO 객체
     * 작성자:방대혁
     */
    void updateCountry(CountryAlertDTO country);

    /**
     * 전체 국가 요약 통계를 조회합니다.
     * (총 국가 수, 안전 국가 수, 여행금지 국가 수 등)
     *
     * @return CountrySummaryDTO 객체
     * 작성자:방대혁
     */
    CountrySummaryDTO getCountrySummary();
}
