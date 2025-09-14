// com.oneplane.user.domain.UserProfile.java

package com.oneplane.userprofile.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersProfile {
    // 출력용 필드
    private Long userId;            // 회원 식별자
    private String name;            // 이름
    private String nickname;        // 닉네임
    private Integer age;            // 나이
    private String grade;           // 등급 (ECONOMY, STANDARD, …)
    private String gender;          // 성별 (male, female)
    private String email;           // 이메일
    private String disease;         // 질병 (true/false)
    private String disability;      // 장애 (true/false)
    private String medication;      // 복용약 (true/false)

    // 읽기 전용 필드 (계산/조회용)
    private Integer totalCount;     // 다녀온 국가 수
    private BigDecimal totalDistance; // 총 이동 거리
    private LocalDate createdAt;    // 가입일
    private LocalDate updatedAt;    // 최종 수정일



}
