// 작성자: 김동현
package com.oneplane.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUser {
    private Integer user_id;
    private String email;
    private String name;
    private String nickname;
    private Integer age;
    private String role;
    private String grade;           // ECONOMY, STANDARD, BUSINESS, FIRST, ELITE
    private String gender;          // male, female
    private String disease;         // true, false
    private String disability;      // true, false
    private String medication;      // true, false
    private String profileImg;      // 프로필 이미지 URL
    private LocalDateTime createdAt;    // 생성일시
    private LocalDateTime updatedAt;    // 수정일시
    private LocalDateTime deletedAt;    // 삭제일시

    /**
     * 계정이 삭제되었는지 확인
     * 작성자 : 김동현
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * 관리자인지 확인
     * 작성자 : 김동현
     */
    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(role);
    }

    /**
     * 일반 사용자인지 확인
     * 작성자 : 김동현
     */
    public boolean isUser() {
        return "ROLE_USER".equals(role);
    }

    /**
     * 프로필이 완성되었는지 확인
     * 작성자 : 김동현
     */
    public boolean isProfileComplete() {
        return name != null && !name.trim().isEmpty() &&
                nickname != null && !nickname.trim().isEmpty() &&
                age != null &&
                gender != null && !gender.trim().isEmpty() &&
                disease != null &&
                disability != null &&
                medication != null;
    }

    /**
     * 건강 정보를 보유한 사용자인지 확인
     * 작성자 : 김동현
     */
    public boolean hasHealthInfo() {
        return "true".equals(disease) ||
                "true".equals(disability) ||
                "true".equals(medication);
    }

    /**
     * 여행 시 주의가 필요한 사용자인지 확인
     * 작성자 : 김동현
     */
    public boolean needsTravelCaution() {
        return hasHealthInfo() || (age != null && age >= 65);
    }

    /**
     * 한국어 성별 반환
     * 작성자 : 김동현
     */
    public String getGenderKorean() {
        if ("male".equals(gender)) {
            return "남성";
        } else if ("female".equals(gender)) {
            return "여성";
        }
        return "미설정";
    }

    /**
     * 한국어 역할 반환
     * 작성자 : 김동현
     */
    public String getRoleKorean() {
        if ("ROLE_ADMIN".equals(role)) {
            return "관리자";
        } else if ("ROLE_USER".equals(role)) {
            return "사용자";
        }
        return "미설정";
    }

    /**
     * 등급을 한국어로 반환
     * 작성자 : 김동현
     */
    public String getGradeKorean() {
        if (grade == null) return "미설정";

        switch (grade) {
            case "ECONOMY": return "이코노미";
            case "STANDARD": return "스탠다드";
            case "BUSINESS": return "비즈니스";
            case "FIRST": return "퍼스트";
            case "ELITE": return "엘리트";
            default: return "미설정";
        }
    }

    /**
     * Boolean 타입으로 질병 여부 반환
     * 작성자 : 김동현
     */
    public Boolean getDiseaseAsBoolean() {
        return "true".equals(disease);
    }

    /**
     * Boolean 타입으로 장애 여부 반환
     * 작성자 : 김동현
     */
    public Boolean getDisabilityAsBoolean() {
        return "true".equals(disability);
    }

    /**
     * Boolean 타입으로 복용약물 여부 반환
     * 작성자 : 김동현
     */
    public Boolean getMedicationAsBoolean() {
        return "true".equals(medication);
    }

    /**
     * 건강 정보 요약
     * 작성자 : 김동현
     */
    public String getHealthSummary() {
        if (!hasHealthInfo()) {
            return "건강 정보 없음";
        }

        StringBuilder summary = new StringBuilder();
        if (getDiseaseAsBoolean()) summary.append("질병유 ");
        if (getDisabilityAsBoolean()) summary.append("장애유 ");
        if (getMedicationAsBoolean()) summary.append("복용약물유");

        return summary.toString().trim();
    }

    /**
     * JSP에서 admin 속성 접근을 위한 getter
     * 작성자 : 김동현
     */
    public boolean getAdmin() {
        return isAdmin();
    }
}