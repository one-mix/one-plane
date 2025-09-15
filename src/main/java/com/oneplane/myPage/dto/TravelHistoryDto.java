package com.oneplane.myPage.dto;

import org.springframework.web.multipart.MultipartFile;

public class TravelHistoryDto {

    private String countryCode;      // 국가명을 문자열로 입력받기 위함
    private String title;
    private String content;
    private String travelDate;
    private String city;
    private String travelPurpose;
    private String companion;
    private Integer rating;
    private MultipartFile travelImg;
    // 파일명 저장용 필드
    private String imagePath;

    // 생성자
    public TravelHistoryDto() {}

    // Getter & Setter
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTravelPurpose() {
        return travelPurpose;
    }

    public void setTravelPurpose(String travelPurpose) {
        this.travelPurpose = travelPurpose;
    }

    public String getCompanion() {
        return companion;
    }

    public void setCompanion(String companion) {
        this.companion = companion;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public MultipartFile getTravelImg() {
        return travelImg;
    }

    public void setTravelImg(MultipartFile travelImg) {
        this.travelImg = travelImg;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}