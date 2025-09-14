<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="/css/admin/commentList.css"/>


<!-- 검색 및 필터 섹션 -->
<div class="search-filter-section">
  <div class="d-flex justify-content-between align-items-center">
    <div class="result-summary">
      <span class="text-muted">총 <strong>${totalCount != null ? totalCount : 0}</strong>개의 댓글</span>
    </div>
    <form method="get" class="search-form" id="searchForm">
      <div class="input-group" style="width: 300px;">
        <span class="input-group-text"><i class="bi bi-search"></i></span>
        <input type="text" class="form-control" name="search"
                value="${search}">
      </div>
    </form>
  </div>
</div>

<!-- 댓글 테이블 -->
<div class="comments-table-container">
  <div class="table-responsive">
    <table class="table table-hover align-middle" id="commentsTable">
      <thead class="table-primary">
      <tr>
        <th scope="col" style="width: 80px;">NO</th>
        <th scope="col" style="width: 150px;">게시글</th>
        <th scope="col" style="width: 190px;">댓글 내용</th>
        <th scope="col" style="width: 100px;">작성자</th>
        <th scope="col" style="width: 100px;">작성일</th>
      </tr>
      </thead>
      <tbody>
      <c:choose>
        <c:when test="${empty comments}">
          <tr>
            <td colspan="6" class="text-center py-5">
              <div class="empty-state">
                <i class="bi bi-chat-dots fs-1"></i>
                <p class="mt-3">
                  <c:choose>
                    <c:when test="${not empty search}">
                      검색 조건에 맞는 댓글이 없습니다.
                    </c:when>
                    <c:otherwise>
                      등록된 댓글이 없습니다.
                    </c:otherwise>
                  </c:choose>
                </p>
              </div>
            </td>
          </tr>
        </c:when>
        <c:otherwise>
          <c:forEach var="comment" items="${comments}" varStatus="status">
            <tr class="comment-row" data-comment-id="${comment.commentId}">
              <td>
                <span class="comment-id-badge">${(currentPage - 1) * 20 + status.index + 1}</span>
                <small class="text-muted d-block">ID: ${comment.commentId}</small>
              </td>
              <td>
                <div class="post-title" title="${comment.postTitle}">
                  <c:choose>
                    <c:when test="${not empty comment.postTitle}">
                      <c:choose>
                        <c:when test="${comment.postTitle.length() > 10}">
                          ${comment.postTitle.substring(0, 10)}...
                        </c:when>
                        <c:otherwise>
                          ${comment.postTitle}
                        </c:otherwise>
                      </c:choose>
                    </c:when>
                    <c:otherwise>
                      <span class="text-muted">제목 없음</span>
                    </c:otherwise>
                  </c:choose>
                </div>
              </td>
              <td>
                <div class="comment-content" title="${comment.content}">
                  <c:choose>
                    <c:when test="${comment.content.length() > 10}">
                      ${comment.content.substring(0, 10)}...
                    </c:when>
                    <c:otherwise>
                      ${comment.content}
                    </c:otherwise>
                  </c:choose>
                </div>
              </td>
              <td>
                <div class="comment-author">${comment.displayName}</div>
              </td>


              <td class="text-center">
                <c:choose>
                  <c:when test="${comment.createdAt != null}">
                    <div><fmt:formatDate value="${comment.createdAt}" pattern="yyyy-MM-dd" /></div>
                    <small class="text-muted"><fmt:formatDate value="${comment.createdAt}" pattern="HH:mm" /></small>
                  </c:when>
                  <c:otherwise>-</c:otherwise>
                </c:choose>
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
    console.log('관리자 댓글 목록 페이지 로드 완료');

    // 검색 폼 엔터 키 처리
    $('#searchForm input[name="search"]').keypress(function(e) {
      if (e.which === 13) {
        $('#searchForm').submit();
      }
    });
  });
</script>