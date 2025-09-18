// 작성자: 김동현
package com.oneplane.friend.service;

import com.oneplane.friend.dao.FriendDao;
import com.oneplane.friend.domain.Friend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FriendService {

    private final FriendDao friendDao;

    /**
     * 팔로우
     * 작성자 : 김동현
     */
    public void follow(Integer followerId, Integer followingId) {
        log.info("팔로우 요청: {} -> {}", followerId, followingId);

        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        if (friendDao.isFollowing(followerId, followingId)) {
            throw new IllegalArgumentException("이미 팔로우한 사용자입니다.");
        }

        Friend friend = Friend.create(followerId, followingId);
        int result = friendDao.insertFriend(friend);

        if (result <= 0) {
            throw new RuntimeException("팔로우 처리에 실패했습니다.");
        }

        log.info("팔로우 완료: {} -> {}", followerId, followingId);
    }

    /**
     * 언팔로우
     * 작성자 : 김동현
     */
    public void unfollow(Integer followerId, Integer followingId) {
        log.info("언팔로우 요청: {} -> {}", followerId, followingId);

        if (!friendDao.isFollowing(followerId, followingId)) {
            throw new IllegalArgumentException("팔로우하지 않은 사용자입니다.");
        }

        int result = friendDao.deleteFriend(followerId, followingId);

        if (result <= 0) {
            throw new RuntimeException("언팔로우 처리에 실패했습니다.");
        }

        log.info("언팔로우 완료: {} -> {}", followerId, followingId);
    }

    /**
     * 팔로우 상태 확인
     * 작성자 : 김동현
     */
    @Transactional(readOnly = true)
    public boolean isFollowing(Integer followerId, Integer followingId) {
        if (followerId == null || followingId == null) {
            return false;
        }
        return friendDao.isFollowing(followerId, followingId);
    }

    /**
     * 내가 팔로우한 사람들 목록 조회 (페이징)
     * 작성자 : 김동현
     */
    @Transactional(readOnly = true)
    public List<Friend> getFollowingList(Integer userId, int page, int size) {
        if (page < 1) page = 1;
        if (size < 1) size = 16;

        int offset = (page - 1) * size;
        return friendDao.findFollowingList(userId, offset, size);
    }

    /**
     * 나를 팔로우한 사람들 목록 조회 (페이징)
     * 작성자 : 김동현
     */
    @Transactional(readOnly = true)
    public List<Friend> getFollowerList(Integer userId, int page, int size) {
        if (page < 1) page = 1;
        if (size < 1) size = 16;

        int offset = (page - 1) * size;
        return friendDao.findFollowerList(userId, offset, size);
    }

    /**
     * 팔로잉 수 조회
     * 작성자 : 김동현
     */
    @Transactional(readOnly = true)
    public int getFollowingCount(Integer userId) {
        return friendDao.countFollowing(userId);
    }

    /**
     * 팔로워 수 조회
     * 작성자 : 김동현
     */
    @Transactional(readOnly = true)
    public int getFollowerCount(Integer userId) {
        return friendDao.countFollower(userId);
    }

    /**
     * 사용자 검색 (닉네임)
     * 작성자 : 김동현
     */
    @Transactional(readOnly = true)
    public List<Friend> searchUsers(String nickname, Integer currentUserId, int page, int size) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("검색어를 입력해주세요.");
        }

        if (page < 1) page = 1;
        if (size < 1) size = 16;

        int offset = (page - 1) * size;
        return friendDao.searchUsersByNickname(nickname.trim(), currentUserId, offset, size);
    }

    /**
     * 검색 결과 총 개수
     * 작성자 : 김동현
     */
    @Transactional(readOnly = true)
    public int getSearchResultCount(String nickname, Integer currentUserId) {
        if (nickname == null || nickname.trim().isEmpty()) {
            return 0;
        }
        return friendDao.countSearchResults(nickname.trim(), currentUserId);
    }
}