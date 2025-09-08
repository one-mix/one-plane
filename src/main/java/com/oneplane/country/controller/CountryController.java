package com.oneplane.country.controller;

import com.oneplane.country.domain.Country;
import com.oneplane.country.service.CountryService;
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

    // 전체 국가 조회
    @GetMapping("/list")
    public String list(Model model) {
        List<Country> countries = countryService.getAllCountries();
        model.addAttribute("countries", countries);
        model.addAttribute("contentPage", "country/list.jsp");
        return "layout/layout";
    }

    // CountryController.java
    @GetMapping("/search")
    @ResponseBody
    public Country searchByName(@RequestParam String name) {
        return countryService.getCountryByName(name);
    }


    // 수동 동기화 실행용 (API → DB 저장)
    @GetMapping("/sync")
    @ResponseBody
    public Map<String, String> syncCountries() {
        countryService.updateCountriesFromApi();
        return Map.of("message", "국가 데이터 동기화 완료");
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Country detail(@PathVariable Long id) {
        return countryService.getCountryById(id);
    }
}
