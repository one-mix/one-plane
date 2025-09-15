package com.oneplane.post.domain;

import com.oneplane.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    private String country;
    private Integer postId;
    private Integer userId;
    private String title;
    private String content;              // Summernote HTML 콘텐츠
    private String thumbnailImage;       // 썸네일 이미지 URL
    private Category category;
    private Long countryId;           // 국가 ID (외래키)
    private String countryName;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    private User user;                   // 작성자 정보
//    private List<PostFile> files;        // 첨부파일 목록

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean isOwner(Integer userId) {
        return this.userId != null && this.userId.equals(userId);
    }

    public String getCategoryName() {
        return category != null ? category.name() : "";
    }

    public String getAuthorName() {
        if (user != null) {
            // 닉네임이 있으면 닉네임, 없으면 이름 반환
            return user.getNickname() != null ? user.getNickname() : user.getName();
        }
        return "알 수 없음";
    }

    // 조회수 증가
    public void increaseViewCount() {
        this.viewCount = (this.viewCount == null ? 0 : this.viewCount) + 1;
    }

    // 좋아요 수 증가/감소
    public void increaseLikeCount() {
        this.likeCount = (this.likeCount == null ? 0 : this.likeCount) + 1;
    }

    public void decreaseLikeCount() {
        this.likeCount = (this.likeCount == null ? 0 : this.likeCount) - 1;
        if (this.likeCount < 0) this.likeCount = 0;
    }

    // 국가명 반환 (countryName이 있으면 그것을, 없으면 기본값)
    public String getDisplayCountryName() {
        return countryName != null ? countryName : "미분류";
    }
}
