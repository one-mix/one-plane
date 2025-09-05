package com.oneplane.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 10, message = "이름은 2~10글자로 입력해주세요.")
    private String name;

    @Size(min = 2, max = 10, message = "닉네임은 2~10글자로 입력해주세요.")
    private String nickname;

    @NotNull(message = "나이를 입력해주세요.")
    @Min(value = 1, message = "올바른 나이를 입력해주세요.")
    @Max(value = 120, message = "올바른 나이를 입력해주세요.")
    private Integer age;

    @NotNull(message = "성별을 선택해주세요.")
    private String gender; // true: 남성, false: 여성

    @NotNull(message = "질병 여부를 선택해주세요.")
    private String disease;

    @NotNull(message = "장애 여부를 선택해주세요.")
    private String disability;

    @NotNull(message = "복용약물 여부를 선택해주세요.")
    private String medication;

    private String email;
    private String profileImg;

    /**
     * DTO를 User 엔티티로 변환
     */
    public com.oneplane.user.domain.User toUser() {
        return com.oneplane.user.domain.User.builder()
                .name(this.name)
                .nickname(this.nickname)
                .age(this.age)
                .gender(this.gender)
                .disease(this.disease)
                .disability(this.disability)
                .medication(this.medication)
                .email(this.email)
                .profileImg(this.profileImg)
                .role(com.oneplane.user.domain.Role.ROLE_USER) // 기본 역할
                .grade(com.oneplane.user.domain.Grade.ECONOMY) // 기본 등급
                .build();
    }

    /**
     * 건강 정보 보유 여부 확인
     */
    public boolean hasHealthInfo() {
        return Boolean.TRUE.equals(disease) ||
                Boolean.TRUE.equals(disability) ||
                Boolean.TRUE.equals(medication);
    }

}
