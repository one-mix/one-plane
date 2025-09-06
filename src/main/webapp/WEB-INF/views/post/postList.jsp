<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<link href="/css/postList.css" rel="stylesheet">


<main class="main-container">
    <div class="top-tabs-wrapper">
        <div class="top-tabs">
            <a href="/post/list" class="top-tab ${empty selectedCategory ? 'active' : ''}">전체</a>
            <a href="/post/list?category=READY" class="top-tab ${selectedCategory eq 'READY' ? 'active' : ''}">준비</a>
            <a href="/post/list?category=REVIEW" class="top-tab ${selectedCategory eq 'REVIEW' ? 'active' : ''}">후기</a>
            <a href="/post/list?category=ACCOMPANY" class="top-tab ${selectedCategory eq 'ACCOMPANY' ? 'active' : ''}">동행</a>
            <a href="/post/list?category=FREE" class="top-tab ${selectedCategory eq 'FREE' ? 'active' : ''}">자유</a>
        </div>

        <div class="search-area">
            <form method="get" action="/post/list" style="display: flex; gap: 12px; align-items: center;">
                <input type="hidden" name="category" value="${selectedCategory}">
                <select name="searchType" class="country-dropdown">
                    <option value="title" ${searchType eq 'title' ? 'selected' : ''}>제목</option>
                    <option value="content" ${searchType eq 'content' ? 'selected' : ''}>내용</option>
                    <option value="author" ${searchType eq 'author' ? 'selected' : ''}>작성자</option>
                </select>
                <input type="text" name="search" class="search-input-top" placeholder="입력해 주세요" value="${searchKeyword}" onkeypress="if(event.keyCode==13) this.form.submit();">
                <a href="/post/write" class="search-btn-top" style="text-decoration: none;">글쓰기</a>
            </form>
        </div>
    </div>

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
                        <!-- 사진이 없다면 빈 사진으로 -->
                        <c:if test="${empty post.thumbnailImage}">
                            <div class="result-icon">
                                <img src="/images/aimg.png" alt="no image" style="width: 60px; height: 60px; object-fit: cover; border-radius: 8px; opacity: 0.3;">
                            </div>
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
                        <a href="/post/write" class="search-btn-top" style="margin-top: 20px; display: inline-block;">글쓰기</a>
                    </sec:authorize>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${totalPages > 1}">
        <div class="pagination-container">
            <c:choose>
                <c:when test="${currentPage > 1}">
                    <a href="?page=1&category=${selectedCategory}&searchType=${searchType}&search=${searchKeyword}&sortBy=${sortBy}" class="pagination-btn">«</a>
                </c:when>
                <c:otherwise>
                    <span class="pagination-btn disabled">«</span>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${hasPrevious}">
                    <a href="?page=${currentPage - 1}&category=${selectedCategory}&searchType=${searchType}&search=${searchKeyword}&sortBy=${sortBy}" class="pagination-btn">‹</a>
                </c:when>
                <c:otherwise>
                    <span class="pagination-btn disabled">‹</span>
                </c:otherwise>
            </c:choose>

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

            <c:choose>
                <c:when test="${hasNext}">
                    <a href="?page=${currentPage + 1}&category=${selectedCategory}&searchType=${searchType}&search=${searchKeyword}&sortBy=${sortBy}" class="pagination-btn">›</a>
                </c:when>
                <c:otherwise>
                    <span class="pagination-btn disabled">›</span>
                </c:otherwise>
            </c:choose>

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

<script>
    document.querySelectorAll('.result-card').forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
        });

        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });

    document.querySelector('form').addEventListener('submit', function(e) {
        const searchInput = document.querySelector('input[name="search"]');
        if (searchInput && searchInput.value.trim() === '') {
            return true;
        }
    });
</script>