package com.oneplane.myPage.dto;

import org.springframework.web.multipart.MultipartFile;

public class CertificationDto {
    private String countryName;
    private String certificationDate; // 이 필드명이 중요!
    private MultipartFile certificationImg;
    private String certificationImgPath;

    // 생성자
    public CertificationDto() {}

    // Getter & Setter
    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }

    public String getCertificationDate() { return certificationDate; }
    public void setCertificationDate(String certificationDate) { this.certificationDate = certificationDate; }

    public MultipartFile getCertificationImg() { return certificationImg; }
    public void setCertificationImg(MultipartFile certificationImg) { this.certificationImg = certificationImg; }

    public String getCertificationImgPath() { return certificationImgPath; }
    public void setCertificationImgPath(String certificationImgPath) { this.certificationImgPath = certificationImgPath; }
}
