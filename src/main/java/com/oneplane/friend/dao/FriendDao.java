// 작성자: 김동현
package com.oneplane.friend.dao;

import com.oneplane.friend.domain.Friend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendDao {

    /**
     * 팔로우 관계 생성
     * 작성자 : 김동현
     */
    int insertFriend(Friend friend);

    /**
     * 팔로우 관계 삭제 (언팔로우)
     * 작성자 : 김동현
     */
    int deleteFriend(@Param("followerId") Integer followerId,
                     @Param("followingId") Integer followingId);

    /**
     * 팔로우 상태 확인
     * 작성자 : 김동현
     */
    boolean isFollowing(@Param("followerId") Integer followerId,
                        @Param("followingId") Integer followingId);

    /**
     * 내가 팔로우한 사람들 목록 (페이징)
     * 작성자 : 김동현
     */
    List<Friend> findFollowingList(@Param("userId") Integer userId,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);

    /**
     * 나를 팔로우한 사람들 목록 (페이징)
     * 작성자 : 김동현
     */
    List<Friend> findFollowerList(@Param("userId") Integer userId,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    /**
     * 내가 팔로우한 사람 수
     * 작성자 : 김동현
     */
    int countFollowing(@Param("userId") Integer userId);

    /**
     * 나를 팔로우한 사람 수
     * 작성자 : 김동현
     */
    int countFollower(@Param("userId") Integer userId);

    /**
     * 닉네임으로 사용자 검색 (친구 기능용)
     * 작성자 : 김동현
     */
    List<Friend> searchUsersByNickname(@Param("nickname") String nickname,
                                       @Param("currentUserId") Integer currentUserId,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    /**
     * 닉네임 검색 결과 총 개수
     * 작성자 : 김동현
     */
    int countSearchResults(@Param("nickname") String nickname,
                           @Param("currentUserId") Integer currentUserId);
}