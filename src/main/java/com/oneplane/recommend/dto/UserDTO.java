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
    private Integer userId;
    private String email;
    private String name;
    private String nickname;
    private Integer age;
    private String role;
    private String grade;
    private String gender;
    private String disease;
    private String disability;
    private String medication;
    private String profileImg;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}