package com.oneplane.recommend.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long userId;
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
}