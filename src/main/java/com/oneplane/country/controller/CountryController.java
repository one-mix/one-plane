// 작성자: 오수경
package com.oneplane.country.controller;

import com.oneplane.country.domain.Country;
import com.oneplane.country.service.CarbonService;
import com.oneplane.country.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;
    private final CarbonService carbonService;

    public CountryController(CountryService countryService, CarbonService carbonService) {
        this.countryService = countryService;
        this.carbonService = carbonService;
    }

    /**
     * 모든 국가 데이터를 JSON 리스트로 반환
     * 프론트에서 전체 국가 목록을 표시할 때 사용
     */
    @GetMapping("/all")
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    /**
     * 국가명을 쿼리 파라미터(name)로 받아 단일 국가 검색
     * 찾지 못하면 404 + {"error":"국가 없음"} 반환
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchCountry(@RequestParam String name) {
        Country c = countryService.getCountryByName(name);
        if (c == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "국가 없음"));
        }
        return ResponseEntity.ok(c);
    }

    /**
     * 국가 ID를 PathVariable로 받아 단일 국가 상세 조회
     * @ResponseBody로 JSON 직렬화
     */
    @GetMapping("/{id}")
    @ResponseBody
    public Country detail(@PathVariable Long id) {
        return countryService.getCountryById(id);
    }

    /**
     * 특정 국가의 특정 연도 탄소 배출량 조회
     * 국가명을 PathVariable로 받아 ISO 코드 기반 검색 후 JSON 반환
     * 데이터가 없으면 404 Not Found
     */
    @GetMapping(value = "/carbon/{year}/{countryName}", produces = "application/json")
    public ResponseEntity<?> getCountryCarbon(
            @PathVariable int year,
            @PathVariable String countryName) {

        Country country = countryService.findByName(countryName);
        if (country == null) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, Object>> carbonData =
                Collections.singletonList(carbonService.getCarbonByCountry(country.getIsoCode(), year));
        return ResponseEntity.ok(carbonData);
    }
}
