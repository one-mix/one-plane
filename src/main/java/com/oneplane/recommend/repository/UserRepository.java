package com.oneplane.recommend.repository;

import com.oneplane.recommend.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {
    User findById(Long userId);
}