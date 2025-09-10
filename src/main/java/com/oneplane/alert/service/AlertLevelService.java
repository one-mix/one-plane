package com.oneplane.alert.service;

import com.oneplane.alert.domain.AlertLevel;
import com.oneplane.alert.dto.TravelWarningApiResponse;

import java.util.List;

public interface AlertLevelService {
    String determineLevelValue(TravelWarningApiResponse.Item item);
    void fetchAndSaveAlertLevels();

    List<AlertLevel> getAllAlerts();
}
