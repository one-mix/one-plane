package com.oneplane.recommend.dao;

import com.oneplane.recommend.domain.Country;
import com.oneplane.recommend.domain.TravelHistory;
import com.oneplane.recommend.domain.User;
import com.oneplane.recommend.repository.CountryRepository;
import com.oneplane.recommend.repository.TravelHistoryRepository;
import com.oneplane.recommend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendDAOImpl implements RecommendDAO {

    private final UserRepository userRepository;
    private final TravelHistoryRepository travelHistoryRepository;
    private final CountryRepository countryRepository;

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
}
