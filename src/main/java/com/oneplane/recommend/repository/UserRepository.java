package com.oneplane.recommend.repository;

import com.oneplane.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {
    User findById(Long userId);
}