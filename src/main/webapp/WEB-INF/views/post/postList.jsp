<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>OnePlane - 게시판</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background-color: #ffffff;
        }

        /* Header */
        .header {
            background: white;
            border-bottom: 1px solid #e9ecef;
            padding: 12px 0;
        }

        .header-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            display: flex;
            align-items: center;
            font-size: 20px;
            font-weight: 600;
            color: #2563eb;
            text-decoration: none;
        }

        .logo svg {
            width: 24px;
            height: 24px;
            margin-right: 8px;
            fill: #2563eb;
        }

        .nav-menu {
            display: flex;
            gap: 40px;
        }

        .nav-menu a {
            text-decoration: none;
            color: #6b7280;
            font-size: 16px;
            font-weight: 400;
        }

        .nav-menu a.active {
            color: #2563eb;
            border-bottom: 2px solid #2563eb;
            padding-bottom: 12px;
        }

        .login-btn {
            color: #6b7280;
            text-decoration: none;
            font-size: 14px;
        }

        /* Main Content */
        .main-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 40px 20px;
        }

        /* Top Tab Section */
        .top-tabs-wrapper {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .top-tabs {
            display: flex;
            gap: 8px;
            align-items: center;
            background: #f8fafc;
            padding: 8px;
            border-radius: 50px;
            flex-shrink: 0;
        }

        .top-tab {
            padding: 12px 24px;
            border: none;
            border-radius: 25px;
            background: transparent;
            color: #64748b;
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.2s ease;
            min-width: 80px;
            text-align: center;
            display: inline-block;
        }

        .top-tab:hover {
            color: #334155;
            background: rgba(255, 255, 255, 0.5);
        }

        .top-tab.active {
            background: #ffffff;
            color: #1e293b;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            border: 1px solid #e2e8f0;
        }

        .search-area {
            display: flex;
            gap: 16px;
            align-items: center;
            margin-left: auto;
        }

        .country-dropdown {
            padding: 8px 16px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            background: white;
            color: #6b7280;
            font-size: 14px;
            min-width: 80px;
        }

        .search-input-top {
            padding: 8px 16px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            width: 200px;
            font-size: 14px;
        }

        .search-btn-top {
            padding: 8px 20px;
            background: #205AA4;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
        }

        .search-btn-top:hover {
            background: #1845a0;
            color: white;
        }

        /* Results Section */
        .results-section {
            margin-bottom: 40px;
            padding: 0 100px;
        }

        .result-card {
            background: white;
            border: 1px solid #f3f4f6;
            border-radius: 8px;
            padding: 24px;
            margin-bottom: 12px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            cursor: pointer;
            transition: all 0.2s ease;
        }

        .result-card:hover {
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            border-color: #e5e7eb;
        }

        .result-info h3 {
            font-size: 16px;
            font-weight: 600;
            color: #1f2937;
            margin: 0 0 8px 0;
        }

        .result-info p {
            font-size: 14px;
            color: #6b7280;
            margin: 0;
            line-height: 1.5;
        }

        .result-details {
            font-size: 12px;
            color: #9ca3af;
            margin-top: 12px;
        }

        .result-icon {
            width: 24px;
            height: 24px;
            opacity: 0.3;
        }

        .category-badge {
            display: inline-block;
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 500;
            margin-right: 8px;
        }

        .category-READY { background: #dbeafe; color: #1e40af; }
        .category-REVIEW { background: #dcfce7; color: #166534; }
        .category-ACCOMPANY { background: #fef3c7; color: #92400e; }
        .category-FREE { background: #e5e7eb; color: #374151; }

        /* Pagination */
        .pagination-container {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 8px;
        }

        .pagination-btn {
            width: 32px;
            height: 32px;
            border: none;
            background: none;
            color: #6b7280;
            cursor: pointer;
            border-radius: 4px;
            display: flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
        }

        .pagination-btn:hover {
            background: #f3f4f6;
            color: #6b7280;
        }

        .pagination-btn.active {
            background: #2563eb;
            color: white;
        }

        .pagination-btn.disabled {
            opacity: 0.5;
            cursor: not-allowed;
            pointer-events: none;
        }

        /* 빈 상태 메시지 */
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #6b7280;
        }

        .empty-state h3 {
            font-size: 18px;
            margin-bottom: 8px;
            color: #374151;
        }

        .empty-state p {
            font-size: 14px;
        }
    </style>
</head>
<body>
<!-- Header -->
<header class="header">
    <div class="header-container">
        <a href="/" class="logo">
            <svg viewBox="0 0 24 24">
                <img src="/images/logo.png" alt="OnePlane Logo">
            </svg>
            OnePlane
        </a>
        <nav class="nav-menu">
            <a href="/">지도</a>
            <a href="/">추천</a>
            <a href="/post/list" class="active">게시판</a>
        </nav>

        <!-- 로그인 상태에 따른 메뉴 -->
        <sec:authorize access="!isAuthenticated()">
            <a href="/oauth2/authorization/kakao" class="login-btn">로그인</a>
        </sec:authorize>

        <sec:authorize access="isAuthenticated()">
            <div class="dropdown">
                <a href="#" class="login-btn dropdown-toggle" data-bs-toggle="dropdown">
                        ${sessionScope.userName != null ? sessionScope.userName : sessionScope.userNickname}님
                </a>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="/user/profile">프로필</a></li>
                    <li><a class="dropdown-item" href="/logout">로그아웃</a></li>
                </ul>
            </div>
        </sec:authorize>
    </div>
</header>

<!-- Main Content -->
<main class="main-container">
    <!-- Top Tab Section -->
    <div class="top-tabs-wrapper">
        <div class="top-tabs">
            <a href="/post/list" class="top-tab ${empty selectedCategory ? 'active' : ''}">전체</a>
            <a href="/post/list?category=READY" class="top-tab ${selectedCategory eq 'READY' ? 'active' : ''}">준비</a>
            <a href="/post/list?category=REVIEW" class="top-tab ${selectedCategory eq 'REVIEW' ? 'active' : ''}">후기</a>
            <a href="/post/list?category=ACCOMPANY" class="top-tab ${selectedCategory eq 'ACCOMPANY' ? 'active' : ''}">동행</a>
            <a href="/post/list?category=FREE" class="top-tab ${selectedCategory eq 'FREE' ? 'active' : ''}">자유</a>
        </div>

        <div class="search-area">
            <form method="get" action="/post/list" style="display: flex; gap: 16px; align-items: center;">
                <input type="hidden" name="category" value="${selectedCategory}">
                <select name="searchType" class="country-dropdown">
                    <option value="title" ${searchType eq 'title' ? 'selected' : ''}>제목</option>
                    <option value="content" ${searchType eq 'content' ? 'selected' : ''}>내용</option>
                    <option value="author" ${searchType eq 'author' ? 'selected' : ''}>작성자</option>
                </select>
                <input type="text" name="search" class="search-input-top" placeholder="입력해 주세요" value="${searchKeyword}">
                <a href="/post/write" class="search-btn-top" style="text-decoration: none;">글쓰기</a>
            </form>
        </div>
    </div>

    <!-- Results Section -->
    <div class="results-section">
        <c:choose>
            <c:when test="${not empty posts}">
                <c:forEach var="post" items="${posts}">
                    <div class="result-card" onclick="location.href='/post/detail/${post.postId}'">
                        <div class="result-info">
                            <div style="margin-bottom: 8px;">
                                <span class="category-badge category-${post.category}">
                                    <c:choose>
                                        <c:when test="${post.category eq 'READY'}">준비</c:when>
                                        <c:when test="${post.category eq 'REVIEW'}">후기</c:when>
                                        <c:when test="${post.category eq 'ACCOMPANY'}">동행</c:when>
                                        <c:when test="${post.category eq 'FREE'}">자유</c:when>
                                        <c:otherwise>${post.category}</c:otherwise>
                                    </c:choose>
                                </span>
                            </div>

                            <h3>${post.title}</h3>

                            <p>
                                <c:set var="content" value="${post.content}" />
                                <c:set var="plainText" value="${content.replaceAll('<[^>]*>', '').replaceAll('&nbsp;', ' ')}" />
                                <c:choose>
                                    <c:when test="${plainText.length() > 100}">
                                        ${plainText.substring(0, 100)}...
                                    </c:when>
                                    <c:otherwise>
                                        ${plainText}
                                    </c:otherwise>
                                </c:choose>
                            </p>

                            <div class="result-details">
                                    ${post.authorName} &nbsp;&nbsp;
                                <fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd HH:mm" /> &nbsp;&nbsp;
                                조회 ${post.viewCount} &nbsp;&nbsp;
                                댓글 ${post.commentCount} &nbsp;&nbsp;
                                좋아요 ${post.likeCount}
                            </div>
                        </div>

                        <c:if test="${not empty post.thumbnailImage}">
                            <div class="result-icon">
                                <img src="${post.thumbnailImage}" alt="thumbnail" style="width: 60px; height: 60px; object-fit: cover; border-radius: 8px;">
                            </div>
                        </c:if>
                            <%-- 사진이 없다면 빈 사진으로 --%>
                        <c:if test="${empty post.thumbnailImage}">
                            <svg class="result-icon" viewBox="0 0 24 24" fill="currentColor">
                                <img src="/images/aimg.png">
                            </svg>
                        </c:if>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <h3>게시글이 없습니다</h3>
                    <p>
                        <c:choose>
                            <c:when test="${not empty selectedCategory}">
                                선택한 카테고리에 게시글이 없습니다.
                            </c:when>
                            <c:otherwise>
                                아직 작성된 게시글이 없습니다. 첫 번째 게시글을 작성해보세요!
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <sec:authorize access="isAuthenticated()">
                        <a href="/post/write" class="search-btn-top" style="margin-top: 20px; display: inline-block;">첫 글 작성하기</a>
                    </sec:authorize>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Pagination -->
    <c:if test="${totalPages > 1}">
        <div class="pagination-container">
            <!-- 첫 페이지로 -->
            <c:choose>
                <c:when test="${currentPage > 1}">
                    <a href="?page=1&category=${selectedCategory}&searchType=${searchType}&search=${searchKeyword}&sortBy=${sortBy}" class="pagination-btn">«</a>
                </c:when>
                <c:otherwise>
                    <span class="pagination-btn disabled">«</span>
                </c:otherwise>
            </c:choose>

            <!-- 이전 페이지 -->
            <c:choose>
                <c:when test="${hasPrevious}">
                    <a href="?page=${currentPage - 1}&category=${selectedCategory}&searchType=${searchType}&search=${searchKeyword}&sortBy=${sortBy}" class="pagination-btn">‹</a>
                </c:when>
                <c:otherwise>
                    <span class="pagination-btn disabled">‹</span>
                </c:otherwise>
            </c:choose>

            <!-- 페이지 번호들 -->
            <c:set var="startPage" value="${((currentPage - 1) / 5) * 5 + 1}" />
            <c:set var="endPage" value="${startPage + 4 > totalPages ? totalPages : startPage + 4}" />

            <c:forEach var="i" begin="${startPage}" end="${endPage}">
                <c:choose>
                    <c:when test="${i eq currentPage}">
                        <span class="pagination-btn active">${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="?page=${i}&category=${selectedCategory}&searchType=${searchType}&search=${searchKeyword}&sortBy=${sortBy}" class="pagination-btn">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <!-- 다음 페이지 -->
            <c:choose>
                <c:when test="${hasNext}">
                    <a href="?page=${currentPage + 1}&category=${selectedCategory}&searchType=${searchType}&search=${searchKeyword}&sortBy=${sortBy}" class="pagination-btn">›</a>
                </c:when>
                <c:otherwise>
                    <span class="pagination-btn disabled">›</span>
                </c:otherwise>
            </c:choose>

            <!-- 마지막 페이지로 -->
            <c:choose>
                <c:when test="${currentPage < totalPages}">
                    <a href="?page=${totalPages}&category=${selectedCategory}&searchType=${searchType}&search=${searchKeyword}&sortBy=${sortBy}" class="pagination-btn">»</a>
                </c:when>
                <c:otherwise>
                    <span class="pagination-btn disabled">»</span>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>
</main>

<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
<script>
    // 게시글 카드 호버 효과
    document.querySelectorAll('.result-card').forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
        });

        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });

    // 검색 폼 유효성 검사
    document.querySelector('form').addEventListener('submit', function(e) {
        const searchInput = document.querySelector('input[name="search"]');
        if (searchInput.value.trim() === '') {
            e.preventDefault();
            alert('검색어를 입력해주세요.');
            searchInput.focus();
        }
    });
</script>

</body>
</html>