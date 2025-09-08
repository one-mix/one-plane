package com.oneplane.recommend.service;

import com.oneplane.recommend.dto.RecommendDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;
import com.oneplane.recommend.repository.RecommendRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RecommendServiceImpl implements RecommendService {

    private final RecommendRepository recommendRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public RecommendServiceImpl(RecommendRepository recommendRepository) {
        this.recommendRepository = recommendRepository;
    }

    @Override
    public Integer saveAgreement(Integer userId) {
        recommendRepository.insertAgreement(userId);
        return recommendRepository.getLatestRecommendId(userId);
    }

    @Override
    public String getLatestAgreement(Integer userId) {
        return recommendRepository.getLatestAgreement(userId);
    }

    @Override
    public void updateInput(RecommendDTO dto) {
        Integer latestId = recommendRepository.getLatestRecommendId(dto.getUserId());
        dto.setRecommendId(latestId);
        recommendRepository.updateInput(dto);
    }

    @Override
    public RecommendDTO getLatestInput(Integer userId) {
        return recommendRepository.getLatestInput(userId);
    }

    @Override
    public List<RecommendResultDTO> callFlaskRecommend(Integer userId, String purpose, String companion) {
        String url = "http://localhost:5001/recommend";

        Map<String, Object> payload = new HashMap<>();

        // user 파트
        Map<String, Object> userPart = new HashMap<>();
        userPart.put("user_id", userId);

        // input 파트
        Map<String, Object> inputPart = new HashMap<>();
        inputPart.put("travelPurpose", purpose);   // 여기 수정
        inputPart.put("companion", companion);

        payload.put("user", userPart);
        payload.put("input", inputPart);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<RecommendResultDTO[]> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, RecommendResultDTO[].class);

        return Arrays.asList(response.getBody());
    }
}