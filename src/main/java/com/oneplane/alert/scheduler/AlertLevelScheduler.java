//작성자: 방대혁
package com.oneplane.alert.scheduler;

import com.oneplane.alert.service.AlertLevelService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * AlertLevelScheduler
 * - 공공데이터포털 여행경보 API를 주기적으로 호출하여
 *   최신 여행경보 데이터를 DB에 반영하는 스케줄러
 * - Spring @Scheduled 애노테이션을 사용하여 24시간마다 실행됨
 */
@Component
public class AlertLevelScheduler {

    private final AlertLevelService alertLevelService;

    public AlertLevelScheduler(AlertLevelService alertLevelService) {
        this.alertLevelService = alertLevelService;
    }

    /**
     * 여행경보 데이터 갱신 작업
     * - 24시간(86400000ms)마다 실행
     * - AlertLevelService를 통해 API 호출 → DB 저장
     */
    @Scheduled(fixedRate = 86400000) // 24시간마다 반복 실행
    public void fetchAndStoreAlertLevels() {
        alertLevelService.fetchAndSaveAlertLevels();
    }
}
