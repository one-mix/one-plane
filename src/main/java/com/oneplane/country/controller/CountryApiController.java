package com.oneplane.country.controller;

import com.oneplane.country.domain.Country;
import com.oneplane.country.service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/countries")
public class CountryApiController {

    private final CountryService countryService;

    public CountryApiController(CountryService countryService) {

        this.countryService = countryService;
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

    // 전세계 GDP 비율 조회 (JSON)
    @GetMapping(value = "/gdp/{year}/{country}", produces = "application/json")
    public Map<String, Double> getWorldGdp(@PathVariable int year, @PathVariable("country") String countryName) {
        return countryService.getWorldGdpShare(year, countryName);
    }
}
