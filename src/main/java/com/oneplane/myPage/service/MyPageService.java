package com.oneplane.myPage.service;

import com.oneplane.myPage.dao.CertificationDao;
import com.oneplane.myPage.dao.TravelHistoryDao;
import com.oneplane.myPage.dto.MyPageStatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MyPageService {

    @Autowired
    private CertificationDao certificationDao;

    @Autowired
    private TravelHistoryDao travelHistoryDao;

    public List<Map<String, Object>> getCertifiedCountries(Long userId) {
        return certificationDao.getUserCertifiedCountries(userId);
    }

    public MyPageStatsDto getUserStats(Long userId) {
        try {
            int certificationCount = certificationDao.getCertificationCount(userId);
            int totalDistance = certificationDao.getTotalCertificationDistance(userId);


            System.out.println("Service - certificationCount: " + certificationCount);
            System.out.println("Service - totalDistance: " + totalDistance);

            return new MyPageStatsDto(certificationCount, totalDistance);

        } catch (Exception e) {
            System.err.println("getUserStats 오류: " + e.getMessage());
            e.printStackTrace();
            return new MyPageStatsDto(0, 0);
        }
    }
}
