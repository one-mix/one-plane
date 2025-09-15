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

@Controller
@RequestMapping("/mypage/travelHistory")
public class TravelHistoryController extends BaseController {

    @Autowired
    private TravelHistoryService travelHistoryService;

    @GetMapping
    public String showHistory(HttpSession session, Model model) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        List<TravelHistory> travelList = travelHistoryService.getUserTravelHistory(userId);
        model.addAttribute("travelList", travelList);
        model.addAttribute("contentPage", "mypage/travelHistory/history.jsp");
        model.addAttribute("activeMenu", "travelHistory");
        model.addAttribute("showSidebar", true);
        model.addAttribute("pageTitle", "여행 기록");
        return "layout/layout";
    }

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


    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("dto", new TravelHistoryDto());
        model.addAttribute("contentPage", "mypage/travelHistory/addTravel.jsp");
        model.addAttribute("activeMenu", "travelHistory");
        model.addAttribute("showSidebar", false);
        model.addAttribute("pageTitle", "여행 기록 추가");
        return "layout/layout";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute("dto") TravelHistoryDto dto,
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
        if (existing == null || !existing.getUserId().equals(userId)) {
            rttr.addFlashAttribute("errorMessage", "수정 권한이 없습니다.");
            return "redirect:/mypage/travelHistory";
        }

        TravelHistoryDto dto = new TravelHistoryDto();
        // countryId → countryCode 조회
        String countryName = travelHistoryService.findCountryNameById(existing.getCountryId());
        dto.setCountryCode(countryName);

        dto.setCity(existing.getCity());
        dto.setTravelDate(existing.getTravelAt().toString());
        dto.setTitle(existing.getTitle());
        dto.setRating(existing.getRating());
        dto.setTravelPurpose(existing.getTravelPurpose());
        dto.setCompanion(existing.getCompanion());
        dto.setContent(existing.getContent());
        // imagePath 세팅 제거

        model.addAttribute("dto", dto);
        model.addAttribute("travelId", id);
        model.addAttribute("hasExistingImage", existing.getTravelImg() != null);
        model.addAttribute("contentPage", "mypage/travelHistory/editTravel.jsp");
        model.addAttribute("activeMenu", "travelHistory");
        model.addAttribute("showSidebar", false);
        model.addAttribute("pageTitle", "여행 기록 수정");
        return "layout/layout";
    }

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

    @GetMapping("/image/{id}")
    public void serveImage(@PathVariable Long id, HttpServletResponse response) {
        TravelHistory travel = travelHistoryService.getTravelHistoryById(id);
        byte[] imageBytes = travel != null ? travel.getTravelImg() : null;

        if (imageBytes != null && imageBytes.length > 0) {
            try {
                // 클라이언트가 이미지임을 알릴 Content-Type 설정 (JPEG, PNG 등 실제 포맷에 맞춰 변경)
                response.setContentType("image/jpeg");
                // 스트림에 이미지 바이트 쓰기
                ServletOutputStream os = response.getOutputStream();
                os.write(imageBytes);
                os.flush();
            } catch (IOException e) {
                throw new RuntimeException("이미지 전송 실패", e);
            }
        } else {
            // 이미지가 없을 때 기본 플레이스홀더를 제공하거나 404 처리
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public Long getUserIdFromSession(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) return null;
        if (userIdObj instanceof Long) return (Long) userIdObj;
        if (userIdObj instanceof Integer) return ((Integer) userIdObj).longValue();
        if (userIdObj instanceof String) return Long.parseLong((String) userIdObj);
        return null;
    }
}
