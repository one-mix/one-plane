package com.oneplane.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    /** 사용자 고유 ID */
    private Integer userId;

    /** 이메일 (로그인 ID) */
    private String email;

    /** 실제 이름 */
    private String name;

    /** 닉네임 (화면 표시용) */
    private String nickname;

    /** 나이 */
    private Integer age;

    /** 사용자 역할 (예: ROLE_USER, ROLE_ADMIN) */
    private String role;

    /** 회원 등급 (예: BASIC, PREMIUM 등) */
    private String grade;

    /** 성별 (M / F) */
    private String gender;

    /** 질병 여부 (Y / N or 상세 값) */
    private String disease;

    /** 장애 여부 (Y / N) */
    private String disability;

    /** 약물 복용 여부 (Y / N) */
    private String medication;

    /** 프로필 이미지 URL */
    private String profileImg;

    /** 삭제 시각 (soft delete) */
    private LocalDateTime deletedAt;

    /** 생성 시각 */
    private LocalDateTime createdAt;

    /** 수정 시각 */
    private LocalDateTime updatedAt;
}