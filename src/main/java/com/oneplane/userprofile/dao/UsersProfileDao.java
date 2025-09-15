package com.oneplane.userprofile.dao;

import com.oneplane.userprofile.domain.UsersProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.sql.Date;

@Mapper
public interface UsersProfileDao {

    /**
     * 회원 프로필 조회
     * @param userId 조회할 회원 ID
     * @return UserProfile 도메인 객체
     */
    UsersProfile selectUserProfile(@Param("userId") Long userId);

    /**
     * 회원 이메일 및 닉네임 수정
     * @param profile 수정할 이메일, 닉네임, userId 포함된 도메인 객체
     * @return 업데이트된 행 개수
     */
    int updateUserProfile(UsersProfile profile);


    // Soft delete: deleted_at 업데이트
    int softDeleteUser(@Param("userId") Long userId, @Param("deletedAt") Date deletedAt);


    int countByNickname(@Param("nickname") String nickname,
                        @Param("userId") Long userId);
}
