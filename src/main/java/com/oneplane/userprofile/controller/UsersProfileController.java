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
            @RequestParam(value = "disease", required = false) String disease,
            @RequestParam(value = "disability", required = false) String disability,
            @RequestParam(value = "medication", required = false) String medication,
            Model model) {

        Long userId = Long.valueOf(SecurityUtil.getCurrentUserId());
        UsersProfile dto = UsersProfile.builder()
                .userId(userId)
                .email(email)
                .nickname(nickname)
                .disease(disease)
                .disability(disability)
                .medication(medication)
                .build();

        try {
            userProfileService.updateUserProfile(dto);
            model.addAttribute("successMessage", "프로필이 업데이트되었습니다.");
        } catch (IllegalArgumentException ex) {
            log.warn("닉네임 중복 오류 - {}", ex.getMessage());
            model.addAttribute("errorMessage", ex.getMessage());
        } catch (IllegalStateException ex) {
            log.error("프로필 업데이트 실패 - {}", ex.getMessage());
            model.addAttribute("errorMessage", "프로필 업데이트에 실패했습니다.");
        }

        return "redirect:/mypage/profile/edit";
    }

    /**
     * 회원 탈퇴 폼 조회
     * URL: /mypage/profile/out
     */
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
        try {
            userProfileService.withdrawUser(userId);
            // 세션 무효화가 필요하면 추가로 수행
            return "redirect:/login";
        } catch (IllegalStateException ex) {
            log.error("탈퇴 처리 실패 - {}", ex.getMessage());
            model.addAttribute("errorMessage", "탈퇴 처리에 실패했습니다.");
            return "redirect:/mypage/profile/out";
        }
    }
}
