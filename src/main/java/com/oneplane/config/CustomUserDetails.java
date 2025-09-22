// 작성자: 김동현
package com.oneplane.config;

import com.oneplane.user.domain.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.oneplane.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails, OAuth2User {
    private final User user;
    private Map<String, Object> attributes;

    /**
     * OAuth2 속성을 포함한 생성자
     * 작성자 : 김동현
     */
    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    /**
     * 사용자 권한 목록 반환
     * 작성자 : 김동현
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    /**
     * 비밀번호 반환 (OAuth2에서는 사용하지 않음)
     * 작성자 : 김동현
     */
    @Override
    public String getPassword() {
        return null;
    }

    /**
     * 사용자명(이메일) 반환
     * 작성자 : 김동현
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * 계정 만료 여부
     * 작성자 : 김동현
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠금 여부
     * 작성자 : 김동현
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명 만료 여부
     * 작성자 : 김동현
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부
     * 작성자 : 김동현
     */
    @Override
    public boolean isEnabled() {
        return user.getDeletedAt() == null;
    }

    /**
     * OAuth2 속성 반환
     * 작성자 : 김동현
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * OAuth2 사용자 이름 반환
     * 작성자 : 김동현
     */
    @Override
    public String getName() {
        return user.getEmail();
    }

    /**
     * 사용자 ID 반환
     * 작성자 : 김동현
     */
    public Integer getUserId() {
        return user.getUser_id();
    }

    /**
     * 사용자 이름 반환
     * 작성자 : 김동현
     */
    public String getUserName() {
        return user.getName() != null ? user.getName() : "미설정";
    }

    /**
     * 닉네임 반환
     * 작성자 : 김동현
     */
    public String getNickname() {
        return user.getNickname() != null ? user.getNickname() : "미설정";
    }

    /**
     * 나이 반환
     * 작성자 : 김동현
     */
    public Integer getAge() {
        return user.getAge();
    }

    /**
     * 성별 반환
     * 작성자 : 김동현
     */
    public String getGender() {
        return user.getGender();
    }

    /**
     * 사용자 역할 반환
     * 작성자 : 김동현
     */
    public Role getRole() {
        return user.getRole();
    }

    /**
     * 일반 사용자 여부 확인
     * 작성자 : 김동현
     */
    public boolean isUser() {
        return user.getRole() == Role.ROLE_USER;
    }

    /**
     * 관리자 여부 확인
     * 작성자 : 김동현
     */
    public boolean isAdmin() {
        return user.getRole() == Role.ROLE_ADMIN;
    }

    /**
     * 여행시 주의가 필요한 사용자인지 확인
     * 작성자 : 김동현
     */
    public boolean needsTravelCaution() {
        return hasHealthInfo() || (user.getAge() != null && user.getAge() >= 65);
    }

    /**
     * 건강 정보 보유 여부 확인
     * 작성자 : 김동현
     */
    public boolean hasHealthInfo() {
        return "true".equals(user.getDisease()) ||
                "true".equals(user.getDisability()) ||
                "true".equals(user.getMedication());
    }

    /**
     * 프로필 이미지 반환
     * 작성자 : 김동현
     */
    public String getProfileImg() {
        return user.getProfileImg();
    }

    /**
     * 질병 정보 반환
     * 작성자 : 김동현
     */
    public String getDisease() {
        return user.getDisease();
    }

    /**
     * 장애 정보 반환
     * 작성자 : 김동현
     */
    public String getDisability() {
        return user.getDisability();
    }

    /**
     * 복용약물 정보 반환
     * 작성자 : 김동현
     */
    public String getMedication() {
        return user.getMedication();
    }

    /**
     * 사용자 등급 반환
     * 작성자 : 김동현
     */
    public com.oneplane.user.domain.Grade getGrade() {
        return user.getGrade();
    }

    /**
     * 삭제 여부 반환
     * 작성자 : 김동현
     */
    public boolean getIsDeleted() {
        return user.getDeletedAt() != null;
    }

    /**
     * 프로필 완성여부 확인
     * 작성자 : 김동현
     */
    public boolean isProfileComplete() {
        return user.isProfileComplete();
    }

    /**
     * 사용자 이메일 반환
     * 작성자 : 김동현
     */
    public String getEmail() {
        return user.getEmail();
    }

    /**
     * 질병 정보를 Boolean으로 반환하는 헬퍼 메서드
     * 작성자 : 김동현
     */
    public Boolean getDiseaseAsBoolean() {
        return "true".equals(user.getDisease());
    }

    /**
     * 장애 정보를 Boolean으로 반환하는 헬퍼 메서드
     * 작성자 : 김동현
     */
    public Boolean getDisabilityAsBoolean() {
        return "true".equals(user.getDisability());
    }

    /**
     * 복용약물 정보를 Boolean으로 반환하는 헬퍼 메서드
     * 작성자 : 김동현
     */
    public Boolean getMedicationAsBoolean() {
        return "true".equals(user.getMedication());
    }

    /**
     * 성별 한국어로 변환
     * 작성자 : 김동현
     */
    public String getGenderKorean() {
        if ("male".equals(user.getGender())) {
            return "남성";
        } else if ("female".equals(user.getGender())) {
            return "여성";
        }
        return "미설정";
    }

    /**
     * 사용자 요약 정보 반환
     * 작성자 : 김동현
     */
    public String getUserSummary() {
        return String.format("User{id=%d, email='%s', name='%s', nickname='%s', role=%s}",
                getUserId(), getEmail(), getUserName(), getNickname(), getRole());
    }
}