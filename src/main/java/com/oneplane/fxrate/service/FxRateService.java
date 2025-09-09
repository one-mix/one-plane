package com.oneplane.fxrate.service;

import com.oneplane.country.dao.CountryDao;
import com.oneplane.fxrate.repository.FxRateMapper;
import com.oneplane.fxrate.domain.FxRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FxRateService {

    private final FxRateMapper fxRateMapper;
    private final CountryDao countryDao;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String API_KEY = "FM8C7ym8k05QP6k7jbysogAPUygHEuvq";

    /**
     * 최근 7일치 환율을 API에서 가져와 DB에 저장
     */
    @Transactional
    public void fetchAndSaveFxRates() throws ParseException {
        for (int i = 0; i < 7; i++) { // 최근 7일치
            String date = LocalDate.now(ZoneId.of("Asia/Seoul"))
                    .minusDays(i)
                    .format(DateTimeFormatter.BASIC_ISO_DATE);

            String url = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON"
                    + "?authkey=" + API_KEY
                    + "&searchdate=" + date
                    + "&data=AP01";

            List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
            if (response == null || response.isEmpty()) continue;

            for (Map<String, Object> item : response) {
                String curUnit = (String) item.get("cur_unit");
                String dealBasRStr = (String) item.get("deal_bas_r");
                if (dealBasRStr == null || dealBasRStr.equals("-")) continue;

                Date parsedDate = new SimpleDateFormat("yyyyMMdd").parse(date);

                List<Long> countryIds = countryDao.findCountryIdsByCurrency(curUnit);
                for (Long countryId : countryIds) {
                    FxRate fxRate = new FxRate();
                    fxRate.setCountryId(countryId);
                    fxRate.setCurrency(curUnit);
                    fxRate.setDealBasR(Double.valueOf(dealBasRStr.replace(",", "")));
                    fxRate.setBaseDate(parsedDate);

                    fxRateMapper.upsertFxRate(fxRate);
                }
            }
        }
    }

    /**
     * 최근 환율 데이터 조회 (최근 7일 기준)
     */
    public List<FxRate> getRecentRates(Long countryId) {
        List<FxRate> rates = fxRateMapper.findRecentByCountry(countryId);
        log.debug("최근 환율 조회 결과 (countryId={}): {}", countryId, rates);
        return rates;
    }
}
