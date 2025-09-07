package com.oneplane.recommend.dao;

import com.oneplane.recommend.domain.Country;
import com.oneplane.recommend.domain.Recommend;
import com.oneplane.recommend.domain.TravelHistory;
import com.oneplane.recommend.repository.CountryRepository;
import com.oneplane.recommend.repository.RecommendRepository;
import com.oneplane.recommend.repository.TravelHistoryRepository;
import com.oneplane.recommend.repository.UserRepository;
import com.oneplane.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendDAOImpl implements RecommendDAO {

    private final UserRepository userRepository;
    private final TravelHistoryRepository travelHistoryRepository;
    private final CountryRepository countryRepository;
    private final RecommendRepository recommendRepository;

    @Override
    public User getUserProfile(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<TravelHistory> getUserTravelHistory(Long userId) {
        return travelHistoryRepository.findByUserId(userId);
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public void saveRecommendation(Recommend recommend) {
        recommendRepository.save(recommend);
    }

    @Override
    public List<Recommend> getUserRecommendations(Long userId) {
        return recommendRepository.findByUserId(userId);
    }
}
