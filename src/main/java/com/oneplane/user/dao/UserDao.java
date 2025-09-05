package com.oneplane.user.dao;

import com.oneplane.user.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    // 이메일로 사용자 조회
    User findByEmail(@Param("email") String email);

    // 사용자 ID로 조회
    User findById(@Param("userId") Integer userId);

    // 카카오 로그인 시 회원가입
    int insertUser(User user);

    // 사용자 업데이트
    int updateUser(User user);

    // 회원 탈퇴(소프트 딜리트)
    int deleteUser(@Param("userId") Integer userId);

    // 닉네임 중복 확인
    boolean existsByNickname(@Param("nickname") String nickname);

}
