<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="/css/admin/userList.css"/>

<!-- 검색 및 필터 섹션 -->
<div class="search-filter-section">
    <div class="d-flex justify-content-between align-items-center">
        <div class="result-summary">
            <span class="text-muted">총 <strong>${totalCount != null ? totalCount : 0}</strong>명의 사용자</span>
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

<!-- 사용자 테이블 -->
<div class="users-table-container">
    <div class="table-responsive">
        <table class="table table-hover align-middle" id="usersTable">
            <thead class="table-primary">
            <tr>
                <th scope="col" style="width: 80px;">ID</th>
                <th scope="col" style="width: 80px;">이름</th>
                <th scope="col" style="width: 80px;">닉네임</th>
                <th scope="col" style="width: 180px;">이메일</th>
                <th scope="col" style="width: 80px;">등급</th>
                <th scope="col" style="width: 100px;">가입일</th>
                <th scope="col" style="width: 100px;">관리</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty users}">
                    <tr>
                        <td colspan="9" class="text-center py-5">
                            <div class="empty-state">
                                <i class="bi bi-people fs-1"></i>
                                <p class="mt-3">
                                    <c:choose>
                                        <c:when test="${not empty search}">
                                            검색 조건에 맞는 사용자가 없습니다.
                                        </c:when>
                                        <c:otherwise>
                                            등록된 사용자가 없습니다.
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="user" items="${users}" varStatus="status">
                        <tr class="user-row" data-user-id="${user.user_id}">
                            <td>
                                <span class="user-id-badge">${user.user_id}</span>
                            </td>
                            <td>
                                <div class="user-name">
                                    <c:choose>
                                        <c:when test="${not empty user.name}">
                                            ${user.name}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">이름 미설정</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                            <td>
                                <div class="user-nickname">
                                    <c:choose>
                                        <c:when test="${not empty user.nickname}">
                                            ${user.nickname}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">닉네임 미설정</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                            <td>
                                <div class="user-email">
                                        ${user.email}
                                </div>
                            </td>
                            <td class="text-center">
                                <span class="badge grade-badge" data-grade="${user.grade}">
                                        ${user.gradeKorean}
                                </span>
                            </td>
                            <td class="text-center">
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
                            <td class="text-center">
                                <div class="action-buttons">
                                    <button type="button"
                                            class="btn btn-primary btn-detail"
                                            title="사용자 상세정보 보기"
                                            onclick="location.href='/admin/userDetail/${user.user_id}'">
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
    <nav aria-label="사용자 목록 페이지네이션" class="mt-4">
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
        console.log('전체 사용자 페이지 로드 완료');
        console.log('총 사용자 수:', '${totalCount}');

        // 툴팁 초기화
        $('[data-bs-toggle="tooltip"]').tooltip();

        // 검색 폼 엔터 키 처리
        $('#searchForm input[name="search"]').keypress(function(e) {
            if (e.which === 13) {
                $('#searchForm').submit();
            }
        });
    });
    /**
     * 목록 새로고침
     */
    function refreshList() {
        location.reload();
    }

</script>