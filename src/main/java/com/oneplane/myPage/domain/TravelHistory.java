package com.oneplane.myPage.domain;

import java.time.LocalDate;
import java.util.Date;

public class TravelHistory {
    private Long travelHistoryId;
    private Long userId;
    private Long countryId;
    private String title;
    private String content;
    private Date travelAt;
    private byte[] travelImg;
    private String city;
    private String travelPurpose;
    private String companion;
    private int rating;
    private LocalDate deletedAt;

    //새로 추가
    private String imagePath;

    // 생성자
    public TravelHistory() {}

    // Getter & Setter (기존과 동일)
    public Long getTravelHistoryId() { return travelHistoryId; }
    public void setTravelHistoryId(Long travelHistoryId) { this.travelHistoryId = travelHistoryId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCountryId() { return countryId; }
    public void setCountryId(Long countryId) { this.countryId = countryId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getTravelAt() { return travelAt; }
    public void setTravelAt(Date travelAt) { this.travelAt = travelAt; }

    public byte[] getTravelImg() { return travelImg; }
    public void setTravelImg(byte[] travelImg) { this.travelImg = travelImg; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getTravelPurpose() { return travelPurpose; }
    public void setTravelPurpose(String travelPurpose) { this.travelPurpose = travelPurpose; }

    public String getCompanion() { return companion; }
    public void setCompanion(String companion) { this.companion = companion; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public LocalDate getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDate deletedAt) { this.deletedAt = deletedAt; }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
