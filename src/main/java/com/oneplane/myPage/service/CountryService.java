package com.oneplane.myPage.service;

import com.oneplane.myPage.dao.MypageCountryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CountryService {

    @Autowired
    private MypageCountryDao mypageCountryDao;

    public List<Map<String, Object>> getAllCountries() {
        return mypageCountryDao.getAllCountries();
    }

    public Map<String, Object> getCountryInfo(Long countryId) {
        return mypageCountryDao.getCountryInfo(countryId);
    }

    public Long findCountryIdByName(String countryName) {
        return mypageCountryDao.findCountryIdByName(countryName);
    }
}
