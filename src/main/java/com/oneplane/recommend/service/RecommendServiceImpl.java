package com.oneplane.recommend.service;

import com.oneplane.alert.dao.AlertLevelDao;
import com.oneplane.country.dao.CountryDao;
import com.oneplane.alert.dto.AlertLevelDTO;
import com.oneplane.country.domain.Country;
import com.oneplane.recommend.dto.RecommendDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;
import com.oneplane.recommend.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final RecommendRepository recommendRepository;
    private final CountryDao countryDao;
    private final AlertLevelDao alertLevelDao;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Integer saveAgreement(Integer userId) {
        return recommendRepository.insertAgreement(userId);
    }

    @Override
    public String getLatestAgreement(Integer userId) {

        RecommendDTO latestRecommend = recommendRepository.getLatestRecommend(userId);
        return latestRecommend.getAgreement();
    }

    @Override
    public void insertInput(RecommendDTO dto) {
        RecommendDTO latestRecommend = recommendRepository.getLatestRecommend(dto.getUserId());
        dto.setRecommendId(latestRecommend.getRecommendId());
        recommendRepository.insertInput(dto);
    }

    @Override
    public RecommendDTO getLatestInput(Integer userId) {
        return recommendRepository.getLatestRecommend(userId);
    }

    @Override
    public List<RecommendResultDTO> callFlaskRecommend(Integer userId, String purpose, String companion) {
        String url = "http://localhost:5001/recommend";

        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> userPart = new HashMap<>();
        userPart.put("user_id", userId);

        Map<String, Object> inputPart = new HashMap<>();
        inputPart.put("travelPurpose", purpose);
        inputPart.put("companion", companion);

        payload.put("user", userPart);
        payload.put("input", inputPart);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<RecommendResultDTO[]> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, RecommendResultDTO[].class);

        List<RecommendResultDTO> results = Arrays.asList(response.getBody());

        // 각 국가 코드 기준으로 DB에서 continent, img 가져오기
        for (RecommendResultDTO dto : results) {
            if (dto.getCountryIso3() != null) {
                RecommendResultDTO info = countryDao.findCountryInfo(dto.getCountryIso3());
                if (info != null) {
                    dto.setContinent(info.getContinent());
                    dto.setCountryImg(info.getCountryImg());
                }

                // 알림 테이블에서 여행 경보 정보 가져오기 (국가 코드로 검색)
                AlertLevelDTO alertLevel = alertLevelDao.findByIsoCode(dto.getCountryIso3());
                if (alertLevel != null) {
                    // 경보 레벨이 없으면 "안전"으로 처리
                    if (alertLevel.getLevelValue() == null || alertLevel.getLevelValue().isEmpty()) {
                        alertLevel.setLevelValue("안전");
                    }
                    String levelValue = alertLevel.getLevelValue();
                    dto.setAlertLevel(levelValue);
                } else {
                    // 만약 경보 정보가 없으면 "안전"으로 처리
                    String safeLevel = "안전";
                    dto.setAlertLevel(safeLevel);
                }
            }
        }
        return results;
    }

    @Override
    public Integer saveSelectedCountry(Integer userId, String country, String city) {
        // 1. 가장 최근 recommendId 가져오기
        RecommendDTO latestRecommend = recommendRepository.getLatestRecommend(userId);
        if (latestRecommend == null) {
            throw new IllegalStateException("추천 정보를 찾을 수 없습니다.");
        }

        // 2. ISO 코드로 countryId 조회
        Integer countryId = countryDao.findCountryIdByIsoCode(country);
        if (countryId == null) {
            throw new IllegalArgumentException("유효하지 않은 국가 코드입니다: " + country);
        }

        // 3. countryId 업데이트
        recommendRepository.updateCountryAndCity(latestRecommend.getRecommendId(), countryId, city);

        return latestRecommend.getRecommendId();
    }

    @Override
    public void updateFeedback(Integer recommendId, Integer rating, String content) {
        recommendRepository.updateFeedback(recommendId, rating, content);
    }

    @Override
    public List<RecommendResultDTO> getRecommendHistory(Integer userId, int page) {
        int limit = 9; // 한 페이지당 9개씩 표시
        int offset = (page - 1) * limit;

        // 페이지네이션을 적용한 추천 이력 조회
        return recommendRepository.findRecommendHistoryByUserId(userId, offset, limit);
    }

    @Override
    public int getTotalRecommendHistoryCount(Integer userId) {
        return recommendRepository.getTotalRecommendHistoryCount(userId);
    }

    public Map<String, Object> getRecommendHistoryWithPagination(Integer userId, int page) {
        if (page < 1) {
            page = 1; // 최소 페이지는 1
        }

        int limit = 9;
        int totalCount = getTotalRecommendHistoryCount(userId);
        int totalPages = (int) Math.ceil((double) totalCount / limit);

        // 페이지가 총 페이지 수를 초과하지 않도록 제한
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        List<RecommendResultDTO> recommendHistory = getRecommendHistory(userId, page);

        Map<String, Object> result = new HashMap<>();
        result.put("recommendHistory", recommendHistory);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);
        result.put("totalCount", totalCount);
        result.put("hasNext", page < totalPages);
        result.put("hasPrevious", page > 1);

        return result;
    }

    @Override
    public void softDeleteRecommend(Integer recommendId, Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("recommendId", recommendId);
        params.put("userId", userId);

        recommendRepository.softDeleteRecommend(params);
    }

    @Override
    public boolean softDeleteRecommendMyPage(Long recommendId) {
        return recommendRepository.softDeleteRecommendMyPage(recommendId) > 0;
    }
}