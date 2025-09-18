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

/**
 * 사용자 프로필 관리 컨트롤러
 * 프로필 조회, 수정 폼 제공 및 AJAX 프로필 이미지 업로드 기능 제공
 *
 * @author 허겸
 */
@Controller
public class UserProfileController extends BaseController {

    @Autowired
    private UserProfileService userProfileService;

    /**
     * 프로필 수정 폼을 표시
     * 로그인 확인 후 세션에 저장된 userId를 기반으로 사용자 정보 조회
     *
     * @param session HTTP 세션 (userId 확인용)
     * @param model  뷰에 전달할 데이터 모델
     * @return 프로필 수정 페이지 또는 로그인 리다이렉트
     */
    @GetMapping("/edit")
    public String showEditForm(HttpSession session, Model model) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            // 로그인되지 않은 경우 로그인 페이지로 이동
            return "redirect:/login";
        }

        // DB에서 사용자 프로필 정보 조회
        UserProfileDto profile = userProfileService.getUserInfo(userId);
        model.addAttribute("profile", profile);                  // 기존 프로필 정보
        model.addAttribute("contentPage", "mypage/profile/edit.jsp"); // JSP 경로
        model.addAttribute("activeMenu", "profile");           // 사이드바 메뉴 활성화
        model.addAttribute("showSidebar", true);                 // 사이드바 표시 여부
        model.addAttribute("pageTitle", "프로필 수정");         // 페이지 제목
        return "layout/layout";                                 // 레이아웃 템플릿 반환
    }

    /**
     * AJAX 요청 처리: 프로필 이미지 업로드
     * MultipartFile로 전달된 이미지를 서비스로 전달하여 저장하고 세션 갱신
     *
     * @param file    업로드된 이미지 파일
     * @param session HTTP 세션 (userId 확인 및 프로필 갱신)
     * @return 성공 시 "success", 실패 시 HTTP 상태 코드와 "fail" 메시지
     */
    @PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("profileImage") MultipartFile file,
            HttpSession session) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            // 인증되지 않은 사용자
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("fail");
        }

        // 서비스 호출로 프로필 이미지 저장
        boolean success = userProfileService.updateProfileImage(userId, file);
        if (success) {
            // 세션에 저장된 프로필 정보 갱신
            session.setAttribute("userProfile", userProfileService.getUserInfo(userId));
            return ResponseEntity.ok("success");
        }

        // 저장 실패 시 서버 에러 응답
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
    }

    /**
     * 세션에서 userId 추출 헬퍼 메서드
     * 다양한 타입(Long, Integer, String) 지원
     *
     * @param session HTTP 세션
     * @return Long 타입 userId 또는 null
     */
    public Long getUserIdFromSession(HttpSession session) {
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