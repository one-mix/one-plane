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

    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getDeletedAt() == null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

    public Integer getUserId() {
        return user.getUser_id();
    }

    public String getUserName() {
        return user.getName() != null ? user.getName() : "미설정";
    }

    public String getNickname() {
        return user.getNickname() != null ? user.getNickname() : "미설정";
    }

    public Integer getAge() {
        return user.getAge();
    }

    public String getGender() {
        return user.getGender();
    }

    public Role getRole() {
        return user.getRole();
    }

    public boolean isUser() {
        return user.getRole() == Role.ROLE_USER;
    }

    public boolean isAdmin() {
        return user.getRole() == Role.ROLE_ADMIN;
    }

    public boolean needsTravelCaution() {
        return hasHealthInfo() || (user.getAge() != null && user.getAge() >= 65);
    }

    public boolean hasHealthInfo() {
        return "true".equals(user.getDisease()) ||
                "true".equals(user.getDisability()) ||
                "true".equals(user.getMedication());
    }

    public String getProfileImg() {
        return user.getProfileImg();
    }

    public String getDisease() {
        return user.getDisease();
    }

    public String getDisability() {
        return user.getDisability();
    }

    public String getMedication() {
        return user.getMedication();
    }

    public com.oneplane.user.domain.Grade getGrade() {
        return user.getGrade();
    }

    public boolean getIsDeleted() {
        return user.getDeletedAt() != null;
    }

    // 프로필 완성여부
    public boolean isProfileComplete() {
        return user.isProfileComplete();
    }

    // 사용자 이메일 반환
    public String getEmail() {
        return user.getEmail();
    }

    // 건강 정보를 Boolean으로 반환하는 헬퍼 메서드
    public Boolean getDiseaseAsBoolean() {
        return "true".equals(user.getDisease());
    }

    public Boolean getDisabilityAsBoolean() {
        return "true".equals(user.getDisability());
    }

    public Boolean getMedicationAsBoolean() {
        return "true".equals(user.getMedication());
    }

    // 성별 한국어로 변환
    public String getGenderKorean() {
        if ("male".equals(user.getGender())) {
            return "남성";
        } else if ("female".equals(user.getGender())) {
            return "여성";
        }
        return "미설정";
    }

    public String getUserSummary() {
        return String.format("User{id=%d, email='%s', name='%s', nickname='%s', role=%s}",
                getUserId(), getEmail(), getUserName(), getNickname(), getRole());
    }
}