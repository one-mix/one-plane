package com.oneplane.fxrate.repository;

import com.oneplane.fxrate.domain.FxRate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FxRateMapper {
    void upsertFxRate(FxRate fxRate);
    List<FxRate> findRecentByCountry(Long countryId);

    void insertFxRate(FxRate fxRate);
}
