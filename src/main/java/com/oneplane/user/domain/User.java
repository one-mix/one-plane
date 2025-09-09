package com.oneplane.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Integer user_id;
    private String email;
    private String name;
    private String nickname;
    private Integer age;
    private Role role;
    private Grade grade;
    private String gender;             // 성별 (true=남성, false=여성)
    private String disease;            // 질병 유무
    private String disability;         // 장애 유무
    private String medication;         // 복용약물 유무
    private String profileImg;          // 프로필 이미지 URL
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;    // 생성일시
    private LocalDateTime updatedAt;

    // 건강 정보를 보유한 사용자인지 확인
    public boolean hasHealthInfo() {
        return "true".equals(disease) ||
                "true".equals(disability) ||
                "true".equals(medication);
    }

    // 계정이 삭제되었는지 확인
    public boolean isDeleted() {
        return deletedAt != null;
    }

    // 프로필이 완성되었는지 확인
    public boolean isProfileComplete() {
        return name != null && !name.trim().isEmpty() &&
                nickname != null && !nickname.trim().isEmpty() &&
                age != null &&
                gender != null && !gender.trim().isEmpty() &&
                disease != null &&
                disability != null &&
                medication != null;
    }

    public boolean needsTravelCaution() {
        return hasHealthInfo() || (age != null && age >= 65);
    }

    public Boolean getDiseaseAsBoolean() {
        return "true".equals(disease);
    }

    public Boolean getDisabilityAsBoolean() {
        return "true".equals(disability);
    }

    public Boolean getMedicationAsBoolean() {
        return "true".equals(medication);
    }

    public String getGenderKorean() {
        if ("male".equals(gender)) {
            return "남성";
        } else if ("female".equals(gender)) {
            return "여성";
        }
        return "미설정";
    }
}
