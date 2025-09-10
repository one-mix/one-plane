package com.oneplane.myPage.service;

import com.oneplane.myPage.dao.CountryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CountryService {

    @Autowired
    private CountryDao countryDao;

    public List<Map<String, Object>> getAllCountries() {
        return countryDao.getAllCountries();
    }

    public Map<String, Object> getCountryInfo(Long countryId) {
        return countryDao.getCountryInfo(countryId);
    }

    public Long findCountryIdByName(String countryName) {
        return countryDao.findCountryIdByName(countryName);
    }
}
