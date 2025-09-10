package com.oneplane.myPage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MypageCountryDao {

    @Autowired
    private JdbcTemplate jdbc;

    public List<Map<String, Object>> getAllCountries() {
        String sql = "SELECT COUNTRY_ID, COUNTRY_NAME, IMG, DISTANCE FROM COUNTRY ORDER BY COUNTRY_NAME";
        return jdbc.queryForList(sql);
    }

    public Map<String, Object> getCountryInfo(Long countryId) {
        String sql = "SELECT COUNTRY_ID, COUNTRY_NAME, IMG, DISTANCE FROM COUNTRY WHERE COUNTRY_ID = ?";
        try {
            return jdbc.queryForMap(sql, countryId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Long findCountryIdByName(String countryName) {
        String sql = "SELECT COUNTRY_ID FROM COUNTRY WHERE UPPER(COUNTRY_NAME) = UPPER(?)";
        try {
            return jdbc.queryForObject(sql, Long.class, countryName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
