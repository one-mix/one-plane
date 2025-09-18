// 작성자: 오수경
package com.oneplane.country.service;

import com.oneplane.country.dao.CarbonEmissionDao;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * CarbonService
 * - 국가별 연도별 CO₂ 배출량을 조회하는 서비스 계층
 * - DAO(CarbonEmissionDao)를 통해 DB 접근
 */
@Service
public class CarbonService {

    private final CarbonEmissionDao carbonEmissionDao;

    /** DAO 주입을 통한 생성자 기반 의존성 주입 */
    public CarbonService(CarbonEmissionDao carbonEmissionMapper) {
        this.carbonEmissionDao = carbonEmissionMapper;
    }

    /**
     * 특정 국가의 특정 연도 CO₂ 배출량 조회
     * @param isoCode ISO 3자리 국가 코드 (예: "KOR")
     * @param year    조회 연도
     * @return 배출량 정보(Map) - {"year":…, "emission":…} 형태
     */
    public Map<String, Object> getCarbonByCountry(String isoCode, int year) {
        return carbonEmissionDao.findByCountryAndYear(isoCode, year);
    }
}
