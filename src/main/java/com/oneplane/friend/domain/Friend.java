// 작성자: 김동현
package com.oneplane.friend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friend {
    private Integer friendId;
    private Integer followerId;      // 팔로우 하는 사용자 ID
    private Integer followingId;     // 팔로우 당하는 사용자 ID
    private LocalDateTime createdAt;

    // 조인된 사용자 정보 (조회시 사용)
    private String followerNickname;
    private String followerName;
    private String followerProfileImg;
    private String followingNickname;
    private String followingName;
    private String followingProfileImg;

    // 팔로우 관계 생성
    public static Friend create(Integer followerId, Integer followingId) {
        return Friend.builder()
                .followerId(followerId)
                .followingId(followingId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
