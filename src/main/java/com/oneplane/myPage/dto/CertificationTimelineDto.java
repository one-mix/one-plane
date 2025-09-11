package com.oneplane.myPage.dto;

public class CertificationTimelineDto {

    private String certificationDate;     // 시작 날짜
    private String countryFlagUrl;        // 국기 이미지 URL
    private int distance;                 // 해당 인증의 개별 이동 거리
    private int cumulativeDistance;       // 누적 이동 거리

    public String getCertificationDate() {
        return certificationDate;
    }

    public void setCertificationDate(String certificationDate) {
        this.certificationDate = certificationDate;
    }

    public String getCountryFlagUrl() {
        return countryFlagUrl;
    }

    public void setCountryFlagUrl(String countryFlagUrl) {
        this.countryFlagUrl = countryFlagUrl;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getCumulativeDistance() {
        return cumulativeDistance;
    }

    public void setCumulativeDistance(int cumulativeDistance) {
        this.cumulativeDistance = cumulativeDistance;
    }
}
