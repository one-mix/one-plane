<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- 검색 / 필터 -->
<form id="searchForm" method="get" action="/admin/countries" class="d-flex align-items-center gap-2 mb-3 w-100">

    <!-- 여행경보 단계 드롭다운 -->
    <select name="levelValue" class="form-select" style="max-width: 200px;"
            onchange="document.getElementById('searchForm').submit()">
        <option value="" <c:if test="${empty selectedLevel}">selected</c:if>>전체</option>
        <option value="여행유의" <c:if test="${selectedLevel eq '여행유의'}">selected</c:if>>여행유의</option>
        <option value="여행자제" <c:if test="${selectedLevel eq '여행자제'}">selected</c:if>>여행자제</option>
        <option value="철수권고" <c:if test="${selectedLevel eq '철수권고'}">selected</c:if>>철수권고</option>
        <option value="여행금지" <c:if test="${selectedLevel eq '여행금지'}">selected</c:if>>여행금지</option>
    </select>

    <!-- 국가명 검색 -->
    <input type="text" name="keyword" class="form-control flex-grow-1"
           placeholder="국가 검색"
           value="${keyword}"/>

    <!-- 검색 버튼 -->
    <button type="submit" class="btn btn-primary flex-shrink-0">검색</button>
</form>

<!-- 국가 리스트 -->
<div class="table-responsive">
    <table id="countryTable" class="table table-striped table-bordered w-100">
        <thead>
        <tr>
            <th>ID</th>
            <th>국가명</th>
            <th>영문명</th>
            <th>ISO 코드</th>
            <th>여행경보</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="c" items="${countries}">
            <tr>
                <td>${c.countryId}</td>
                <td>
                    <a href="/admin/countries/${c.countryId}" class="text-decoration-none">
                            ${c.countryName}
                    </a>
                </td>
                <td>${c.countryEnName}</td>
                <td>${c.isoCode}</td>
                <td>
                    <c:choose>
                        <c:when test="${empty c.levelValue}">-</c:when>
                        <c:otherwise>
                            <span class="badge
                                <c:choose>
                                    <c:when test="${c.levelValue eq '여행유의'}">bg-info</c:when>
                                    <c:when test="${c.levelValue eq '여행자제'}">bg-warning</c:when>
                                    <c:when test="${c.levelValue eq '철수권고'}">bg-danger</c:when>
                                    <c:when test="${c.levelValue eq '여행금지'}">bg-dark</c:when>
                                    <c:otherwise>bg-secondary</c:otherwise>
                                </c:choose>">
                                    ${c.levelValue}
                            </span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script>
    $(function() {
        initializeDataTable("#countryTable", {
            pageLength: 20
        });
    });
</script>
