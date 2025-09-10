package com.oneplane.myPage.dao;

import com.oneplane.myPage.model.TravelHistory;
import java.util.List;

public interface ITravelHistoryDao {
    int insert(TravelHistory travel);
    List<TravelHistory> selectByUserId(Long userId);
    TravelHistory selectById(Long id);
    int deleteById(Long id);  // 추가
    int update(TravelHistory th);  // 추가
}