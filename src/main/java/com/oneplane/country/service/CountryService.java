// 공동 작성자: 방대혁, 오수경
package com.oneplane.country.service;

import com.oneplane.alert.dto.CountryAlertDTO;
import com.oneplane.country.domain.Country;
import com.oneplane.country.dto.CountrySummaryDTO;
import java.util.List;

/**
 * 국가 정보 및 여행경보 서비스 인터페이스
 * 방대혁 작성 메서드는 기존 주석 유지,
 * 그 외 메서드는 작성자: 오수경
 */
public interface CountryService {

    /** ID(PK)로 단일 국가 조회
     * 작성자: 오수경 */
    Country getCountryById(Long id);

    /** 모든 국가 리스트 조회
     * 작성자: 오수경 */
    List<Country> getAllCountries();

    /** 국가 신규 등록
     * 작성자: 오수경 */
    int addCountry(Country country);

    /** 국가 정보 수정
     *  작성자: 오수경 */
    int updateCountry(Country country);

    /** 국가 정보 삭제
     * 작성자: 오수경 */
    int deleteCountry(Long id);

    /** 국가명으로 단일 국가 조회
     * 작성자: 오수경 */
    Country getCountryByName(String name);

    /** 국가명으로 ISO 코드 반환
     * 작성자: 오수경 */
    String getIsoCodeByName(String countryName);

    /** 국가명으로 단일 국가 조회(별칭 메서드)
     * 작성자: 오수경 */
    Country findByName(String name);

    /**
     * 여행경보 단계와 키워드를 기준으로 국가 목록을 조회합니다.
     * 작성자: 방대혁
     */
    List<CountryAlertDTO> getCountries(String levelValue, String keyword);

    /**
     * 관리자 화면에서 특정 국가 상세 정보를 조회합니다.
     * 작성자: 방대혁
     */
    CountryAlertDTO getCountryByIdAdmin(Long countryId);

    /**
     * 국가 및 여행경보 정보를 업데이트합니다.
     * 작성자: 방대혁
     */
    void updateCountry(CountryAlertDTO country);

    /**
     * 전체 국가 요약 통계를 조회합니다.
     * 작성자: 방대혁
     */
    CountrySummaryDTO getCountrySummary();
}
