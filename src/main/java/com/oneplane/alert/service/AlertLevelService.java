package com.oneplane.alert.service;

import com.oneplane.alert.domain.AlertLevel;
import java.util.List;

public interface AlertLevelService {
    AlertLevel getAlertByCountryId(Long countryId);
    void saveAlert(AlertLevel alertLevel);
}
