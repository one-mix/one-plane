package com.oneplane.country.controller;

import com.oneplane.country.domain.Country;
import com.oneplane.country.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/countries")
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    // 모든 국가 조회
    @GetMapping("/all")
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    // 전체 국가 조회
    @GetMapping("/list")
    public String list(Model model) {
        List<Country> countries = countryService.getAllCountries();
        model.addAttribute("countries", countries);
        model.addAttribute("contentPage", "country/list.jsp");
        return "layout/layout";
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
}
