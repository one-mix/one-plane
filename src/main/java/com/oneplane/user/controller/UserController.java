package com.oneplane.user.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.user.dto.ProfileCompleteRequestDto;
import com.oneplane.user.dto.SignupRequestDto;
import com.oneplane.user.service.CustomOAuth2UserService;
import com.oneplane.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserService userService;

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    // 프로필 완성 페이지 (카카오 로그인 후)
    @GetMapping("/profile/complete")
    public String profileCompletePage(Model model, HttpSession session) {
        // 인증된 사용자인지 확인
        if (!SecurityUtil.isAuthenticated()) {
            log.warn("인증되지 않은 사용자가 프로필 완성 페이지에 접근 시도");
            return "redirect:/";
        }

        // 현재 사용자 정보를 모델에 추가
        Integer userId = SecurityUtil.getCurrentUserId();
        String userEmail = SecurityUtil.getCurrentUserDetails().getUser().getEmail();

        log.info("프로필 완성 페이지 접근 - 사용자 ID: {}, 이메일: {}", userId, userEmail);

        model.addAttribute("userId", userId);
        model.addAttribute("userEmail", userEmail);

        return "user/profileComplete";
    }

    @PostMapping("/profile/complete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> completeProfile(
            @Valid @ModelAttribute ProfileCompleteRequestDto profileRequest,  // @RequestBody → @ModelAttribute 변경
            BindingResult bindingResult,
            HttpSession session) {

        log.info("프로필 완성 요청 - 이름: {}, 나이: {}, 닉네임: {}",
                profileRequest.getName(), profileRequest.getAge(), profileRequest.getNickname());

        Map<String, Object> response = new HashMap<>();

        try {
            // 인증된 사용자인지 확인
            if (!SecurityUtil.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(401).body(response);
            }

            // 유효성 검사 오류 확인
            if (bindingResult.hasErrors()) {
                response.put("success", false);
                response.put("message", "입력 정보를 확인해주세요.");
                return ResponseEntity.badRequest().body(response);
            }

            // 닉네임 중복 확인
            if (userService.isNicknameDuplicate(profileRequest.getNickname())) {
                response.put("success", false);
                response.put("message", "이미 사용중인 닉네임입니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 현재 사용자 ID 가져오기
            Integer currentUserId = SecurityUtil.getCurrentUserId();

            // 프로필 완성 처리
            userService.completeProfile(currentUserId, profileRequest);

            // 세션 정보 업데이트
            updateSessionAfterProfileComplete(session, profileRequest);

            response.put("success", true);
            response.put("message", "프로필이 성공적으로 완성되었습니다!");
            response.put("redirectUrl", "/");

            log.info("프로필 완성 성공 - 사용자 ID: {}, 이름: {}", currentUserId, profileRequest.getName());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("프로필 완성 실패 - 잘못된 요청: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("프로필 완성 처리 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "프로필 완성 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private void updateSessionAfterProfileComplete(HttpSession session, ProfileCompleteRequestDto profileRequest) {
        session.setAttribute("userName", profileRequest.getName());
        session.setAttribute("userNickname", profileRequest.getNickname());
        session.setAttribute("userAge", profileRequest.getAge());
        session.setAttribute("userGender", profileRequest.getGender());

        // 건강 정보 기반 여행 주의 필요 여부 계산
        boolean hasHealthInfo = Boolean.TRUE.equals(profileRequest.getDisease()) ||
                Boolean.TRUE.equals(profileRequest.getDisability()) ||
                Boolean.TRUE.equals(profileRequest.getMedication());

        boolean needsTravelCaution = hasHealthInfo || (profileRequest.getAge() != null && profileRequest.getAge() >= 65);

        session.setAttribute("hasHealthInfo", hasHealthInfo);
        session.setAttribute("needsTravelCaution", needsTravelCaution);

        log.debug("세션 업데이트 완료 - 건강정보: {}, 여행주의: {}", hasHealthInfo, needsTravelCaution);
    }

    @GetMapping("/check-nickname")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkNickname(
            @RequestParam String nickname) {

        log.debug("닉네임 중복 확인 요청: {}", nickname);

        Map<String, Object> response = new HashMap<>();

        try {
            if (nickname == null || nickname.trim().isEmpty()) {
                response.put("available", false);
                response.put("message", "닉네임을 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }

            if (nickname.length() < 2 || nickname.length() > 10) {
                response.put("available", false);
                response.put("message", "닉네임은 2~10글자로 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }

            boolean isDuplicate = userService.isNicknameDuplicate(nickname);

            response.put("available", !isDuplicate);
            response.put("message", isDuplicate ?
                    "이미 사용중인 닉네임입니다." : "사용 가능한 닉네임입니다.");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("닉네임 중복 확인 중 오류 발생", e);
            response.put("available", false);
            response.put("message", "닉네임 확인 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}