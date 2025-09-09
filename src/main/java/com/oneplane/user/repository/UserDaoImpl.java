package com.oneplane.user.repository;

import com.oneplane.user.dao.UserDao;
import com.oneplane.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserDaoImpl implements UserDao {

    @Autowired
    private SqlSession sqlSession;
    private final String namespace = "com.oneplane.user.mapper.UserMapper.";

    @Override
    public User findByEmail(String email) {
        log.debug("이메일로 사용자 조회: {}", email);
        return sqlSession.selectOne(namespace + "findByEmail", email);
    }

    @Override
    public User findById(Integer userId) {
        log.debug("ID로 사용자 조회: {}", userId);
        return sqlSession.selectOne(namespace + "findById", userId);
    }

    @Override
    public int insertUser(User user) {
        log.debug("새 사용자 생성: {}", user.getEmail());
        return sqlSession.insert(namespace+"insertUser", user);
    }

    @Override
    public int updateUser(User user) {
        log.debug("사용자 정보 업데이트: {}", user.getEmail());
        return sqlSession.update(namespace + "updateUser", user);
    }

    @Override
    public int deleteUser(Integer userId) {
        log.debug("사용자 논리 삭제: {}", userId);
        return sqlSession.update(namespace + "deleteUser", userId);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        log.debug("닉네임 중복 확인: {}", nickname);
        Integer count = sqlSession.selectOne(namespace + "countByNickname", nickname);
        return count != null && count > 0;
    }
}
