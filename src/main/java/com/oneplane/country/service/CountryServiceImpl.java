package com.oneplane.country.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.oneplane.alert.dao.AlertLevelDao;
import com.oneplane.alert.dto.CountryAlertDTO;
import com.oneplane.country.dao.CountryDao;
import com.oneplane.country.domain.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
public class CountryServiceImpl implements CountryService {
    private final CountryDao countryDao;
    private final AlertLevelDao alertLevelDao;

    @Value("${api.country.key}")
    private String apiKey;   // application.yml에 원본 키(인코딩되지 않은 값) 저장

    private final WebClient webClient =
            WebClient.create("https://apis.data.go.kr/1262000/CountryBasicService");

    public CountryServiceImpl(CountryDao countryDao, AlertLevelDao alertLevelDao) {
        this.countryDao = countryDao;
        this.alertLevelDao = alertLevelDao;
    }

    @Override
    public Country getCountryById(Long id) {
        return countryDao.selectCountryById(id);
    }

    @Override
    public List<Country> getAllCountries() {
        return countryDao.selectAllCountries();
    }

    @Override
    public int addCountry(Country country) {
        return countryDao.insertCountry(country);
    }

    @Override
    public int updateCountry(Country country) {
        return countryDao.updateCountry(country);
    }

    @Override
    public int deleteCountry(Long id) {
        return countryDao.deleteCountry(id);
    }

    @Override
    public Country getCountryByName(String name) {
        return countryDao.findByName(name);
    }

    @Override
    public String getIsoCodeByName(String name) {
        Country country = countryDao.findByName(name);
        return country != null ? country.getIsoCode() : null;
    }

    @Override
    public Country findByName(String name) {
        return countryDao.findByName(name);
    }
}
