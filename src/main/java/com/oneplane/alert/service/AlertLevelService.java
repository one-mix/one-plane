package com.oneplane.alert.service;

<<<<<<< HEAD
import com.oneplane.alert.domain.AlertLevel;
import java.util.List;

public interface AlertLevelService {
    AlertLevel getAlertByCountryId(Long countryId);
    void saveAlert(AlertLevel alertLevel);

    List<AlertLevel> getAllAlerts();
=======
import com.oneplane.alert.dto.TravelWarningApiResponse;

public interface AlertLevelService {
    String determineLevelValue(TravelWarningApiResponse.Item item);
    void fetchAndSaveAlertLevels();
>>>>>>> dev
}
