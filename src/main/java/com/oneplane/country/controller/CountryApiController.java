package com.oneplane.country.controller;

import com.oneplane.country.domain.Country;
import com.oneplane.country.service.CarbonService;
import com.oneplane.country.service.CountryService;
import com.oneplane.country.service.GdpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/countries")
public class CountryApiController {

    private final CountryService countryService;
    private final GdpService gdpService;

    private final CarbonService carbonService;

    public CountryApiController(CountryService countryService, GdpService gdpService, CarbonService carbonService) {
        this.countryService = countryService;
        this.gdpService = gdpService;
        this.carbonService = carbonService;
    }

    // 전체 국가 조회 (JSON)
    @GetMapping("/all")
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    // 단일 국가 조회 (JSON)
    @GetMapping("/{id}")
    public Country getCountry(@PathVariable Long id) {
        return countryService.getCountryById(id);
    }

    // 국가별 GDP 조회
    @GetMapping("/gdp/{year}/{countryName}")
    public ResponseEntity<?> getCountryGdp(
            @PathVariable int year,
            @PathVariable String countryName) {

        Country country = countryService.findByName(countryName);
        if (country == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> gdpData = gdpService.getGdpByCountryAndYear(country.getIsoCode(), year);
        return ResponseEntity.ok(gdpData);
    }


    // 국가별 탄소 배출량 조회
    @GetMapping(value = "/carbon/{countryName}", produces = "application/json")
    public ResponseEntity<?> getCountryCarbon(@PathVariable String countryName) {
        Country country = countryService.findByName(countryName);
        if (country == null) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, Object>> carbonData = carbonService.getCarbonByCountry(country.getIsoCode());
        return ResponseEntity.ok(carbonData);
    }

    @GetMapping("/carbon/id/{id}")
    public ResponseEntity<?> getCountryCarbonById(@PathVariable Long id) {
        Country country = countryService.getCountryById(id);
        if (country == null) return ResponseEntity.notFound().build();
        List<Map<String, Object>> carbonData = carbonService.getCarbonByCountry(country.getCountryName());
        return ResponseEntity.ok(carbonData);
    }
}
