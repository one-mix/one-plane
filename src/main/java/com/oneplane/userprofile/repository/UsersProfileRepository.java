package com.oneplane.userprofile.repository;

import com.oneplane.userprofile.dao.UsersProfileDao;
import com.oneplane.userprofile.domain.UsersProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
@RequiredArgsConstructor
public class UsersProfileRepository {

    private final UsersProfileDao userProfileDao;

    /**
     * 회원 프로필 조회
     *
     * @param userId 조회할 회원 ID
     * @return UsersProfile 도메인 객체
     */
    public UsersProfile findById(Long userId) {
        return userProfileDao.selectUserProfile(userId);
    }

    /**
     * 회원 프로필 업데이트
     * (이메일, 닉네임, 질병, 장애, 복용약)
     *
     * @param profile 수정할 UsersProfile 객체
     * @return 업데이트된 행 개수
     */
    public int update(UsersProfile profile) {
        return userProfileDao.updateUserProfile(profile);
    }

    /**
     * 닉네임 중복 개수 조회
     *
     * @param nickname 검사할 닉네임
     * @param userId   자기 자신을 제외할 회원 ID
     * @return 중복된 닉네임 개수
     */
    public int countByNickname(String nickname, Long userId) {
        return userProfileDao.countByNickname(nickname, userId);
    }

    /**
     * 회원 소프트 삭제 처리
     *
     * @param userId    회원 ID
     * @param deletedAt 삭제 일시 (Date)
     * @return 업데이트된 행 개수
     */
    public int softDelete(Long userId, Date deletedAt) {
        return userProfileDao.softDeleteUser(userId, deletedAt);
    }
}
