// 작성자: 김동현
package com.oneplane.admin.repository;

import com.oneplane.admin.dao.AdminUserDao;
import com.oneplane.admin.domain.AdminUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class AdminUserDaoImpl implements AdminUserDao {

    @Autowired
    private SqlSession sqlSession;
    private final String namespace = "com.oneplane.admin.mapper.AdminUserMapper.";

    @Override
    public List<AdminUser> findUserList(int offset, int pageSize, String search) {
        log.debug("사용자 목록 조회 - offset: {}, pageSize: {}, search: {}", offset, pageSize, search);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        params.put("search", search);

        return sqlSession.selectList(namespace + "findUserList", params);
    }

    @Override
    public int getTotalUserCount(String search) {
        log.debug("전체 사용자 수 조회 - search: {}", search);

        Map<String, Object> params = new HashMap<>();
        params.put("search", search);

        return sqlSession.selectOne(namespace + "getTotalUserCount", params);
    }

    @Override
    public List<AdminUser> findDeletedUserList(int offset, int pageSize, String search) {
        log.debug("탈퇴 사용자 목록 조회 - offset: {}, pageSize: {}, search: {}", offset, pageSize, search);

        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        params.put("search", search);

        return sqlSession.selectList(namespace + "findDeletedUserList", params);
    }

    @Override
    public int getTotalDeletedUserCount(String search) {
        log.debug("전체 탈퇴 사용자 수 조회 - search: {}", search);

        Map<String, Object> params = new HashMap<>();
        params.put("search", search);

        return sqlSession.selectOne(namespace + "getTotalDeletedUserCount", params);
    }

    @Override
    public AdminUser findUserById(Integer userId) {
        log.debug("사용자 ID로 조회 - userId: {}", userId);
        return sqlSession.selectOne(namespace + "findUserById", userId);
    }

    @Override
    public int updateUser(AdminUser user) {
        log.debug("사용자 정보 수정 - userId: {}", user.getUser_id());
        return sqlSession.update(namespace + "updateUser", user);
    }

    @Override
    public int deleteUser(Integer userId) {
        log.debug("사용자 논리 삭제 - userId: {}", userId);
        return sqlSession.update(namespace + "deleteUser", userId);
    }

    @Override
    public boolean existsByNickname(String nickname, Integer excludeUserId) {
        log.debug("닉네임 중복 확인 - nickname: {}, excludeUserId: {}", nickname, excludeUserId);

        Map<String, Object> params = new HashMap<>();
        params.put("nickname", nickname);
        params.put("excludeUserId", excludeUserId);

        Integer count = sqlSession.selectOne(namespace + "countByNickname", params);
        return count != null && count > 0;
    }

    @Override
    public int getTodaySignupCount() {
        log.debug("오늘 가입한 사용자 수 조회");
        return sqlSession.selectOne(namespace + "getTodaySignupCount");
    }

    @Override
    public int getMonthlySignupCount() {
        log.debug("이번달 가입한 사용자 수 조회");
        return sqlSession.selectOne(namespace + "getMonthlySignupCount");
    }

    @Override
    public List<Map<String, Object>> getUserRoleStats() {
        log.debug("사용자 역할별 통계 조회");
        return sqlSession.selectList(namespace + "getUserRoleStats");
    }

    @Override
    public List<Map<String, Object>> getUserGradeStats() {
        log.debug("사용자 등급별 통계 조회");
        return sqlSession.selectList(namespace + "getUserGradeStats");
    }

    @Override
    public int getActiveUserCount() {
        log.debug("전체 활성 사용자 수 조회");
        return sqlSession.selectOne(namespace + "getActiveUserCount");
    }

    @Override
    public List<Map<String, Object>> getUserGenderStats() {
        log.debug("성별별 사용자 통계 조회");
        return sqlSession.selectList(namespace + "getUserGenderStats");
    }

    @Override
    public List<Map<String, Object>> getUserAgeStats() {
        log.debug("연령대별 사용자 통계 조회");
        return sqlSession.selectList(namespace + "getUserAgeStats");
    }

    @Override
    public List<Map<String, Object>> getUserHealthStats() {
        log.debug("건강 정보별 사용자 통계 조회");
        return sqlSession.selectList(namespace + "getUserHealthStats");
    }

    @Override
    public int getTravelCautionUserCount() {
        log.debug("여행 주의 대상자 수 조회");
        return sqlSession.selectOne(namespace + "getTravelCautionUserCount");
    }

    @Override
    public List<Map<String, Object>> getMonthlySignupTrend() {
        log.debug("월별 가입 추이 조회");
        return sqlSession.selectList(namespace + "getMonthlySignupTrend");
    }
}