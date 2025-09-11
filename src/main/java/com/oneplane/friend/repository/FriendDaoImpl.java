package com.oneplane.friend.repository;

import com.oneplane.friend.dao.FriendDao;
import com.oneplane.friend.domain.Friend;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class FriendDaoImpl implements FriendDao {

    @Autowired
    private SqlSession sqlSession;
    private final String namespace = "com.oneplane.friend.mapper.FriendMapper.";

    // 팔로우 하기
    @Override
    public int insertFriend(Friend friend) {
        log.debug("팔로우 관계 생성: {} -> {}", friend.getFollowerId(), friend.getFollowingId());
        return sqlSession.insert(namespace + "insertFriend", friend);
    }

    // 팔로우 삭제
    @Override
    public int deleteFriend(Integer followerId, Integer followingId) {
        log.debug("팔로우 관계 삭제: {} -> {}", followerId, followingId);
        Map<String, Object> params = new HashMap<>();
        params.put("followerId", followerId);
        params.put("followingId", followingId);
        return sqlSession.delete(namespace + "deleteFriend", params);
    }

    // 팔로우 상태 확인
    @Override
    public boolean isFollowing(Integer followerId, Integer followingId) {
        log.debug("팔로우 상태 확인: {} -> {}", followerId, followingId);
        Map<String, Object> params = new HashMap<>();
        params.put("followerId", followerId);
        params.put("followingId", followingId);
        Integer count = sqlSession.selectOne(namespace + "isFollowing", params);
        return count != null && count > 0;
    }

    // 내가 팔로우한 사람들 목록 (페이징)
    @Override
    public List<Friend> findFollowingList(Integer userId, int offset, int limit) {
        log.debug("팔로잉 목록 조회: userId={}, offset={}, limit={}", userId, offset, limit);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", offset);
        params.put("limit", limit);
        return sqlSession.selectList(namespace + "findFollowingList", params);
    }

    // 내가 팔로우한 사람들 목록 (페이징)
    @Override
    public List<Friend> findFollowerList(Integer userId, int offset, int limit) {
        log.debug("팔로워 목록 조회: userId={}, offset={}, limit={}", userId, offset, limit);
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", offset);
        params.put("limit", limit);
        return sqlSession.selectList(namespace + "findFollowerList", params);
    }

    // 내가 팔로우한 사람 수
    @Override
    public int countFollowing(Integer userId) {
        log.debug("팔로잉 수 조회: {}", userId);
        return sqlSession.selectOne(namespace + "countFollowing", userId);
    }
    // 나를 팔로우한 사람 수
    @Override
    public int countFollower(Integer userId) {
        log.debug("팔로워 수 조회: {}", userId);
        return sqlSession.selectOne(namespace + "countFollower", userId);
    }
    // 닉네임으로 사용자 검색 (친구 기능용)
    @Override
    public List<Friend> searchUsersByNickname(String nickname, Integer currentUserId, int offset, int limit) {
        log.debug("사용자 검색: nickname={}, currentUserId={}", nickname, currentUserId);
        Map<String, Object> params = new HashMap<>();
        params.put("nickname", "%" + nickname + "%");
        params.put("currentUserId", currentUserId);
        params.put("offset", offset);
        params.put("limit", limit);
        return sqlSession.selectList(namespace + "searchUsersByNickname", params);
    }
    // 닉네임 검색 결과 총 개수
    @Override
    public int countSearchResults(String nickname, Integer currentUserId) {
        log.debug("검색 결과 수 조회: nickname={}, currentUserId={}", nickname, currentUserId);
        Map<String, Object> params = new HashMap<>();
        params.put("nickname", "%" + nickname + "%");
        params.put("currentUserId", currentUserId);
        return sqlSession.selectOne(namespace + "countSearchResults", params);
    }
}
