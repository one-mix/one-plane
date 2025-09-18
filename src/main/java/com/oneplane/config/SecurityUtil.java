// 작성자: 김동현
package com.oneplane.config;

import com.oneplane.user.domain.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {
    }

    /**
     * 현재 로그인한 사용자 ID 반환
     * 작성자 : 김동현
     */
    public static Integer getCurrentUserId() {
        return getCurrentUserDetails().getUserId();
    }

    /**
     * 현재 로그인한 사용자 이름 반환
     * 작성자 : 김동현
     */
    public static String getCurrentUserName() {
        return getCurrentUserDetails().getUserName();
    }

    /**
     * 현재 로그인한 사용자 닉네임 반환
     * 작성자 : 김동현
     */
    public static String getCurrentUserNickname() {
        return getCurrentUserDetails().getNickname();
    }

    /**
     * 현재 로그인한 사용자 역할 반환
     * 작성자 : 김동현
     */
    public static Role getCurrentUserRole() {
        return getCurrentUserDetails().getRole();
    }

    /**
     * 현재 사용자가 일반 사용자인지 확인
     * 작성자 : 김동현
     */
    public static boolean isCurrentUserNormalUser() {
        return Role.ROLE_USER.equals(getCurrentUserRole());
    }

    /**
     * 현재 사용자가 관리자인지 확인
     * 작성자 : 김동현
     */
    public static boolean isCurrentUserAdmin() {
        return getCurrentUserDetails().isAdmin();
    }

    /**
     * 현재 로그인한 사용자의 상세 정보 반환
     * 작성자 : 김동현
     */
    public static CustomUserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new RuntimeException("잘못된 인증 정보입니다.");
        }

        return (CustomUserDetails) principal;
    }

    /**
     * 사용자가 로그인되어 있는지 확인
     * 작성자 : 김동현
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
                authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof CustomUserDetails;
    }

    /**
     * 특정 사용자의 소유권을 확인
     * 작성자 : 김동현
     */
    public static boolean isOwnerOrAdmin(String targetUserId) {
        if (!isAuthenticated()) {
            return false;
        }

        String currentUserId = String.valueOf(getCurrentUserId());
        return currentUserId.equals(targetUserId) || isCurrentUserAdmin();
    }

    /**
     * 현재 사용자가 여행시 주의가 필요한지 확인
     * 작성자 : 김동현
     */
    public static boolean currentUserNeedsTravelCaution() {
        return getCurrentUserDetails().needsTravelCaution();
    }

    /**
     * 현재 사용자의 건강 정보 보유 여부를 확인
     * 작성자 : 김동현
     */
    public static boolean currentUserHasHealthInfo() {
        return getCurrentUserDetails().hasHealthInfo();
    }
}