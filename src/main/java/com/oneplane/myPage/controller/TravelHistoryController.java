package com.oneplane.myPage.controller;

import com.oneplane.myPage.dto.TravelHistoryDto;
import com.oneplane.myPage.model.TravelHistory;
import com.oneplane.myPage.service.TravelHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mypage/travelHistory")
public class TravelHistoryController extends BaseController{

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
        model.addAttribute("showSidebar", true); // 사이드바 표시 플래그
        model.addAttribute("pageTitle", "여행 기록");  // 추가
        return "layout/layout";
    }



    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        if (getUserIdFromSession(session) == null) {
            return "redirect:/login";
        }

        model.addAttribute("dto", new TravelHistoryDto());
        model.addAttribute("contentPage", "mypage/travelHistory/addTravel.jsp");
        model.addAttribute("activeMenu", "travelHistory");
        model.addAttribute("showSidebar", false);  // 추가
        model.addAttribute("pageTitle", "여행 기록 추가");  // 추가

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
        boolean success = travelHistoryService.save(dto, userId);
        if (success) {
            rttr.addFlashAttribute("successMessage", "여행 기록이 저장되었습니다.");
        } else {
            rttr.addFlashAttribute("errorMessage", "저장에 실패했습니다.");
        }
        return "redirect:/mypage/travelHistory";
    }

    // 삭제를 GET 링크로 처리할 경우
    // DELETE 전용 메서드 대신 POST로 처리
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

        System.out.println("===== Edit 매핑 호출됨, ID: " + id + " =====");
        System.out.println("===== 요청 URL: /mypage/travelHistory/edit/" + id + " =====");

        // 나머지 코드는 동일
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }
        TravelHistory existing = travelHistoryService.getTravelHistoryById(id);
        if (existing == null || !existing.getUserId().equals(userId)) {
            rttr.addFlashAttribute("errorMessage", "수정 권한이 없습니다.");
            return "redirect:/mypage/travelHistory";
        }
        // DTO 생성 및 필드 매핑
        TravelHistoryDto dto = new TravelHistoryDto();
        dto.setCountryId(existing.getCountryId());
        dto.setTitle(existing.getTitle());
        dto.setContent(existing.getContent());
        dto.setTravelDate(existing.getTravelAt().toLocaleString().toString());
        dto.setCity(existing.getCity());
        dto.setTravelPurpose(existing.getTravelPurpose());
        dto.setCompanion(existing.getCompanion());
        dto.setRating(existing.getRating());
        // travelImg는 빈 MultipartFile로 두고, 기존 이미지는 imagePath로만 관리
        dto.setImagePath(existing.getImagePath());

        model.addAttribute("dto", dto);
        model.addAttribute("travelId", id);
        model.addAttribute("contentPage", "mypage/travelHistory/editTravel.jsp");
        model.addAttribute("activeMenu", "travelHistory");
        model.addAttribute("showSidebar", false);  // 추가
        model.addAttribute("pageTitle", "여행 기록 수정");  // 추가
        return "layout/layout";
    }

    @PostMapping("/edit/{id}")
    public String updateTravel(@PathVariable Long id,
                               @ModelAttribute("dto") TravelHistoryDto dto,
                               HttpSession session,
                               RedirectAttributes rttr) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        // 디버깅용 로그 추가
        System.out.println("=== 컨트롤러에서 받은 DTO 정보 ===");
        System.out.println("DTO imagePath: " + dto.getImagePath());
        System.out.println("새 파일 있음: " + (dto.getTravelImg() != null && !dto.getTravelImg().isEmpty()));

        boolean success = travelHistoryService.updateTravel(id, dto, userId);
        if (success) {
            rttr.addFlashAttribute("successMessage", "여행 기록이 수정되었습니다.");
        } else {
            rttr.addFlashAttribute("errorMessage", "수정에 실패했습니다.");
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
