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
    private Category category;
    private String country;
    private String searchType;
    private String searchKeyword;
    private String sortBy;
    private Integer page;
    private Integer size;
    private Integer userId;
    private Integer periodDays;
    private Integer offset;

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
                (country != null && !country.trim().isEmpty()) ||
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

    /**
     * URL 파라미터 생성을 위한 쿼리 스트링 반환
     */
    public String toQueryString() {
        StringBuilder sb = new StringBuilder();

        if (category != null) {
            sb.append("&category=").append(category.name());
        }

        if (country != null && !country.trim().isEmpty()) {
            sb.append("&country=").append(country);
        }

        if (searchType != null && !searchType.trim().isEmpty()) {
            sb.append("&searchType=").append(searchType);
        }

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sb.append("&search=").append(searchKeyword);
        }

        if (sortBy != null && !sortBy.equals("latest")) {
            sb.append("&sortBy=").append(sortBy);
        }

        return sb.toString();
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

}