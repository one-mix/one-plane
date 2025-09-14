<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="/css/admin/postList.css"/>

<!-- 검색 및 필터 섹션 -->
<div class="search-filter-section">
  <div class="d-flex justify-content-between align-items-center">
    <div class="result-summary">
      <span class="text-muted">총 <strong>${totalCount != null ? totalCount : 0}</strong>개의 게시글</span>
    </div>
    <form method="get" class="search-form" id="searchForm">
      <div class="input-group" style="width: 300px;">
        <span class="input-group-text"><i class="bi bi-search"></i></span>
        <input type="text" class="form-control" name="search"
               placeholder="" value="${search}">
      </div>
    </form>
  </div>
</div>

<!-- 인기 게시글 테이블 -->
<div class="posts-table-container">
  <div class="table-responsive">
    <table class="table table-hover align-middle" id="postsTable">
      <thead class="table-primary">
      <tr>
        <th scope="col" style="width: 50px;">순위</th>
        <th scope="col" style="width: 200px;">제목</th>
        <th scope="col" style="width: 100px;">작성자</th>
        <th scope="col" style="width: 100px;">국가</th>
        <th scope="col" style="width: 80px;">카테고리</th>
        <th scope="col" style="width: 80px;">인기점수</th>
        <th scope="col" style="width: 100px;">작성일</th>
        <th scope="col" style="width: 100px;">관리</th>
      </tr>
      </thead>
      <tbody>
      <c:choose>
        <c:when test="${empty posts}">
          <tr>
            <td colspan="11" class="text-center py-5">
              <div class="empty-state">
                <i class="bi bi-graph-up fs-1"></i>
                <p class="mt-3">
                  <c:choose>
                    <c:when test="${not empty search}">
                      검색 조건에 맞는 인기 게시글이 없습니다.
                    </c:when>
                    <c:otherwise>
                      해당 기간의 인기 게시글이 없습니다.
                    </c:otherwise>
                  </c:choose>
                </p>
              </div>
            </td>
          </tr>
        </c:when>
        <c:otherwise>
          <c:forEach var="post" items="${posts}" varStatus="status">
            <tr class="user-row" data-post-id="${post.postId}">
              <td class="text-center">
                <c:set var="rank" value="${(currentPage - 1) * 20 + status.index + 1}" />
                <span class="rank-badge">${rank}</span>
              </td>
              <td>
                <div class="post-name" title="${post.title}">
                  <c:choose>
                    <c:when test="${not empty post.title}">
                      <c:choose>
                        <c:when test="${post.title.length() > 10}">
                          ${post.title.substring(0, 10)}...
                        </c:when>
                        <c:otherwise>
                          ${post.title}
                        </c:otherwise>
                      </c:choose>
                    </c:when>
                    <c:otherwise>
                      <span class="text-muted">제목 미설정</span>
                    </c:otherwise>
                  </c:choose>
                </div>
              </td>
              <td>
                <div class="post-authorName">
                  <c:choose>
                    <c:when test="${not empty post.user.nickname}">
                      ${post.user.nickname}
                    </c:when>
                    <c:when test="${not empty post.user.name}">
                      ${post.user.name}
                    </c:when>
                    <c:otherwise>
                      <span class="text-muted">알 수 없음</span>
                    </c:otherwise>
                  </c:choose>
                </div>
              </td>
              <td>
                <div class="post-country">
                  <c:if test="${not empty post.country}">
                    <small class="text-muted">
                        ${post.country}
                    </small>
                  </c:if>
                </div>
              </td>

              <td class="text-center">
                <c:choose>
                  <c:when test="${post.category.name() == 'READY'}">
                    <span class="badge bg-info">여행준비</span>
                  </c:when>
                  <c:when test="${post.category.name() == 'REVIEW'}">
                    <span class="badge bg-success">여행후기</span>
                  </c:when>
                  <c:when test="${post.category.name() == 'ACCOMPANY'}">
                    <span class="badge bg-warning text-dark">동행구하기</span>
                  </c:when>
                  <c:when test="${post.category.name() == 'FREE'}">
                    <span class="badge bg-secondary">자유게시판</span>
                  </c:when>
                  <c:otherwise>
                    <span class="badge bg-light text-dark">${post.category.name()}</span>
                  </c:otherwise>
                </c:choose>
              </td>

              <!-- 인기점수 (조회수*2 + 좋아요*3 + 댓글*1) -->
              <td class="text-center">
                <c:set var="popularityScore"
                       value="${(post.viewCount != null ? post.viewCount : 0) * 2 +
                               (post.likeCount != null ? post.likeCount : 0) * 3 +
                               (post.commentCount != null ? post.commentCount : 0) * 1}" />
                <span>${popularityScore}</span>
              </td>

              <!-- 작성일 -->
              <td class="text-center">
                <c:choose>
                  <c:when test="${post.createdAt != null}">
                    <div><fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd" /></div>
                    <small class="text-muted"><fmt:formatDate value="${post.createdAt}" pattern="HH:mm" /></small>
                  </c:when>
                  <c:otherwise>-</c:otherwise>
                </c:choose>
              </td>

              <td class="text-center">
                <div class="action-buttons">
                  <button type="button"
                          class="btn btn-primary btn-detail"
                          title="게시글 상세정보 보기"
                          onclick="location.href='/admin/postDetail/${post.postId}'">
                    상세보기
                  </button>
                </div>
              </td>
            </tr>
          </c:forEach>
        </c:otherwise>
      </c:choose>
      </tbody>
    </table>
  </div>
</div>

<!-- 페이지네이션 -->
<c:if test="${totalPages > 1}">
  <nav class="mt-4">
    <ul class="pagination justify-content-center">
      <!-- 맨 처음 -->
      <c:if test="${currentPage > 1}">
        <li class="page-item">
          <a class="page-link" href="?page=1${searchParams}">
            <i class="bi bi-chevron-double-left"></i>
          </a>
        </li>
      </c:if>

      <!-- 이전 페이지 -->
      <c:if test="${hasPrevious}">
        <li class="page-item">
          <a class="page-link" href="?page=${currentPage - 1}${searchParams}">
            <i class="bi bi-chevron-left"></i>
          </a>
        </li>
      </c:if>

      <!-- 페이지 번호들 -->
      <c:forEach begin="${currentPage > 5 ? currentPage - 4 : 1}"
                 end="${currentPage + 4 < totalPages ? currentPage + 4 : totalPages}"
                 var="pageNum">
        <li class="page-item ${pageNum == currentPage ? 'active' : ''}">
          <a class="page-link" href="?page=${pageNum}${searchParams}">
              ${pageNum}
          </a>
        </li>
      </c:forEach>

      <!-- 다음 페이지 -->
      <c:if test="${hasNext}">
        <li class="page-item">
          <a class="page-link" href="?page=${currentPage + 1}${searchParams}">
            <i class="bi bi-chevron-right"></i>
          </a>
        </li>
      </c:if>

      <!-- 맨 끝 -->
      <c:if test="${currentPage < totalPages}">
        <li class="page-item">
          <a class="page-link" href="?page=${totalPages}${searchParams}">
            <i class="bi bi-chevron-double-right"></i>
          </a>
        </li>
      </c:if>
    </ul>
  </nav>
</c:if>

<!-- 페이지 정보 -->
<div class="text-center mt-3">
  <small class="text-muted">
    <c:choose>
      <c:when test="${totalCount > 0}">
        ${startRow}~${endRow}번째 항목 (전체 ${totalCount}개)
      </c:when>
      <c:otherwise>
        검색 결과가 없습니다.
      </c:otherwise>
    </c:choose>
  </small>
</div>

<script>
  $(document).ready(function() {
    console.log('관리자 인기 게시글 목록 페이지 로드 완료');

    // 검색 폼 엔터 키 처리
    $('#searchForm input[name="search"]').keypress(function(e) {
      if (e.which === 13) {
        $('#searchForm').submit();
      }
    });

    // 필터 변경 시 자동 검색
    $('#searchForm select').change(function() {
      $('#searchForm').submit();
    });
  });

  /**
   * 게시글 상세보기
   */
  function viewPost(postId) {
    // 우선 새 창으로 일반 사용자 상세페이지 열기
    window.open('/post/detail/' + postId, '_blank');
  }
</script>