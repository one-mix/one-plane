// 작성자: 김동현
package com.oneplane.post.domain;

import com.oneplane.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private Integer commentId;
    private Integer postId;
    private Integer userId;
    private String content;
    private Date createdAt;
    private Date deletedAt;

    // 댓글 작성자 정보
    private String nickname;
    private String authorName;
    private String profileImg;

    private String postTitle;

    // 사용자 객체
    private User user;


    public boolean isDeleted() {
        return deletedAt != null;
    }

    // 작성자 표시명 반환
    public String getDisplayName() {
        if (nickname != null && !nickname.trim().isEmpty()) {
            return nickname;
        }
        if (authorName != null && !authorName.trim().isEmpty()) {
            return authorName;
        }
        return "익명";
    }

    // 프로필 이미지 URL 반환
    public String getProfileImageUrl() {
        if (profileImg != null && !profileImg.trim().isEmpty()) {
            return profileImg;
        }
        return "/images/profile.png";
    }
}
