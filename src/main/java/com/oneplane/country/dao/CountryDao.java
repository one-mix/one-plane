package com.oneplane.country.dao;

import com.oneplane.alert.dto.CountryAlertDTO;
import com.oneplane.country.domain.Country;
import com.oneplane.country.dto.CountrySummaryDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CountryDao {

    Country selectCountryById(Long id);
    List<Country> selectAllCountries();

    int insertCountry(Country country);
    int updateCountry(Country country);
    int deleteCountry(Long id);
    int deleteAllCountries();

    Country findByName(@Param("name") String name);
    Country findByIsoCode(@Param("isoCode") String isoCode);

    Integer findCountryIdByIsoCode(@Param("isoCode") String isoCode);

    List<Long> findCountryIdsByCurrency(@Param("curUnit") String curUnit);

    RecommendResultDTO findCountryInfo(@Param("isoCode") String isoCode);

    /**
     * 조건에 따른 국가 리스트 조회
     * @param levelValue 여행경보 단계 (여행유의 / 여행자제 / 철수권고 / 여행금지)
     * @param keyword 국가명 검색 키워드
     * @return 국가 리스트 (경보 단계, 국가명, 이미지 포함)
     */
    List<CountryAlertDTO> findCountries(@Param("levelValue") String levelValue,
                                        @Param("keyword") String keyword);

    /**
     * 국가 ID로 단일 국가 조회
     * @param countryId 국가 PK
     * @return CountryAlertDTO (해당 국가 정보 + 경보 단계)
     */
    CountryAlertDTO findCountryById(@Param("countryId") Long countryId);

    /**
     * 관리자(Admin)에서 국가 정보 수정
     * @param country 수정할 국가 정보 DTO
     */
    void updateCountryAdmin(CountryAlertDTO country);

    /**
     * 전체 국가 요약 정보 조회
     * - 총 국가 수, 여행경보 분포, 대륙별 요약 등
     * @return CountrySummaryDTO
     */
    CountrySummaryDTO getCountrySummary();
}
