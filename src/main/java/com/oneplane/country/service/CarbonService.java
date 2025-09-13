package com.oneplane.country.service;

import com.oneplane.country.dao.CarbonEmissionDao;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CarbonService {

    CarbonEmissionDao carbonEmissionDao;

    // 생성자 주입
    public CarbonService(CarbonEmissionDao carbonEmissionMapper) {
        this.carbonEmissionDao = carbonEmissionMapper;
    }

    /**
     * 특정 국가의 연도별 CO2 배출량 (단위: kt) 반환
     */
    public Map<String, Object> getCarbonByCountry(String isoCode, int year) {
        return carbonEmissionDao.findByCountryAndYear(isoCode, year);
    }
}
