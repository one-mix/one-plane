// 작성자: 오수경
package com.oneplane.country.controller;

import com.oneplane.country.domain.Country;
import com.oneplane.country.service.CarbonService;
import com.oneplane.country.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    // 모든 국가 조회
    @GetMapping("/all")
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchCountry(@RequestParam String name) {
        Country c = countryService.getCountryByName(name);
        if (c == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "국가 없음"));
        }
        return ResponseEntity.ok(c);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Country detail(@PathVariable Long id) {
        return countryService.getCountryById(id);
    }

    // 국가별 탄소 배출량 조회 (국가명 기반)
    @GetMapping(value = "/carbon/{year}/{countryName}", produces = "application/json")
    public ResponseEntity<?> getCountryCarbon(
            @PathVariable int year,
            @PathVariable String countryName) {

        Country country = countryService.findByName(countryName);
        if (country == null) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, Object>> carbonData = Collections.singletonList(carbonService.getCarbonByCountry(country.getIsoCode(), year));
        return ResponseEntity.ok(carbonData);
    }
}
