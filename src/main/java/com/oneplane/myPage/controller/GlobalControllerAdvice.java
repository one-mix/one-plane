package com.oneplane.myPage.controller;

import com.oneplane.myPage.dto.UserProfileDto;
import com.oneplane.myPage.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 모든 컨트롤러에 공통으로 적용되는 Advice
 * 로그인된 사용자의 프로필 정보를 세션에 캐시하고 뷰에 제공
 *
 * @author 허겸
 */
@ControllerAdvice(basePackages = "com.oneplane")
public class GlobalControllerAdvice {

    @Autowired
    private UserProfileService userProfileService;

    /**
     * 모든 뷰에서 사용할 수 있도록 로그인 사용자의 프로필을 Model에 추가
     * 세션에 이미 저장된 프로필이 있으면 재조회 없이 캐시 사용
     *
     * @param session HTTP 세션 객체 (userId, userProfile 속성 확인)
     * @return UserProfileDto—로그인 전이면 빈 DTO, 로그인 후면 사용자 정보
     */
    @ModelAttribute("userProfile")
    public UserProfileDto addUserProfile(HttpSession session) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            // 로그인 전 상태: 빈 프로필 반환
            return new UserProfileDto();
        }

        // 세션에 이미 저장된 프로필이 있으면 재사용
        UserProfileDto profile = (UserProfileDto) session.getAttribute("userProfile");
        if (profile != null) {
            return profile;
        }

        // DB에서 사용자 정보 조회 후 세션에 저장
        profile = userProfileService.getUserInfo(userId);
        session.setAttribute("userProfile", profile);
        return profile;
    }

    /**
     * 세션에서 userId를 안전하게 파싱하여 반환
     * Integer, Long, String 타입 모두 처리
     *
     * @param session HTTP 세션 객체
     * @return Long userId 또는 null
     */
    private Long getUserIdFromSession(HttpSession session) {
        Object obj = session.getAttribute("userId");
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        }
        if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException ignored) {
                // 파싱 실패 시 null 반환
            }
        }
        return null;
    }
}
