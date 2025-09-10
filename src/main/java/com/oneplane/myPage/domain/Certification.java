package com.oneplane.myPage.domain;

import java.util.Date;

public class Certification {
    private Long certificationId;
    private Long userId;
    private Long countryId;
    private String certificationImg;
    private Date certificationAt;
    private Date createdAt;

    // 생성자
    public Certification() {}

    // Getter & Setter
    public Long getCertificationId() { return certificationId; }
    public void setCertificationId(Long certificationId) { this.certificationId = certificationId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCountryId() { return countryId; }
    public void setCountryId(Long countryId) { this.countryId = countryId; }

    public String getCertificationImg() { return certificationImg; }
    public void setCertificationImg(String certificationImg) { this.certificationImg = certificationImg; }

    public Date getCertificationAt() { return certificationAt; }
    public void setCertificationAt(Date certificationAt) { this.certificationAt = certificationAt; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
