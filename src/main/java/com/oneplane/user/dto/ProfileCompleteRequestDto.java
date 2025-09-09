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
public class ProfileCompleteRequestDto {

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 10, message = "이름은 2~10글자로 입력해주세요.")
    private String name;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 10, message = "닉네임은 2~10글자로 입력해주세요.")
    private String nickname;

    @NotNull(message = "나이를 입력해주세요.")
    @Min(value = 1, message = "올바른 나이를 입력해주세요.")
    @Max(value = 120, message = "올바른 나이를 입력해주세요.")
    private Integer age;

    @NotBlank(message = "성별을 선택해주세요.")
    private String gender; // "male" 또는 "female"

    @NotNull(message = "질병 여부를 선택해주세요.")
    private Boolean disease;

    @NotNull(message = "장애 여부를 선택해주세요.")
    private Boolean disability;

    @NotNull(message = "복용약물 여부를 선택해주세요.")
    private Boolean medication;

    /**
     * 건강 정보 보유 여부 확인
     */
    public boolean hasHealthInfo() {
        return Boolean.TRUE.equals(disease) ||
                Boolean.TRUE.equals(disability) ||
                Boolean.TRUE.equals(medication);
    }

    /**
     * 여행 시 주의가 필요한 사용자인지 확인
     */
    public boolean needsTravelCaution() {
        return hasHealthInfo() || (age != null && age >= 65);
    }

    /**
     * DTO 유효성 검증을 위한 커스텀 검증 메서드
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
                nickname != null && !nickname.trim().isEmpty() &&
                age != null && age >= 1 && age <= 120 &&
                gender != null && (gender.equals("male") || gender.equals("female")) &&
                disease != null && disability != null && medication != null;
    }
}