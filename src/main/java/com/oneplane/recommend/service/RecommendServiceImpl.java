package com.oneplane.recommend.service;

import com.oneplane.recommend.dto.RecommendDTO;
import com.oneplane.recommend.repository.RecommendRepository;
import org.springframework.stereotype.Service;

@Service
public class RecommendServiceImpl implements RecommendService {

    private final RecommendRepository recommendRepository;

    public RecommendServiceImpl(RecommendRepository recommendRepository) {
        this.recommendRepository = recommendRepository;
    }

    @Override
    public void saveAgreement(Integer userId) {
        recommendRepository.insertAgreement(userId);
    }

    @Override
    public String getLatestAgreement(Integer userId) {
        return recommendRepository.findLatestAgreement(userId);
    }

    @Override
    public void insertSelection(RecommendDTO dto) {
        recommendRepository.insertSelection(dto);
    }

    @Override
    public void insertFeedback(RecommendDTO dto) {
        recommendRepository.insertFeedback(dto);
    }
}