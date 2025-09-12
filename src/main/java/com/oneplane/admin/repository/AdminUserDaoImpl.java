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

@Slf4j
@Repository
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
    public int updateUser(AdminUser user) {
        log.debug("사용자 정보 수정 - userId: {}", user.getUser_id());
        return sqlSession.update(namespace + "updateUser", user);
    }

    @Override
    public int deleteUser(Integer userId) {
        log.debug("사용자 논리 삭제 - userId: {}", userId);
        return sqlSession.update(namespace + "deleteUser", userId);
    }
}
