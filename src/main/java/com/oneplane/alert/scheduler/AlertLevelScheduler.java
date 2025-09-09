package com.oneplane.alert.scheduler;

import com.oneplane.alert.service.AlertLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlertLevelScheduler {

    private final AlertLevelService alertLevelService;

    public AlertLevelScheduler(AlertLevelService alertLevelService) {
        this.alertLevelService = alertLevelService;
    }

    // 24시간마다 실행
    @Scheduled(fixedRate = 86400000)
    public void fetchAndStoreAlertLevels() {
        alertLevelService.fetchAndSaveAlertLevels();
    }
}
