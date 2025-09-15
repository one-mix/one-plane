package com.oneplane.admin.dao;

import com.oneplane.admin.domain.AdminUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AdminUserDao {

    /**
     * 사용자 목록 조회 (페이징, 검색)
     */
    List<AdminUser> findUserList(@Param("offset") int offset,
                                 @Param("pageSize") int pageSize,
                                 @Param("search") String search);

    /**
     * 전체 사용자 수 조회 (검색 조건 포함)
     */
    int getTotalUserCount(@Param("search") String search);

    /**
     * 탈퇴 사용자 목록 조회 (페이징, 검색)
     */
    List<AdminUser> findDeletedUserList(@Param("offset") int offset,
                                        @Param("pageSize") int pageSize,
                                        @Param("search") String search);

    /**
     * 전체 탈퇴 사용자 수 조회 (검색 조건 포함)
     */
    int getTotalDeletedUserCount(@Param("search") String search);

    /**
     * 사용자 ID로 조회
     */
    AdminUser findUserById(@Param("userId") Integer userId);

    /**
     * 사용자 정보 수정
     */
    int updateUser(AdminUser user);

    /**
     * 사용자 삭제 (논리 삭제)
     */
    int deleteUser(@Param("userId") Integer userId);

    /**
     * 닉네임 중복 확인 (특정 사용자 제외)
     */
    boolean existsByNickname(@Param("nickname") String nickname,
                             @Param("excludeUserId") Integer excludeUserId);

    /**
     * 오늘 가입한 사용자 수
     */
    int getTodaySignupCount();

    /**
     * 이번달 가입한 사용자 수
     */
    int getMonthlySignupCount();

    /**
     * 사용자 역할별 통계
     */
    List<Map<String, Object>> getUserRoleStats();

    /**
     * 사용자 등급별 통계
     */
    List<Map<String, Object>> getUserGradeStats();

    /**
     * 전체 활성 사용자 수 (deleted_at이 null)
     */
    int getActiveUserCount();

    /**
     * 성별별 통계
     */
    List<Map<String, Object>> getUserGenderStats();

    /**
     * 연령대별 통계
     */
    List<Map<String, Object>> getUserAgeStats();

    /**
     * 건강 정보별 통계
     */
    List<Map<String, Object>> getUserHealthStats();

    /**
     * 여행 주의 대상자 수 (65세 이상 또는 건강 정보 보유)
     */
    int getTravelCautionUserCount();

    /**
     * 월별 가입 추이 (최근 12개월)
     */
    List<Map<String, Object>> getMonthlySignupTrend();
}