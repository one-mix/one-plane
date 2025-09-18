// 작성자: 김동현
package com.oneplane.post.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLike {
    private Integer postLikeId;
    private Integer userId;
    private Integer postId;
    private Date createdAt;

    // 연관 객체 (조회시 사용)
    private String userName;      // 좋아요 누른 사용자 이름
    private String userNickname;  // 좋아요 누른 사용자 닉네임
    private String postTitle;     // 게시글 제목

    /**
     * 특정 사용자가 좋아요 눌렀는지 확인
     * 작성자 : 김동현
     */
    public boolean isLikedBy(Integer userId) {
        return this.userId != null && this.userId.equals(userId);
    }

    /**
     * 좋아요 객체 생성 팩토리 메소드
     * 작성자 : 김동현
     */
    public static PostLike create(Integer userId, Integer postId) {
        return PostLike.builder()
                .userId(userId)
                .postId(postId)
                .createdAt(new Date())
                .build();
    }
}