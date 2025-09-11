package com.oneplane.country.controller;

import com.oneplane.country.domain.Country;
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

    public CountryApiController(CountryService countryService, GdpService gdpService) {
        this.countryService = countryService;
        this.gdpService = gdpService;
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
}
