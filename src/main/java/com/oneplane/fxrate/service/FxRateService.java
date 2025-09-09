package com.oneplane.fxrate.service;

import com.oneplane.fxrate.dao.FxRateMapper;
import com.oneplane.fxrate.domain.FxRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FxRateService {

    private final FxRateMapper fxRateMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String API_KEY = "FM8C7ym8k05QP6k7jbysogAPUygHEuvq";

    public void fetchAndSaveFxRates(Long countryId) {
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        String url = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON"
                + "?authkey=" + API_KEY
                + "&searchdate=" + today
                + "&data=AP01";

        log.debug("환율 API 호출 URL: {}", url);

        List<Map<String, Object>> response =
                restTemplate.getForObject(url, List.class);

        log.debug("환율 API 응답 데이터: {}", response);

        if (response != null) {
            for (Map<String, Object> item : response) {
                String curUnit = (String) item.get("cur_unit");
                String dealBasRStr = (String) item.get("deal_bas_r");
                String date = (String) item.get("date");

                log.info("통화: {}, 기준환율: {}, 날짜: {}",
                        curUnit, dealBasRStr, date);

                FxRate fxRate = new FxRate();
                fxRate.setCountryId(countryId);
                fxRate.setCurrencyCode((String) item.get("cur_unit"));
                fxRate.setDealBasR(Double.valueOf(((String) item.get("deal_bas_r")).replace(",", "")));
                fxRate.setBaseDate(LocalDate.parse((String) item.get("date"), DateTimeFormatter.ofPattern("yyyyMMdd")));

                fxRateMapper.insertFxRate(fxRate);

                log.debug("DB 저장 완료: {}", fxRate);
            }
        } else {
            log.warn("환율 API 응답이 null입니다.");
        }
    }

    public List<FxRate> getRecentRates(Long countryId) {
        return fxRateMapper.findRecentByCountry(countryId);
    }
}
