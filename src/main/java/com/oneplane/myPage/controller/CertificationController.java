package com.oneplane.myPage.controller;

import com.oneplane.myPage.dto.CertificationDto;
import com.oneplane.myPage.dto.CertificationTimelineDto;
import com.oneplane.myPage.service.CertificationService;
import com.oneplane.myPage.service.CountryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mypage/certification")
public class CertificationController extends BaseController {

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private CountryService countryService;

    @GetMapping("/add")
    public String showCertificationForm(HttpSession session, Model model) {
        if (getUserIdFromSession(session) == null) {
            return "redirect:/login";
        }

        model.addAttribute("dto", new CertificationDto());
        model.addAttribute("countries", countryService.getAllCountries());
        model.addAttribute("contentPage", "mypage/travelHistory/addCertification.jsp");
        model.addAttribute("activeMenu", "certification");
        model.addAttribute("pageTitle", "국가 인증 등록");
        return "layout/layout";
    }

    @PostMapping("/add")
    public String registerCertification(
            @ModelAttribute("dto") CertificationDto dto,
            HttpSession session,
            RedirectAttributes rttr) {

        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

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

        model.addAttribute("certifications", certificationService.getUserCertifications(userId));
        model.addAttribute("contentPage", "mypage/certification/list.jsp");
        model.addAttribute("activeMenu", "certification");
        model.addAttribute("pageTitle", "국가 인증 목록");
        return "layout/layout";
    }

    @GetMapping("/timeline")
    public String showCountryTimeline(HttpSession session, Model model) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        // 인증 내역 통계
        List<CertificationTimelineDto> timeline = certificationService.getTimelineData(userId);
        int totalDistance = certificationService.getTotalCertificationDistance(userId);
        int totalPhotos = certificationService.getTotalPhotosCount(userId);

        model.addAttribute("timeline", timeline);
        model.addAttribute("maxDistance", totalDistance);
        model.addAttribute("totalPhotos", totalPhotos);
        model.addAttribute("totalDistance", totalDistance);

        // 레이아웃에서 include할 JSP 경로
        model.addAttribute("contentPage", "mypage/travelHistory/countryTimeline.jsp");
        model.addAttribute("activeMenu", "timeline");
        model.addAttribute("pageTitle", "여행 타임라인");
        return "layout/layout";
    }
}
