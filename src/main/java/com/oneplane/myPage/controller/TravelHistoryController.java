package com.oneplane.myPage.controller;

import com.oneplane.country.domain.Country;
import com.oneplane.myPage.dto.TravelHistoryDto;
import com.oneplane.myPage.domain.TravelHistory;
import com.oneplane.myPage.service.TravelHistoryService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.util.List;

/**
 * 여행 기록 관리 컨트롤러
 * 사용자의 여행 이력 조회, 추가, 수정, 삭제 및 이미지 서빙 기능 제공
 *
 * 작성자: 허겸
 */
@Controller
@RequestMapping("/mypage/travelHistory")
public class TravelHistoryController extends BaseController {

    @Autowired
    private TravelHistoryService travelHistoryService;

    /**
     * 여행 이력 목록 페이지 표시
     * 로그인 확인 후 사용자의 모든 여행 기록을 조회하여 모델에 추가
     *
     * @param session HTTP 세션 (userId 확인)
     * @param model 뷰에 전달할 데이터 모델
     * @return 여행 기록 목록 뷰 또는 로그인 리다이렉트
     */
    @GetMapping
    public String showHistory(HttpSession session, Model model) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login"; // 로그인 필요
        }

        List<TravelHistory> travelList = travelHistoryService.getUserTravelHistory(userId);
        model.addAttribute("travelList", travelList);                 // 기록 리스트
        model.addAttribute("contentPage", "mypage/travelHistory/history.jsp");
        model.addAttribute("activeMenu", "travelHistory");         // 사이드바 메뉴 활성화
        model.addAttribute("showSidebar", true);                    // 사이드바 표시
        model.addAttribute("pageTitle", "여행 기록");              // 페이지 제목
        return "layout/layout";
    }

    /**
     * 특정 여행 기록 삭제 처리
     * 사용자가 소유한 기록만 삭제 가능
     *
     * @param id 삭제할 여행 기록 ID
     * @param session HTTP 세션 (userId 확인)
     * @param rttr 리다이렉트 시 메시지 전달용
     * @return 여행 기록 목록 페이지로 리다이렉트
     */
    @PostMapping("/delete/{id}")
    public String deleteTravel(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes rttr) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            boolean success = travelHistoryService.deleteTravel(id, userId);
            if (success) {
                rttr.addFlashAttribute("successMessage", "여행 기록이 삭제되었습니다.");
            } else {
                rttr.addFlashAttribute("errorMessage", "여행 기록 삭제에 실패했습니다.");
            }
        } catch (Exception e) {
            rttr.addFlashAttribute("errorMessage", "삭제 중 오류 발생: " + e.getMessage());
        }
        return "redirect:/mypage/travelHistory";
    }

    /**
     * 여행 기록 추가 폼 표시
     *
     * @param session HTTP 세션 (userId 확인)
     * @param model 뷰 모델
     * @return 여행 기록 추가 뷰 또는 로그인 리다이렉트
     */
    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("dto", new TravelHistoryDto());            // 빈 DTO
        model.addAttribute("contentPage", "mypage/travelHistory/addTravel.jsp");
        model.addAttribute("activeMenu", "travelHistory");
        model.addAttribute("showSidebar", false);
        model.addAttribute("pageTitle", "여행 기록 추가");
        return "layout/layout";
    }

    /**
     * 여행 기록 저장 처리
     * 입력된 DTO를 서비스로 전달하여 DB 저장
     *
     * @param dto 사용자 입력 DTO
     * @param session HTTP 세션 (userId 확인)
     * @param rttr 리다이렉트 메시지 전달용
     * @return 여행 기록 목록 페이지로 리다이렉트
     */
    @PostMapping("/add")
    public String save(
            @ModelAttribute("dto") TravelHistoryDto dto,
            HttpSession session,
            RedirectAttributes rttr) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            boolean success = travelHistoryService.save(dto, userId);
            if (success) {
                rttr.addFlashAttribute("successMessage", "여행 기록이 저장되었습니다.");
            } else {
                rttr.addFlashAttribute("errorMessage", "여행 기록 저장에 실패했습니다.");
            }
        } catch (Exception e) {
            rttr.addFlashAttribute("errorMessage", "저장 중 오류 발생: " + e.getMessage());
        }
        return "redirect:/mypage/travelHistory";
    }

    /**
     * 여행 기록 수정 폼 표시
     * 기존 데이터를 DTO로 변환하여 뷰에 전달
     *
     * @param id 수정할 여행 기록 ID
     * @param session HTTP 세션 (userId 확인)
     * @param model 뷰 모델
     * @param rttr 리다이렉트 메시지
     * @return 여행 기록 수정 뷰 또는 리스트 뷰
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            HttpSession session,
            Model model,
            RedirectAttributes rttr) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        TravelHistory existing = travelHistoryService.getTravelHistoryById(id);
        // 소유자 확인
        if (existing == null || !existing.getUserId().equals(userId)) {
            rttr.addFlashAttribute("errorMessage", "수정 권한이 없습니다.");
            return "redirect:/mypage/travelHistory";
        }

        TravelHistoryDto dto = new TravelHistoryDto();
        // 기존 정보 DTO에 설정
        String countryName = travelHistoryService.findCountryNameById(existing.getCountryId());
        dto.setCountryCode(countryName);
        dto.setCity(existing.getCity());
        dto.setTravelDate(existing.getTravelAt().toString());
        dto.setTitle(existing.getTitle());
        dto.setRating(existing.getRating());
        dto.setTravelPurpose(existing.getTravelPurpose());
        dto.setCompanion(existing.getCompanion());
        dto.setContent(existing.getContent());
        // 이미지 경로 설정은 프론트에서 처리

        model.addAttribute("dto", dto);
        model.addAttribute("travelId", id);
        model.addAttribute("hasExistingImage", existing.getTravelImg() != null);
        model.addAttribute("contentPage", "mypage/travelHistory/editTravel.jsp");
        model.addAttribute("activeMenu", "travelHistory");
        model.addAttribute("showSidebar", false);
        model.addAttribute("pageTitle", "여행 기록 수정");
        return "layout/layout";
    }

    /**
     * 여행 기록 수정 처리
     *
     * @param id 수정할 여행 기록 ID
     * @param dto 사용자 입력 DTO
     * @param session HTTP 세션 (userId 확인)
     * @param rttr 리다이렉트 메시지
     * @return 여행 기록 목록 페이지
     */
    @PostMapping("/edit/{id}")
    public String updateTravel(
            @PathVariable Long id,
            @ModelAttribute("dto") TravelHistoryDto dto,
            HttpSession session,
            RedirectAttributes rttr) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            boolean success = travelHistoryService.updateTravel(id, dto, userId);
            if (success) {
                rttr.addFlashAttribute("successMessage", "여행 기록이 수정되었습니다.");
            } else {
                rttr.addFlashAttribute("errorMessage", "여행 기록 수정에 실패했습니다.");
            }
        } catch (Exception e) {
            rttr.addFlashAttribute("errorMessage", "수정 중 오류 발생: " + e.getMessage());
        }
        return "redirect:/mypage/travelHistory";
    }

    /**
     * 여행 기록 이미지 서빙
     * 기록에 저장된 바이너리 이미지를 HTTP 응답으로 전송
     *
     * @param id 이미지가 포함된 여행 기록 ID
     * @param response HTTP 응답 객체
     */
    @GetMapping("/image/{id}")
    public void serveImage(@PathVariable Long id, HttpServletResponse response) {
        TravelHistory travel = travelHistoryService.getTravelHistoryById(id);
        byte[] imageBytes = travel != null ? travel.getTravelImg() : null;
        if (imageBytes != null && imageBytes.length > 0) {
            try {
                response.setContentType("image/jpeg"); // 이미지 포맷에 맞춰 변경
                ServletOutputStream os = response.getOutputStream();
                os.write(imageBytes);
                os.flush();
            } catch (IOException e) {
                throw new RuntimeException("이미지 전송 실패", e);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 이미지 없을 때 처리
        }
    }

    /**
     * 세션에서 userId를 안전하게 추출하는 헬퍼 메서드
     * 다양한 타입(Long, Integer, String) 지원
     *
     * @param session HTTP 세션
     * @return Long userId 또는 null
     */
    public Long getUserIdFromSession(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) return null;
        if (userIdObj instanceof Long) return (Long) userIdObj;
        if (userIdObj instanceof Integer) return ((Integer) userIdObj).longValue();
        if (userIdObj instanceof String) return Long.parseLong((String) userIdObj);
        return null;
    }
}