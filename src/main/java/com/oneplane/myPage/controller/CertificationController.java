package com.oneplane.myPage.controller;

import com.oneplane.myPage.dao.CertificationDao;
import com.oneplane.myPage.dto.CertificationDto;
import com.oneplane.myPage.service.CertificationService;
import com.oneplane.myPage.service.CountryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mypage/certification")
public class CertificationController extends BaseController{

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CertificationDao certificationDao;


    @GetMapping("/add")
    public String showCertificationForm(HttpSession session, Model model) {
        if (getUserIdFromSession(session) == null) {
            return "redirect:/login";
        }

        List<Map<String, Object>> countries = countryService.getAllCountries();
        model.addAttribute("dto", new CertificationDto());
        model.addAttribute("countries", countries);
        model.addAttribute("contentPage", "mypage/travelHistory/addCertification.jsp");
        model.addAttribute("activeMenu", "certification");
        model.addAttribute("showSidebar", true);  // 추가
        model.addAttribute("pageTitle", "국가 인증 등록");  // 추가

        return "layout/layout";
    }

    @PostMapping("/add")
    public String registerCertification(@ModelAttribute("dto") CertificationDto dto,
                                        HttpSession session,
                                        RedirectAttributes rttr) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        // 디버깅 로그 추가
        System.out.println("=== 컨트롤러 디버그 ===");
        System.out.println("전달받은 DTO:");
        System.out.println("countryName: " + dto.getCountryName());
        System.out.println("certificationDate: " + dto.getCertificationDate());
        System.out.println("userId: " + userId);

        boolean success = certificationService.registerCountryCertification(dto, userId);
        if (success) {
            rttr.addFlashAttribute("successMessage", "국가 인증이 등록되었습니다.");
        } else {
            rttr.addFlashAttribute("errorMessage", "등록에 실패했습니다. 모든 필드를 확인해주세요.");
        }

        return "redirect:/mypage";
    }

    @GetMapping
    public String showCertifications(HttpSession session, Model model) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        List<Map<String, Object>> certifications = certificationService.getUserCertifications(userId);
        model.addAttribute("certifications", certifications);
        model.addAttribute("contentPage", "mypage/certification/list.jsp");
        model.addAttribute("activeMenu", "certification");
        model.addAttribute("showSidebar", true);  // 추가
        model.addAttribute("pageTitle", "국가 인증 등록");  // 추가

        return "layout/layout";
    }

    public Long getUserIdFromSession(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) return null;

        if (userIdObj instanceof Long) return (Long) userIdObj;
        if (userIdObj instanceof Integer) return ((Integer) userIdObj).longValue();
        if (userIdObj instanceof String) return Long.parseLong((String) userIdObj);

        return null;
    }

    // CertificationController.java에 추가할 메서드

    @GetMapping("/timeline")
    public String showCountryTimeline(HttpSession session, Model model) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        // 인증된 국가들 조회
        List<Map<String, Object>> certifications = certificationService.getUserCertifications(userId);

        // 총 거리 계산 (인스턴스 메서드 호출)
        int totalDistance = certificationDao.getTotalCertificationDistance(userId);

        // 총 사진 수 계산 (인스턴스 메서드 호출)
        int totalPhotos = certificationDao.getTotalPhotosCount(userId);

        model.addAttribute("certifications", certifications);
        model.addAttribute("totalDistance", totalDistance);
        model.addAttribute("totalPhotos", totalPhotos);
        model.addAttribute("contentPage", "mypage/travelHistory/countryTimeline.jsp");
        model.addAttribute("activeMenu", "timeline");
        model.addAttribute("showSidebar", true);  // 추가
        model.addAttribute("pageTitle", "국가 인증 등록");  // 추가
        return "layout/layout";
    }

}
