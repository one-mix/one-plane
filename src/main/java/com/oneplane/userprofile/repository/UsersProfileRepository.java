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

    public UsersProfile findById(Long userId) {
        return userProfileDao.selectUserProfile(userId);
    }

    public int save(UsersProfile profile) {
        return userProfileDao.updateUserProfile(profile);
    }

    /**
     * Soft Delete 처리: deleted_at 컬럼에 날짜를 기록
     * @param userId    회원 ID
     * @param deletedAt 삭제 일시
     * @return 업데이트된 행 개수
     */
    public int softDelete(Long userId, Date deletedAt) {
        return userProfileDao.softDeleteUser(userId, deletedAt);
    }
}
