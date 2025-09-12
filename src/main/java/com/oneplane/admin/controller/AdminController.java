package com.oneplane.admin.controller;

import com.oneplane.alert.dto.CountryAlertDTO;
import com.oneplane.country.service.CountryServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final CountryServiceImpl countryService;

    @GetMapping("/dashboard")
    public String adminMain(Model model) {
        model.addAttribute("contentPage", "dashboard.jsp");
        model.addAttribute("activeMenu", "dashboard");

        return "admin/layout/adminLayout";
    }

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
}
