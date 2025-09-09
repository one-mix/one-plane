package com.oneplane.fxrate.dao;

import com.oneplane.fxrate.domain.FxRate;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FxRateMapper {

    @Insert("INSERT INTO fx_rate (fx_rate_id, country_id, currency_code, deal_bas_r, base_date) " +
            "VALUES (seq_fx_rate.NEXTVAL, #{countryId}, #{currencyCode}, #{dealBasR}, #{baseDate})")
    void insertFxRate(FxRate fxRate);

    @Select("SELECT * FROM fx_rate WHERE country_id = #{countryId} ORDER BY base_date DESC FETCH FIRST 7 ROWS ONLY")
    List<FxRate> findRecentByCountry(Long countryId);
}
