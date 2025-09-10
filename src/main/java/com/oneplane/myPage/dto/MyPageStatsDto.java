package com.oneplane.myPage.dto;

public class MyPageStatsDto {
    private int certificationCount;
    private int totalDistance;
    private int travelHistoryCount;

    // 생성자
    public MyPageStatsDto() {}

    public MyPageStatsDto(int certificationCount, int totalDistance) {
        this.certificationCount = certificationCount;
        this.totalDistance = totalDistance;
        this.travelHistoryCount = travelHistoryCount;
    }

    // Getter & Setter
    public int getCertificationCount() { return certificationCount; }
    public void setCertificationCount(int certificationCount) { this.certificationCount = certificationCount; }

    public int getTotalDistance() { return totalDistance; }
    public void setTotalDistance(int totalDistance) { this.totalDistance = totalDistance; }

    public int getTravelHistoryCount() { return travelHistoryCount; }
    public void setTravelHistoryCount(int travelHistoryCount) { this.travelHistoryCount = travelHistoryCount; }
}
