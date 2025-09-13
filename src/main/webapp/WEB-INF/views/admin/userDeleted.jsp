<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="/css/admin/userDeleted.css"/>

<!-- 검색 및 필터 섹션 -->
<div class="search-filter-section">
    <div class="d-flex justify-content-between align-items-center">
        <div class="result-summary">
            <span class="text-muted">총 <strong>${totalCount != null ? totalCount : 0}</strong>명의 탈퇴 사용자</span>
        </div>
        <form method="get" class="search-form" id="searchForm">
            <div class="input-group" style="width: 300px;">
                <span class="input-group-text"><i class="bi bi-search"></i></span>
                <input type="text" class="form-control" name="search"
                       placeholder="이름, 닉네임, 이메일 검색" value="${search}">
            </div>
        </form>
    </div>
</div>

<!-- 탈퇴 사용자 테이블 -->
<div class="deleted-users-table-container">
    <div class="table-responsive">
        <table class="table table-hover align-middle" id="deletedUsersTable">
            <thead class="table-danger">
            <tr>
                <th scope="col">ID</th>
                <th scope="col">이름</th>
                <th scope="col">닉네임</th>
                <th scope="col">이메일</th>
                <th scope="col">등급</th>
                <th scope="col">가입일</th>
                <th scope="col">탈퇴일</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty users}">
                    <tr>
                        <td colspan="8" class="text-center py-5">
                            <div class="empty-state">
                                <i class="bi bi-person-x fs-1 text-muted"></i>
                                <p class="mt-3 text-muted">
                                    <c:choose>
                                        <c:when test="${not empty search}">
                                            검색 조건에 맞는 탈퇴 사용자가 없습니다.
                                        </c:when>
                                        <c:otherwise>
                                            탈퇴한 사용자가 없습니다.
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="user" items="${users}" varStatus="status">
                        <tr class="deleted-user-row" data-user-id="${user.user_id}">
                            <td>
                                <span class="user-id-badge deleted">${user.user_id}</span>
                            </td>
                            <td>
                                <div class="user-name deleted">
                                    <c:choose>
                                        <c:when test="${not empty user.name}">
                                            <span class="deleted-text">${user.name}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">이름 미설정</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                            <td>
                                <div class="user-nickname deleted">
                                    <c:choose>
                                        <c:when test="${not empty user.nickname}">
                                            <span class="deleted-text">${user.nickname}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">닉네임 미설정</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                            <td>
                                <div class="user-email deleted">
                                    <span class="deleted-text">${user.email}</span>
                                </div>
                            </td>
                            <td>
                                <span class="badge grade-badge deleted" data-grade="${user.grade}">
                                        ${user.gradeKorean}
                                </span>
                            </td>
                            <td>
                                <div class="join-date">
                                    <c:choose>
                                        <c:when test="${user.createdAt != null}">
                                            ${user.createdAt.toLocalDate()}
                                        </c:when>
                                        <c:otherwise>
                                            -
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="join-time">
                                    <c:if test="${user.createdAt != null}">
                                        ${user.createdAt.toLocalTime().toString().substring(0, 5)}
                                    </c:if>
                                </div>
                            </td>
                            <td>
                                <div class="deleted-date">
                                    <c:choose>
                                        <c:when test="${user.deletedAt != null}">
                                            <span class="text-danger fw-bold">
                                                    ${user.deletedAt.toLocalDate()}
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            -
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="deleted-time">
                                    <c:if test="${user.deletedAt != null}">
                                        <span class="text-danger">
                                                ${user.deletedAt.toLocalTime().toString().substring(0, 5)}
                                        </span>
                                    </c:if>
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
    <nav aria-label="탈퇴 사용자 목록 페이지네이션" class="mt-4">
        <ul class="pagination justify-content-center">
            <c:if test="${hasPrevious}">
                <li class="page-item">
                    <a class="page-link" href="?page=${currentPage - 1}${searchParams}">이전</a>
                </li>
            </c:if>

            <c:forEach begin="${currentPage > 5 ? currentPage - 4 : 1}"
                       end="${currentPage + 4 < totalPages ? currentPage + 4 : totalPages}"
                       var="pageNum">
                <li class="page-item ${pageNum == currentPage ? 'active' : ''}">
                    <a class="page-link" href="?page=${pageNum}${searchParams}">
                            ${pageNum}
                    </a>
                </li>
            </c:forEach>

            <c:if test="${hasNext}">
                <li class="page-item">
                    <a class="page-link" href="?page=${currentPage + 1}${searchParams}">다음</a>
                </li>
            </c:if>
        </ul>
    </nav>
</c:if>

<!-- 페이지 정보 -->
<div class="page-info text-center mt-3">
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
        console.log('탈퇴 사용자 페이지 로드 완료');
        console.log('총 탈퇴 사용자 수:', '${totalCount}');
    });

    function refreshList() {
        location.reload();
    }
</script>