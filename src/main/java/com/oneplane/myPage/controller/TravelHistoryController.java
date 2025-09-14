package com.oneplane.myPage.controller;

import com.oneplane.myPage.dto.TravelHistoryDto;
import com.oneplane.myPage.domain.TravelHistory;
import com.oneplane.myPage.service.TravelHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

        System.out.println("=== 여행 기록 저장 시작 ===");
        System.out.println("DTO countryId: " + dto.getCountryId());
        System.out.println("DTO title: " + dto.getTitle());
        System.out.println("DTO content: " + dto.getContent());
        System.out.println("DTO travelDate: " + dto.getTravelDate());
        System.out.println("DTO city: " + dto.getCity());
        System.out.println("DTO rating: " + dto.getRating());
        System.out.println("DTO 이미지: " + (dto.getTravelImg() != null && !dto.getTravelImg().isEmpty() ? dto.getTravelImg().getOriginalFilename() : "없음"));

        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            boolean success = travelHistoryService.save(dto, userId);

            if (success) {
                rttr.addFlashAttribute("successMessage", "여행 기록이 저장되었습니다.");
                System.out.println("저장 성공!");
            } else {
                rttr.addFlashAttribute("errorMessage", "저장에 실패했습니다.");
                System.out.println("저장 실패!");
            }

        } catch (Exception e) {
            System.err.println("저장 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
            rttr.addFlashAttribute("errorMessage", "저장 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "redirect:/mypage/travelHistory";
    }

    @PostMapping("/delete/{id}")
    public String deleteTravel(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes rttr) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        boolean success = travelHistoryService.deleteTravel(id, userId);
        if (success) {
            rttr.addFlashAttribute("successMessage", "삭제되었습니다.");
        } else {
            rttr.addFlashAttribute("errorMessage", "삭제에 실패했습니다.");
        }

        return "redirect:/mypage/travelHistory";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
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
        dto.setCountryId(existing.getCountryId());
        dto.setTitle(existing.getTitle());
        dto.setContent(existing.getContent());
        dto.setTravelDate(existing.getTravelAt().toString());
        dto.setCity(existing.getCity());
        dto.setTravelPurpose(existing.getTravelPurpose());
        dto.setCompanion(existing.getCompanion());
        dto.setRating(existing.getRating());

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
    public String updateTravel(@PathVariable Long id,
                               @ModelAttribute("dto") TravelHistoryDto dto,
                               HttpSession session,
                               RedirectAttributes rttr) {

        System.out.println("=== 여행 기록 수정 시작 ===");
        System.out.println("수정할 ID: " + id);
        System.out.println("DTO title: " + dto.getTitle());
        System.out.println("새 이미지: " + (dto.getTravelImg() != null && !dto.getTravelImg().isEmpty() ? dto.getTravelImg().getOriginalFilename() : "없음"));

        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            boolean success = travelHistoryService.updateTravel(id, dto, userId);

            if (success) {
                rttr.addFlashAttribute("successMessage", "여행 기록이 수정되었습니다.");
                System.out.println("수정 성공!");
            } else {
                rttr.addFlashAttribute("errorMessage", "수정에 실패했습니다.");
                System.out.println("수정 실패!");
            }

        } catch (Exception e) {
            System.err.println("수정 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
            rttr.addFlashAttribute("errorMessage", "수정 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "redirect:/mypage/travelHistory";
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