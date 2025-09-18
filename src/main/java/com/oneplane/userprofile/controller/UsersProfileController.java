package com.oneplane.userprofile.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.userprofile.domain.UsersProfile;
import com.oneplane.userprofile.service.UsersProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 상세 프로필 관리 컨트롤러
 * 프로필 수정, 탈퇴(Soft Delete) 기능 제공
 *
 * 작성자: 허겸
 */
@Controller
@RequestMapping("/mypage/profile")
@RequiredArgsConstructor
@Slf4j
public class UsersProfileController {

    private final UsersProfileService userProfileService; // 서비스 의존성 주입

    /**
     * 프로필 수정 폼 표시
     * 로그인된 사용자 ID 기반으로 사용자 프로필 조회 후 뷰로 전달
     *
     * @param model 뷰에 전달할 데이터 모델
     * @return 레이아웃 템플릿 뷰
     */
    @GetMapping("/edit")
    public String showEditForm(Model model) {
        Long userId = Long.valueOf(SecurityUtil.getCurrentUserId());
        UsersProfile profile = userProfileService.getUserProfile(userId);
        model.addAttribute("profile", profile);                       // 기존 프로필 정보
        model.addAttribute("contentPage", "mypage/profile/userEdit.jsp");
        model.addAttribute("activeMenu", "profile");
        model.addAttribute("showSidebar", true);
        return "layout/layout";
    }

    /**
     * 프로필 수정 처리
     * 폼 파라미터로 전달된 프로필 항목을 DTO에 매핑하여 서비스 호출
     *
     * @param email       사용자 이메일
     * @param nickname    사용자 닉네임
     * @param disease     기저질환 (선택)
     * @param disability  장애 정보 (선택)
     * @param medication  복용 약물 정보 (선택)
     * @param model       뷰 모델 (메시지 전달용)
     * @return 프로필 수정 폼 뷰(리다이렉트)
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
     * 회원 탈퇴 폼 표시 (Soft Delete 전 단계)
     *
     * @param model 뷰 모델
     * @return 레이아웃 템플릿 뷰
     */
    @GetMapping("/out")
    public String showWithdrawForm(Model model) {
        Long userId = Long.valueOf(SecurityUtil.getCurrentUserId());
        model.addAttribute("userId", userId);                     // 탈퇴 폼에서 사용자 ID 사용
        model.addAttribute("contentPage", "mypage/profile/userOut.jsp");
        model.addAttribute("activeMenu", "profile");
        model.addAttribute("showSidebar", true);
        return "layout/layout";
    }

    /**
     * 회원 탈퇴(Soft Delete) 처리
     * 서비스 호출 후 로그인 페이지로 리다이렉트
     *
     * @param model 뷰 모델 (오류 메시지 전달용)
     * @return 로그인 페이지 또는 탈퇴 폼 뷰(오류 시)
     */
    @PostMapping("/out")
    public String withdrawUser(Model model) {
        Long userId = Long.valueOf(SecurityUtil.getCurrentUserId());
        try {
            userProfileService.withdrawUser(userId);
            return "redirect:/login";  // 로그아웃 및 로그인 페이지 이동
        } catch (IllegalStateException ex) {
            log.error("탈퇴 처리 실패 - {}", ex.getMessage());
            model.addAttribute("errorMessage", "탈퇴 처리에 실패했습니다.");
            return "redirect:/mypage/profile/out";
        }
    }
}