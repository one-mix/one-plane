//작성자:방대혁,오수경
package com.oneplane.recommend.service;

import com.oneplane.alert.dao.AlertLevelDao;
import com.oneplane.country.dao.CountryDao;
import com.oneplane.alert.dto.AlertLevelDTO;
import com.oneplane.recommend.dto.RecommendDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;
import com.oneplane.recommend.Dao.RecommendDao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final RecommendDao recommendDao;
    private final CountryDao countryDao;
    private final AlertLevelDao alertLevelDao;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final int PAGE_SIZE = 9; // 페이지당 표시 개수

    /**
     * 추천 동의 저장
     * 작성자:방대혁
     */
    @Override
    @Transactional
    public Integer saveAgreement(Integer userId) {
        return recommendDao.insertAgreement(userId);
    }

    /**
     * 최신 추천 동의 상태 조회
     * 작성자:방대혁
     */
    @Override
    public String getLatestAgreement(Integer userId) {
        RecommendDTO latestRecommend = recommendDao.getLatestRecommend(userId);
        return latestRecommend != null ? latestRecommend.getAgreement() : null;
    }

    /**
     * 여행 목적/동행자 입력 저장
     * 작성자:방대혁
     */
    @Override
    @Transactional
    public void insertInput(RecommendDTO dto) {
        RecommendDTO latestRecommend = recommendDao.getLatestRecommend(dto.getUserId());
        if (latestRecommend == null) {
            throw new IllegalStateException("추천 정보를 찾을 수 없습니다.");
        }
        dto.setRecommendId(latestRecommend.getRecommendId());
        recommendDao.insertInput(dto);
    }

    /**
     * 최신 입력 조회
     * 작성자:방대혁
     */
    @Override
    public RecommendDTO getLatestInput(Integer userId) {
        return recommendDao.getLatestRecommend(userId);
    }

    /**
     * Flask 추천 API 호출
     * 작성자:방대혁
     */
    @Override
    public List<RecommendResultDTO> callFlaskRecommend(Integer userId, String purpose, String companion) {
        String url = "http://localhost:5001/recommend";

        // 요청 바디 구성
        Map<String, Object> payload = Map.of(
                "user", Map.of("user_id", userId),
                "input", Map.of("travelPurpose", purpose, "companion", companion)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<RecommendResultDTO[]> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, RecommendResultDTO[].class);

        RecommendResultDTO[] body = response.getBody();
        if (body == null) return Collections.emptyList();

        List<RecommendResultDTO> results = Arrays.asList(body);

        // 각 국가 코드 기준으로 DB 보강 정보 추가
        for (RecommendResultDTO dto : results) {
            if (dto.getCountryIso3() != null) {
                // 대륙/국기 이미지 조회
                RecommendResultDTO info = countryDao.findCountryInfo(dto.getCountryIso3());
                if (info != null) {
                    dto.setContinent(info.getContinent());
                    dto.setCountryImg(info.getCountryImg());
                }

                // 여행 경보 레벨 조회
                AlertLevelDTO alertLevel = alertLevelDao.findByIsoCode(dto.getCountryIso3());
                if (alertLevel != null) {
                    dto.setAlertLevel(
                            (alertLevel.getLevelValue() == null || alertLevel.getLevelValue().isEmpty())
                                    ? "안전"
                                    : alertLevel.getLevelValue()
                    );
                } else {
                    dto.setAlertLevel("안전");
                }
            }
        }
        return results;
    }

    /**
     * 선택 국가 저장
     * 작성자:방대혁
     */
    @Override
    @Transactional
    public Integer saveSelectedCountry(Integer userId, String country, String city) {
        RecommendDTO latestRecommend = recommendDao.getLatestRecommend(userId);
        if (latestRecommend == null) {
            throw new IllegalStateException("추천 정보를 찾을 수 없습니다.");
        }

        Integer countryId = countryDao.findCountryIdByIsoCode(country);
        if (countryId == null) {
            throw new IllegalArgumentException("유효하지 않은 국가 코드입니다: " + country);
        }

        recommendDao.updateCountryAndCity(latestRecommend.getRecommendId(), countryId, city);
        return latestRecommend.getRecommendId();
    }

    /**
     * 피드백 업데이트
     * 작성자:방대혁
     */
    @Override
    @Transactional
    public void updateFeedback(Integer recommendId, Integer rating, String content) {
        recommendDao.updateFeedback(recommendId, rating, content);
    }

    /**
     * 추천 이력 조회 (페이지네이션)
     * 작성자:방대혁
     */
    @Override
    public List<RecommendResultDTO> getRecommendHistory(Integer userId, int page) {
        int offset = (page - 1) * PAGE_SIZE;
        return recommendDao.findRecommendHistoryByUserId(userId, offset, PAGE_SIZE);
    }

    /**
     * 추천 이력 전체 개수
     * 작성자:방대혁
     */
    @Override
    public int getTotalRecommendHistoryCount(Integer userId) {
        return recommendDao.getTotalRecommendHistoryCount(userId);
    }

    /**
     * 추천 이력 + 페이지네이션 정보 조회
     * 작성자:방대혁
     */
    @Override
    public Map<String, Object> getRecommendHistoryWithPagination(Integer userId, int page) {
        if (page < 1) page = 1;

        int totalCount = getTotalRecommendHistoryCount(userId);
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);

        if (page > totalPages && totalPages > 0) page = totalPages;

        List<RecommendResultDTO> recommendHistory = getRecommendHistory(userId, page);

        return Map.of(
                "recommendHistory", recommendHistory,
                "currentPage", page,
                "totalPages", totalPages,
                "totalCount", totalCount,
                "hasNext", page < totalPages,
                "hasPrevious", page > 1
        );
    }

    @Override
    public void softDeleteRecommend(Integer recommendId, Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("recommendId", recommendId);
        params.put("userId", userId);
        recommendDao.softDeleteRecommend(params);
    }

    /**
     * Soft Delete (마이페이지 전용)
     * 작성자:방대혁
     */
    @Override
    @Transactional
    public boolean softDeleteRecommendMyPage(Long recommendId) {
        return recommendDao.softDeleteRecommendMyPage(recommendId) > 0;
    }
}
