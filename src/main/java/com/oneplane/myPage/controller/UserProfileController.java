package com.oneplane.myPage.controller;

import com.oneplane.myPage.dto.UserProfileDto;
import com.oneplane.myPage.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@RequestMapping("/mypage/profile")
public class UserProfileController extends BaseController{

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/edit")
    public String showEditForm(HttpSession session, Model model) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }

        UserProfileDto profile = userProfileService.getUserInfo(userId);
        model.addAttribute("profile", profile);
        model.addAttribute("contentPage", "mypage/profile/edit.jsp"); // 중복 제거
        model.addAttribute("activeMenu", "profile");
        model.addAttribute("showSidebar", true);
        model.addAttribute("pageTitle", "프로필 수정");
        return "layout/layout";
    }


    // AJAX: 프로필 사진 업로드
    @PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("profileImage") MultipartFile file,
            HttpSession session) {

        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("fail");
        }
        boolean success = userProfileService.updateProfileImage(userId, file);
        if (success) {
            // 세션 프로필 정보 갱신
            session.setAttribute("userProfile", userProfileService.getUserInfo(userId));
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
    }

    public Long getUserIdFromSession(HttpSession session) {
        Object obj = session.getAttribute("userId");
        if (obj instanceof Long) return (Long) obj;
        if (obj instanceof Integer) return ((Integer) obj).longValue();
        if (obj instanceof String) return Long.parseLong((String) obj);
        return null;
    }
}
