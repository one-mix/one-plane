package com.oneplane.fxrate.scheduler;

import com.oneplane.fxrate.service.FxRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FxRateScheduler {

    private final FxRateService fxRateService;

    @Scheduled(fixedRate = 10000)  // 10초마다 실행
    // @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void updateDailyFxRates() {
        log.info("매일 환율 자동 업데이트 시작");

        fxRateService.fetchAndSaveFxRates();

        log.info("매일 환율 자동 업데이트 완료");
    }
}
