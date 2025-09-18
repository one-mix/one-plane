package com.oneplane.admin.controller;

import com.oneplane.alert.dto.CountryAlertDTO;
import com.oneplane.country.dto.CountrySummaryDTO;
import com.oneplane.country.service.CountryServiceImpl;
import com.oneplane.travelHistory.dto.ContinentStatsDTO;
import com.oneplane.travelHistory.dto.MonthlyStatsDTO;
import com.oneplane.travelHistory.dto.TopCountryStatsDTO;
import com.oneplane.travelHistory.service.TravelHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final CountryServiceImpl countryService;
    private final TravelHistoryService travelHistoryService;


    @GetMapping("/countries")
    public String countryList(
            @RequestParam(required = false) String levelValue,
            @RequestParam(required = false) String keyword, // 국가명 검색 키워드
            Model model
    ) {
        List<CountryAlertDTO> list = countryService.getCountries(levelValue, keyword);

        model.addAttribute("countries", list);
        model.addAttribute("selectedLevel", levelValue);
        model.addAttribute("keyword", keyword); // JSP에 검색어 유지
        model.addAttribute("contentPage", "countries.jsp");
        model.addAttribute("activeMenu", "countries");

        return "admin/layout/adminLayout";
    }

    @GetMapping("/countries/{id}")
    public String countryDetail(@PathVariable("id") Long countryId, Model model) {
        CountryAlertDTO country = countryService.getCountryByIdAdmin(countryId);
        model.addAttribute("country", country);
        model.addAttribute("contentPage", "countryDetail.jsp");
        model.addAttribute("activeMenu", "countries");
        return "admin/layout/adminLayout";
    }

    @PostMapping("/countries/update")
    public String updateCountry(@ModelAttribute CountryAlertDTO country) {
        countryService.updateCountry(country);
        return "redirect:/admin/countries"; // 수정 후 목록으로 이동
    }

    @GetMapping("/dashboard/country")
    public String countryDashboard(Model model) {
        model.addAttribute("contentPage", "country.jsp");
        model.addAttribute("activeMenu", "dashboard");

        return "admin/layout/adminLayout";
    }

    @GetMapping("/continent")
    public ResponseEntity<List<ContinentStatsDTO>> getContinentStats() {
        return ResponseEntity.ok(travelHistoryService.getContinentStats());
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyStatsDTO>> getMonthlyStats() {
        return ResponseEntity.ok(travelHistoryService.getMonthlyStats());
    }

    @GetMapping("/top-recommend")
    public ResponseEntity<List<TopCountryStatsDTO>> getTopRecommendCountries() {
        return ResponseEntity.ok(travelHistoryService.getTopRecommendCountries());
    }

    @GetMapping("/top-favorite")
    public ResponseEntity<List<TopCountryStatsDTO>> getTopFavoriteCountries() {
        return ResponseEntity.ok(travelHistoryService.getTopFavoriteCountries());
    }

    @GetMapping("/country-summary")
    @ResponseBody
    public CountrySummaryDTO getCountrySummary() {
        return countryService.getCountrySummary();
    }
}
