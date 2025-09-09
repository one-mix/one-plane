package com.oneplane.fxrate.controller;

import com.oneplane.fxrate.domain.FxRate;
import com.oneplane.fxrate.service.FxRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fx")
public class FxRateController {

    private final FxRateService fxRateService;

    // 환율 API 호출 + DB 저장
    @PostMapping("/update/{countryId}")
    public String updateFxRates(@PathVariable Long countryId) throws ParseException {
        fxRateService.fetchAndSaveFxRates();
        return "환율 업데이트 완료";
    }

    // 최근 환율 조회
    @GetMapping("/{countryId}")
    public List<FxRate> getFxRates(@PathVariable Long countryId) {
        return fxRateService.getRecentRates(countryId);
    }
}
