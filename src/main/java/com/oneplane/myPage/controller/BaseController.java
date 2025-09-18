package com.oneplane.myPage.controller;

import com.oneplane.myPage.dto.UserProfileDto;
import com.oneplane.myPage.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 작성자 : 허겸
 * 마이페이지 관련 컨트롤러들의 공통 기능을 제공하는 추상 베이스 컨트롤러
 * 모든 하위 컨트롤러에서 사용자 프로필 정보를 자동으로 모델에 추가
 */
public abstract class BaseController {

    // 사용자 프로필 관련 비즈니스 로직을 처리하는 서비스 의존성 주입
    @Autowired
    protected UserProfileService userProfileService;

    /**
     * 작성자 : 허겸
     * 모든 컨트롤러 메서드 실행 전에 공통으로 실행되는 메서드
     * 세션에서 사용자 ID를 가져와서 사용자 프로필 정보를 모델에 추가
     *
     * @param session HTTP 세션 객체 (사용자 ID 정보 포함)
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     */
    @ModelAttribute
    public void addCommonAttributes(HttpSession session, Model model) {
        // 세션에서 사용자 ID 추출
        Long userId = getUserIdFromSession(session);

        // 사용자 ID가 존재하는 경우에만 프로필 정보 조회 및 모델에 추가
        if (userId != null) {
            UserProfileDto userProfile = userProfileService.getUserInfo(userId); // 실제 메서드명 확인
            model.addAttribute("userProfile", userProfile);
        }
    }

    /**
     * 작성자 : 허겸
     * HTTP 세션에서 사용자 ID를 안전하게 추출하는 유틸리티 메서드
     * 다양한 타입(Long, Integer, String)으로 저장된 userId를 Long 타입으로 변환
     *
     * @param session HTTP 세션 객체
     * @return 변환된 사용자 ID (Long 타입) 또는 null (세션에 userId가 없는 경우)
     */
    protected Long getUserIdFromSession(HttpSession session) {
        // 세션에서 "userId" 속성 값 가져오기
        Object userIdObj = session.getAttribute("userId");

        // userId가 세션에 없는 경우 null 반환
        if (userIdObj == null) {
            return null;
        }

        // 이미 Long 타입인 경우 그대로 반환
        if (userIdObj instanceof Long) {
            return (Long) userIdObj;
        }

        // Integer 타입인 경우 Long으로 변환
        if (userIdObj instanceof Integer) {
            return ((Integer) userIdObj).longValue();
        }

        // String 타입인 경우 Long으로 파싱
        if (userIdObj instanceof String) {
            return Long.parseLong((String) userIdObj);
        }

        // 위의 타입들이 아닌 경우 null 반환 (예외 상황)
        return null;
    }
}
