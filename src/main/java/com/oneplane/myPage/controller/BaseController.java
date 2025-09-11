package com.oneplane.myPage.controller;

import com.oneplane.myPage.dto.UserProfileDto;
import com.oneplane.myPage.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class BaseController {

    @Autowired
    protected UserProfileService userProfileService;

    @ModelAttribute
    public void addCommonAttributes(HttpSession session, Model model) {
        Long userId = getUserIdFromSession(session);
        if (userId != null) {
            UserProfileDto userProfile = userProfileService.getUserInfo(userId); // 실제 메서드명 확인
            model.addAttribute("userProfile", userProfile);
        }
    }

    protected Long getUserIdFromSession(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            return null;
        }
        if (userIdObj instanceof Long) {
            return (Long) userIdObj;
        }
        if (userIdObj instanceof Integer) {
            return ((Integer) userIdObj).longValue();
        }
        if (userIdObj instanceof String) {
            return Long.parseLong((String) userIdObj);
        }
        return null;
    }
}
