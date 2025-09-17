package com.oneplane.admin.controller;

import com.oneplane.alert.dto.CountryAlertDTO;
import com.oneplane.country.dto.CountrySummaryDTO;
import com.oneplane.country.service.CountryService;
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

    private final CountryService countryService;
    private final TravelHistoryService travelHistoryService;

    /**
     * 관리자 메인 대시보드 페이지
     */
    @GetMapping("/dashboard")
    public String adminMain(Model model) {
        model.addAttribute("contentPage", "dashboard.jsp");
        model.addAttribute("activeMenu", "dashboard");

        return "admin/layout/adminLayout";
    }

    /**
     * 국가 리스트 조회 (경보 단계 + 검색 키워드 필터링 가능)
     */
    @GetMapping("/countries")
    public String countryList(
            @RequestParam(required = false) String levelValue,
            @RequestParam(required = false) String keyword,
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

    /**
     * 특정 국가 상세 조회
     */
    @GetMapping("/countries/{id}")
    public String countryDetail(@PathVariable("id") Long countryId, Model model) {
        CountryAlertDTO country = countryService.getCountryByIdAdmin(countryId);
        if (country == null) {
            log.warn("Country not found: {}", countryId);
            return "redirect:/admin/countries?error=notfound";
        }
        model.addAttribute("country", country);
        model.addAttribute("contentPage", "countryDetail.jsp");
        model.addAttribute("activeMenu", "countries");
        return "admin/layout/adminLayout";
    }

    /**
     * 국가 정보 수정 (POST 요청)
     */
    @PostMapping("/countries/update")
    public String updateCountry(@ModelAttribute CountryAlertDTO country) {
        log.info("Updating country: {}", country.getCountryName());
        countryService.updateCountry(country);
        return "redirect:/admin/countries";
    }

    /**
     * 대시보드 내 국가별 페이지
     */
    @GetMapping("/dashboard/country")
    public String countryDashboard(Model model) {
        model.addAttribute("contentPage", "country.jsp");
        model.addAttribute("activeMenu", "dashboard");

        return "admin/layout/adminLayout";
    }

    /**
     * 대륙별 여행 통계 (JSON 반환)
     */
    @GetMapping("/continent")
    public ResponseEntity<List<ContinentStatsDTO>> getContinentStats() {
        return ResponseEntity.ok(travelHistoryService.getContinentStats());
    }

    /**
     * 월별 여행 통계 (JSON 반환)
     */
    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyStatsDTO>> getMonthlyStats() {
        return ResponseEntity.ok(travelHistoryService.getMonthlyStats());
    }

    /**
     * 추천 국가 TOP 리스트 (JSON 반환)
     */
    @GetMapping("/top-recommend")
    public ResponseEntity<List<TopCountryStatsDTO>> getTopRecommendCountries() {
        return ResponseEntity.ok(travelHistoryService.getTopRecommendCountries());
    }

    /**
     * 인기 국가 TOP 리스트 (JSON 반환)
     */
    @GetMapping("/top-favorite")
    public ResponseEntity<List<TopCountryStatsDTO>> getTopFavoriteCountries() {
        return ResponseEntity.ok(travelHistoryService.getTopFavoriteCountries());
    }

    /**
     * 국가 요약 통계 (JSON 반환)
     */
    @GetMapping("/country-summary")
    public ResponseEntity<CountrySummaryDTO> getCountrySummary() {
        return ResponseEntity.ok(countryService.getCountrySummary());
    }
}
