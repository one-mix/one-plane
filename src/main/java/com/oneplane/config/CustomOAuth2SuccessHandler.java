package com.oneplane.config;

import com.oneplane.user.domain.Role;
import com.oneplane.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("OAuth2 로그인 성공");

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("로그인 성공한 사용자: email={}, name={}, role={}",
                userDetails.getUser().getEmail(), userDetails.getUserName(), userDetails.getRole());

        // 세션 확인 및 강제 생성
        HttpSession session = request.getSession(true);
        log.info("세션 ID: {}, 새로운 세션: {}", session.getId(), session.isNew());

        try {
            User user = userDetails.getUser();

            // 세션에 사용자 정보 저장
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUser_id());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userName", user.getName() != null ? user.getName() : "미설정");
            session.setAttribute("userNickname", user.getNickname() != null ? user.getNickname() : "미설정");
            session.setAttribute("userRole", user.getRole() != null ? user.getRole() : Role.ROLE_USER);
            session.setAttribute("isUser", userDetails.isUser());
            session.setAttribute("isAdmin", userDetails.isAdmin());
            session.setAttribute("needsTravelCaution", userDetails.needsTravelCaution());
            session.setAttribute("hasHealthInfo", userDetails.hasHealthInfo());
            session.setAttribute("isProfileComplete", user.isProfileComplete());

            log.info("세션에 저장된 정보 확인 완료");

        } catch (Exception e) {
            log.error("세션 정보 저장 중 오류 발생", e);
        }

        // 최종 리다이렉트 URL 결정
        String redirectUrl = determineTargetUrl(userDetails, request);
        log.info("로그인 후 최종 리다이렉트 URL: {}", redirectUrl);

        response.sendRedirect(redirectUrl);
    }

    /**
     * 로그인 후 이동할 URL 결정
     */
    private String determineTargetUrl(CustomUserDetails userDetails, HttpServletRequest request) {
        // 1. 관리자라면 관리자 대시보드 페이지로 이동
        if (userDetails.isAdmin()) {
            return "/admin/dashboard";
        }

        // 2. 프로필 미완성 → 프로필 완성 페이지로 이동
        if (isProfileIncomplete(userDetails)) {
            log.info("프로필 불완전 사용자 → 프로필 완성 페이지로 이동: {}", userDetails.getUser().getEmail());
            return "/user/profile/complete";
        }

        // 3. SavedRequest 확인 (로그인 전에 가려던 페이지 복원)
        HttpSession session = request.getSession(false);
        if (session != null) {
            SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            if (savedRequest != null) {
                String targetUrl = savedRequest.getRedirectUrl();
                log.info("SavedRequest 감지: {}", targetUrl);

                // 로그인 페이지나 OAuth2 관련 주소는 제외
                if (!targetUrl.contains("/login") && !targetUrl.contains("/oauth2")) {
                    session.removeAttribute("SPRING_SECURITY_SAVED_REQUEST");
                    return targetUrl;
                }
            }
        }

        return "/";
    }

    /**
     * 회원 정보 필수값 누락 여부 확인
     */
    private boolean isProfileIncomplete(CustomUserDetails userDetails) {
        return userDetails.getUser().getName() == null ||
                userDetails.getUser().getName().trim().isEmpty() ||
                userDetails.getAge() == null ||
                userDetails.getGender() == null ||
                userDetails.getNickname() == null ||
                userDetails.getNickname().trim().isEmpty() ||
                userDetails.getUser().getDisease() == null ||
                userDetails.getUser().getDisability() == null ||
                userDetails.getUser().getMedication() == null;
    }
}
