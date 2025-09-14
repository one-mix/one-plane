package com.oneplane.myPage.service;

import com.oneplane.myPage.dao.CertificationDao;
import com.oneplane.myPage.dao.TravelHistoryDao;
import com.oneplane.myPage.dto.MyPageStatsDto;
import com.oneplane.myPage.domain.TravelHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyPageService {

    @Autowired
    private CertificationDao certificationDao;

    @Autowired
    private TravelHistoryDao travelHistoryDao;

    public List<?> getCertifiedCountries(Long userId) {
        return certificationDao.getUserCertifiedCountries(userId);
    }

    public MyPageStatsDto getUserStats(Long userId) {
        try {
            int certificationCount = certificationDao.getCertificationCount(userId);
            int totalDistance      = certificationDao.getTotalCertificationDistance(userId);
            List<TravelHistory> histories = travelHistoryDao.selectByUserId(userId);
            int travelHistoryCount = histories.size();

            return new MyPageStatsDto(certificationCount, totalDistance, travelHistoryCount);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyPageStatsDto(0, 0, 0);
        }
    }
}
