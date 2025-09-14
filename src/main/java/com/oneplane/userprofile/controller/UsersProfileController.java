package com.oneplane.userprofile.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.userprofile.domain.UsersProfile;
import com.oneplane.userprofile.service.UsersProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mypage/profile")
@RequiredArgsConstructor
@Slf4j
public class UsersProfileController {

    private final UsersProfileService userProfileService;

    /**
     * 프로필 수정 폼 조회
     * URL: /mypage/profile/edit
     */
    @GetMapping("/edit")
    public String showEditForm(Model model) {
        Long userId = Long.valueOf(SecurityUtil.getCurrentUserId());
        UsersProfile profile = userProfileService.getUserProfile(userId);
        model.addAttribute("profile", profile);
        model.addAttribute("contentPage", "mypage/profile/userEdit.jsp");
        model.addAttribute("activeMenu", "profile");
        model.addAttribute("showSidebar", true);
        return "layout/layout";
    }

    /**
     * 프로필 수정 처리
     * URL: /mypage/profile/edit
     */
    @PostMapping("/edit")
    public String updateProfile(
            @RequestParam("email") String email,
            @RequestParam("nickname") String nickname,
            Model model) {

        Long userId = Long.valueOf(SecurityUtil.getCurrentUserId());
        boolean success = userProfileService.updateUserProfile(userId, email, nickname);
        if (success) {
            model.addAttribute("successMessage", "프로필이 업데이트되었습니다.");
        } else {
            model.addAttribute("errorMessage", "프로필 업데이트에 실패했습니다.");
        }
        return "redirect:/mypage/profile/edit";
    }

    @GetMapping("/out")
    public String showWithdrawForm(Model model) {
        Long userId = Long.valueOf(SecurityUtil.getCurrentUserId());
        model.addAttribute("userId", userId);
        model.addAttribute("contentPage", "mypage/profile/userOut.jsp");
        model.addAttribute("activeMenu", "profile");
        model.addAttribute("showSidebar", true);
        return "layout/layout";
    }

    /**
     * 회원 탈퇴 처리 (Soft Delete)
     * URL: /mypage/profile/out
     */
    @PostMapping("/out")
    public String withdrawUser(Model model) {
        Long userId = Long.valueOf(SecurityUtil.getCurrentUserId());
        boolean success = userProfileService.withdrawUser(userId);

        if (success) {
            // 세션 무효화 등 추가 처리 필요 시 여기에 삽입
            model.addAttribute("successMessage", "정상적으로 탈퇴 처리되었습니다.");
            return "redirect:/login";
        } else {
            model.addAttribute("errorMessage", "탈퇴 처리에 실패했습니다.");
            return "redirect:/mypage/profile/out";
        }
    }
}
