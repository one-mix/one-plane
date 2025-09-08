package com.oneplane.alert.service;

import com.oneplane.alert.dao.AlertLevelDao;
import com.oneplane.alert.domain.AlertLevel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertLevelServiceImpl implements AlertLevelService {
    private final AlertLevelDao alertLevelDao;

    public AlertLevelServiceImpl(AlertLevelDao alertLevelDao) {
        this.alertLevelDao = alertLevelDao;
    }

    @Override
    public AlertLevel getAlertByCountryId(Long countryId) {
        return alertLevelDao.selectAlertByCountryId(countryId);
    }

    @Override
    public void saveAlert(AlertLevel alertLevel) {
        alertLevelDao.insertAlert(alertLevel);
    }

    @Override
    public List<AlertLevel> getAllAlerts() {
        return alertLevelDao.selectAllAlerts();
    }
}
