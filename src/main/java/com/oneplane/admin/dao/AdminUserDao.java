package com.oneplane.admin.dao;

import com.oneplane.admin.domain.AdminUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminUserDao {

    // 사용자 목록 조회 (페이징, 검색)
    List<AdminUser> findUserList(@Param("offset") int offset,
                                 @Param("pageSize") int pageSize,
                                 @Param("search") String search);

    // 전체 사용자 수 조회 (검색 조건 포함)
    int getTotalUserCount(@Param("search") String search);


     // 사용자 정보 수정
    int updateUser(AdminUser user);

    /**
     * 사용자 삭제 (논리 삭제)
     */
    int deleteUser(@Param("userId") Integer userId);
}
