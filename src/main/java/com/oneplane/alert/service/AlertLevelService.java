// 공동 작성자: 방대혁, 오수경
package com.oneplane.alert.service;

import com.oneplane.alert.domain.AlertLevel;
import com.oneplane.alert.dto.TravelWarningApiResponse;

import java.util.List;
/**
 * AlertLevelService
 * - 공공데이터포털 여행경보 API를 통해 데이터를 가져오고
 *   DB에 저장/갱신 및 조회하는 기능을 정의하는 인터페이스
 */
public interface AlertLevelService {

    /**
     * API 응답 데이터를 바탕으로 경보 단계를 판별
     * @param item TravelWarningApiResponse.Item (API에서 내려온 국가별 경보 데이터)
     * @return 경보 단계 문자열 (여행금지 / 철수권고 / 여행자제 / 여행유의 / null)
     * 작성자: 방대혁
     */
    String determineLevelValue(TravelWarningApiResponse.Item item);

    /**
     * 공공데이터포털 API에서 최신 여행경보 데이터를 가져와
     * DB에 저장하거나 갱신
     * 작성자: 방대혁
     */
    void fetchAndSaveAlertLevels();

    List<AlertLevel> getAllAlerts();
}
