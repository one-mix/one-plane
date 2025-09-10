package com.oneplane.myPage.dto;

public class UserProfileDto {
    private String nickname;
    private String name;
    private Integer age;
    private String gender;
    private String profileImagePath;

    // 생성자
    public UserProfileDto() {}

    public UserProfileDto(String nickname, String profileImagePath) {
        this.nickname = nickname;
        this.profileImagePath = profileImagePath;
    }

    // Getter & Setter
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getProfileImagePath() { return profileImagePath; }
    public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }
}
