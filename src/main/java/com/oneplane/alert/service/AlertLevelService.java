package com.oneplane.alert.service;

import com.oneplane.alert.dto.TravelWarningApiResponse;

public interface AlertLevelService {
    String determineLevelValue(TravelWarningApiResponse.Item item);
    void fetchAndSaveAlertLevels();
}
