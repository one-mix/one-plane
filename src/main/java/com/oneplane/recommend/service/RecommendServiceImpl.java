package com.oneplane.recommend.service;

import com.oneplane.alert.dto.AlertLevelDTO;
import com.oneplane.alert.repository.AlertLevelRepository;
import com.oneplane.country.repository.CountryRepository;
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
    private final CountryRepository countryRepository;
    private final AlertLevelRepository alertLevelRepository;
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
                RecommendResultDTO info = countryRepository.findCountryInfo(dto.getCountryIso3());
                if (info != null) {
                    dto.setContinent(info.getContinent());
                    dto.setCountryImg(info.getCountryImg());
                }

                // 알림 테이블에서 여행 경보 정보 가져오기 (국가 코드로 검색)
                AlertLevelDTO alertLevel = alertLevelRepository.findByIsoCode(dto.getCountryIso3());
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
    public Integer saveSelectedCountry(Integer userId, String country) {
        // 1. 가장 최근 recommendId 가져오기
        RecommendDTO latestRecommend = recommendRepository.getLatestRecommend(userId);
        if (latestRecommend == null) {
            throw new IllegalStateException("추천 정보를 찾을 수 없습니다.");
        }

        // 2. ISO 코드로 countryId 조회
        Integer countryId = countryRepository.findCountryIdByIsoCode(country);
        if (countryId == null) {
            throw new IllegalArgumentException("유효하지 않은 국가 코드입니다: " + country);
        }

        // 3. countryId 업데이트
        recommendRepository.updateCountryId(latestRecommend.getRecommendId(), countryId);

        return latestRecommend.getRecommendId();
    }

    @Override
    public void updateFeedback(Integer recommendId, Integer rating, String content) {
        recommendRepository.updateFeedback(recommendId, rating, content);
    }
}