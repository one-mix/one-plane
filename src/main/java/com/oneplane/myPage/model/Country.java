package com.oneplane.myPage.model;

public class Country {
    private Long countryId;
    private String countryName;
    private String countryEnName;
    private String isoCode;
    private String continent;
    private String img;
    private Double latitude;
    private Double longitude;
    private String currency;
    private Integer distance;

    // 생성자
    public Country() {}

    // Getter & Setter
    public Long getCountryId() { return countryId; }
    public void setCountryId(Long countryId) { this.countryId = countryId; }

    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }

    public String getCountryEnName() { return countryEnName; }
    public void setCountryEnName(String countryEnName) { this.countryEnName = countryEnName; }

    public String getIsoCode() { return isoCode; }
    public void setIsoCode(String isoCode) { this.isoCode = isoCode; }

    public String getContinent() { return continent; }
    public void setContinent(String continent) { this.continent = continent; }

    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Integer getDistance() { return distance; }
    public void setDistance(Integer distance) { this.distance = distance; }
}
