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
        if (latestId == null) {
            throw new IllegalStateException("해당 유저의 동의 내역이 없습니다.");
        }
        dto.setRecommendId(latestId);
        recommendRepository.updateInput(dto);
    }

    @Override
    public void updateSelection(RecommendDTO dto) {
        recommendRepository.updateSelection(dto);
    }

    @Override
    public void updateFeedback(RecommendDTO dto) {
        recommendRepository.updateFeedback(dto);
    }
}