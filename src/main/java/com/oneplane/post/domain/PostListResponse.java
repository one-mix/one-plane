package com.oneplane.post.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListResponse {
    private List<Post> posts;            // 게시글 목록
    private int currentPage;             // 현재 페이지
    private int totalPages;              // 전체 페이지 수
    private long totalElements;          // 전체 게시글 수
    private int size;                    // 페이지 크기
    private boolean hasNext;             // 다음 페이지 존재 여부
    private boolean hasPrevious;         // 이전 페이지 존재 여부

    // 페이징 정보 계산
    public static PostListResponse of(List<Post> posts, PostSearchCondition condition, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / condition.getSize());

        return PostListResponse.builder()
                .posts(posts)
                .currentPage(condition.getPage())
                .totalPages(totalPages)
                .totalElements(totalElements)
                .size(condition.getSize())
                .hasNext(condition.getPage() < totalPages)
                .hasPrevious(condition.getPage() > 1)
                .build();
    }
}