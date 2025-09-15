package com.oneplane.mypost.domain;

//import com.oneplane.post.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPost {

    private Integer userId;          // 사용자 ID
    //    private Category category;       // 카테고리 필터
    private String country;          // 국가 필터
    private String searchType;       // 검색 타입 (title, content, author)
    private String searchKeyword;    // 검색 키워드
    private String sortBy;           // 정렬 기준 (latest, popular, oldest)
    private Integer page;            // 현재 페이지
    private Integer size;            // 페이지 크기
    private Integer offset;          // 오프셋



    // 페이지 그룹 크기 (한번에 보여질 페이지 번호 개수)
    private static final int PAGE_GROUP_SIZE = 5;

    // 기본값 설정
    public void setDefaults() {
        if (this.page == null || this.page < 1) {
            this.page = 1;
        }
        if (this.size == null || this.size < 1) {
            this.size = 6; // 기본 페이지 크기
        }
        if (this.sortBy == null || this.sortBy.isEmpty()) {
            this.sortBy = "latest"; // 기본 정렬: 최신순
        }

        // offset 계산
        this.offset = (this.page - 1) * this.size;
    }

    // 페이지 그룹 시작 번호 계산
    public int getPageGroupStart() {
        return ((this.page - 1) / PAGE_GROUP_SIZE) * PAGE_GROUP_SIZE + 1;
    }

    // 페이지 그룹 끝 번호 계산
    public int getPageGroupEnd(int totalPages) {
        int groupEnd = getPageGroupStart() + PAGE_GROUP_SIZE - 1;
        return Math.min(groupEnd, totalPages);
    }
}