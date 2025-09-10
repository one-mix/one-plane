package com.oneplane.myPage.controller;

import com.oneplane.myPage.dto.UserProfileDto;
import com.oneplane.myPage.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// 모든 패키지의 컨트롤러에 적용되도록 basePackages 추가
@ControllerAdvice(basePackages = "com.oneplane")
public class GlobalControllerAdvice {

    @Autowired
    private UserProfileService userProfileService;

    @ModelAttribute("userProfile")
    public UserProfileDto addUserProfile(HttpSession session) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return new UserProfileDto();
        }

        UserProfileDto profile = (UserProfileDto) session.getAttribute("userProfile");
        if (profile != null) {
            return profile;
        }

        profile = userProfileService.getUserInfo(userId);
        session.setAttribute("userProfile", profile);
        return profile;
    }

    private Long getUserIdFromSession(HttpSession session) {
        Object obj = session.getAttribute("userId");
        if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        } else if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }
}
