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

/**
 * 국가 인증 관련 요청을 처리하는 컨트롤러
 * 사용자의 여행 인증 정보 등록, 조회, 타임라인 관리 기능 제공
 * 작성자: 허겸
 */
@Controller
@RequestMapping("/mypage/certification")
public class CertificationController extends BaseController {

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private CountryService countryService;

    /**
     * 국가 인증 등록 폼을 표시하는 메서드
     * 로그인 확인 후 인증 등록 페이지로 이동
     *
     * @param session HTTP 세션 객체 (사용자 인증 확인용)
     * @param model 뷰에 전달할 데이터를 담는 객체
     * @return 인증 등록 페이지 또는 로그인 페이지로 리다이렉트
     */
    @GetMapping("/add")
    public String showCertificationForm(HttpSession session, Model model) {
        // 세션에서 사용자 ID 확인 - 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        if (getUserIdFromSession(session) == null) {
            return "redirect:/login";
        }

        // 모델에 필요한 데이터 추가
        model.addAttribute("dto", new CertificationDto()); // 빈 DTO 객체
        model.addAttribute("countries", countryService.getAllCountries()); // 전체 국가 목록
        model.addAttribute("contentPage", "mypage/travelHistory/addCertification.jsp"); // 포함할 JSP 경로
        model.addAttribute("activeMenu", "certification"); // 활성 메뉴 표시용
        model.addAttribute("pageTitle", "국가 인증 등록"); // 페이지 제목

        return "layout/layout";
    }

    /**
     * 국가 인증 정보를 등록하는 메서드
     * 사용자가 입력한 인증 정보를 데이터베이스에 저장
     *
     * @param dto 사용자가 입력한 인증 정보를 담은 DTO
     * @param session HTTP 세션 객체 (사용자 인증 확인용)
     * @param rttr 리다이렉트 시 메시지 전달용 객체
     * @return 대시보드 페이지로 리다이렉트
     */
    @PostMapping("/add")
    public String registerCertification(
            @ModelAttribute("dto") CertificationDto dto,
            HttpSession session,
            RedirectAttributes rttr) {

        // 세션에서 사용자 ID 획득
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        // 인증 정보 등록 처리
        boolean success = certificationService.registerCountryCertification(dto, userId);

        // 등록 결과에 따른 메시지 설정
        if (success) {
            rttr.addFlashAttribute("successMessage", "국가 인증이 등록되었습니다.");
        } else {
            rttr.addFlashAttribute("errorMessage", "등록에 실패했습니다. 모든 필드를 확인해주세요.");
        }

        return "redirect:/mypage/dashboard";
    }

    /**
     * 사용자의 국가 인증 목록을 조회하는 메서드
     * 로그인한 사용자의 모든 인증 내역을 표시
     *
     * @param session HTTP 세션 객체 (사용자 인증 확인용)
     * @param model 뷰에 전달할 데이터를 담는 객체
     * @return 인증 목록 페이지 또는 로그인 페이지로 리다이렉트
     */
    @GetMapping
    public String showCertifications(HttpSession session, Model model) {
        // 사용자 인증 확인
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        // 사용자의 인증 목록 조회 및 모델에 추가
        model.addAttribute("certifications", certificationService.getUserCertifications(userId));
        model.addAttribute("contentPage", "mypage/certification/list.jsp");
        model.addAttribute("activeMenu", "certification");
        model.addAttribute("pageTitle", "국가 인증 목록");

        return "layout/layout";
    }

    /**
     * 사용자의 여행 타임라인을 표시하는 메서드
     * 인증 내역, 총 거리, 사진 수 등의 통계 정보를 포함
     *
     * @param session HTTP 세션 객체 (사용자 인증 확인용)
     * @param model 뷰에 전달할 데이터를 담는 객체
     * @return 여행 타임라인 페이지 또는 로그인 페이지로 리다이렉트
     */
    @GetMapping("/timeline")
    public String showCountryTimeline(HttpSession session, Model model) {
        // 사용자 인증 확인
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        // 인증 내역 통계 데이터 수집
        List<CertificationTimelineDto> timeline = certificationService.getTimelineData(userId);
        int totalDistance = certificationService.getTotalCertificationDistance(userId);
        int totalPhotos = certificationService.getTotalPhotosCount(userId);

        // 모델에 통계 데이터 추가
        model.addAttribute("timeline", timeline); // 타임라인 데이터
        model.addAttribute("maxDistance", totalDistance); // 최대 거리 (차트 스케일용)
        model.addAttribute("totalPhotos", totalPhotos); // 전체 사진 수
        model.addAttribute("totalDistance", totalDistance); // 전체 여행 거리

        // 레이아웃에서 포함할 JSP 경로
        model.addAttribute("contentPage", "mypage/travelHistory/countryTimeline.jsp");
        model.addAttribute("activeMenu", "timeline");
        model.addAttribute("pageTitle", "여행 타임라인");

        return "layout/layout";
    }
}