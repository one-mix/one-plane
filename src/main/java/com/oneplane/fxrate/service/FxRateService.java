package com.oneplane.fxrate.service;

import com.oneplane.country.dao.CountryDao;
import com.oneplane.fxrate.repository.FxRateMapper;
import com.oneplane.fxrate.domain.FxRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FxRateService {

    private final FxRateMapper fxRateMapper;
    private final CountryDao countryDao; // country 테이블 조회용
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String API_KEY = "FM8C7ym8k05QP6k7jbysogAPUygHEuvq";

    @Transactional
    public void fetchAndSaveFxRates() {
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        String url = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON"
                + "?authkey=" + API_KEY
                + "&searchdate=" + today
                + "&data=AP01";

        log.debug("환율 API 호출 URL: {}", url);

        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

        if (response != null) {
            for (Map<String, Object> item : response) {
                String curUnit = (String) item.get("cur_unit");
                String dealBasRStr = (String) item.get("deal_bas_r");

                if (dealBasRStr == null || dealBasRStr.equals("-")) {
                    log.warn("{} 환율 데이터 없음, 스킵", curUnit);
                    continue;
                }

                // 통화 단위로 여러 국가 ID 조회
                List<Long> countryIds = countryDao.findCountryIdsByCurrency(curUnit);

                if (countryIds == null || countryIds.isEmpty()) {
                    log.warn("통화 {} 에 해당하는 countryId 없음", curUnit);
                    continue;
                }

                for (Long countryId : countryIds) {
                    FxRate fxRate = new FxRate();
                    fxRate.setCountryId(countryId);
                    fxRate.setCurrency(curUnit);
                    fxRate.setDealBasR(Double.valueOf(dealBasRStr.replace(",", "")));
                    fxRate.setBaseDate(LocalDate.parse(today, DateTimeFormatter.BASIC_ISO_DATE));

                    fxRateMapper.upsertFxRate(fxRate);
                    log.info("환율 저장 완료: {} → {}", curUnit, countryId);
                }
            }
        }
    }

    public List<FxRate> getRecentRates(Long countryId) {
        return fxRateMapper.findRecentByCountry(countryId);
    }
}

