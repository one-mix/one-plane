package com.oneplane.post.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSearchCondition {
    private Category category;       // 카테고리 필터
    private String searchType;           // 검색 타입 (title, content, author)
    private String searchKeyword;        // 검색 키워드
    private String sortBy;               // 정렬 기준 (latest, view, like)
    private Integer page;                // 페이지 번호
    private Integer size;                // 페이지 크기
    private Integer userId;              // 특정 사용자 게시글만 조회

    // 기본값 설정
    public void setDefaults() {
        if (sortBy == null) sortBy = "latest";
        if (page == null || page < 1) page = 1;
        if (size == null || size < 1) size = 6;
    }

    // 페이징 오프셋 계산
    public int getOffset() {
        return (page - 1) * size;
    }

    // 검색 조건이 있는지 확인
    public boolean hasSearchCondition() {
        return (searchKeyword != null && !searchKeyword.trim().isEmpty()) ||
                category != null ||
                userId != null;
    }

    // 다음 페이지 번호
    public int getNextPage() {
        return page + 1;
    }

    // 이전 페이지 번호
    public int getPrevPage() {
        return page > 1 ? page - 1 : 1;
    }

    // 페이지 그룹 시작 번호 (1, 6, 11, ...)
    public int getPageGroupStart() {
        return ((page - 1) / 5) * 5 + 1;
    }

    // 페이지 그룹 끝 번호
    public int getPageGroupEnd(int totalPages) {
        int groupEnd = getPageGroupStart() + 4;
        return Math.min(groupEnd, totalPages);
    }
}