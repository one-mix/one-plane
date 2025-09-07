package com.oneplane.recommend.service;

import com.oneplane.recommend.dao.RecommendDAO;
import com.oneplane.recommend.domain.Country;
import com.oneplane.recommend.domain.TravelHistory;
import com.oneplane.recommend.domain.User;
import com.oneplane.recommend.dto.RecommendFeatureDTO;
import com.oneplane.recommend.dto.RecommendRequestDTO;
import com.oneplane.recommend.dto.RecommendResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final RecommendDAO recommendDAO;

    @Override
    public List<RecommendResultDTO> getRecommendations(RecommendRequestDTO request) {
        User user = recommendDAO.getUserProfile(request.getUserId());
        List<TravelHistory> histories = recommendDAO.getUserTravelHistory(request.getUserId());
        List<Country> countries = recommendDAO.getAllCountries();

        // 과거 평점만 뽑기
        List<Integer> pastRatings = histories.stream()
                .map(TravelHistory::getRating)
                .collect(Collectors.toList());

        // AI 요청 DTO 만들기
        RecommendFeatureDTO features = new RecommendFeatureDTO();
        features.setGender(user.getGender());
        features.setAge(user.getAge());
        features.setDisease(user.getDisease());
        features.setDisability(user.getDisability());
        features.setMedication(user.getMedication());
        features.setTravelPurpose(request.getTravelPurpose());
        features.setCompanion(request.getCompanion());
        features.setPastRatings(pastRatings);

        return countries.stream().limit(3).map(c -> {
            RecommendResultDTO dto = new RecommendResultDTO();
            dto.setCountryId(c.getCountryId());
            dto.setCountryName(c.getCountryName());
            dto.setContinent(c.getContinent());
            dto.setSimilarity((int) (Math.random() * 20 + 80)); // 80~100 랜덤
            return dto;
        }).collect(Collectors.toList());
    }
}