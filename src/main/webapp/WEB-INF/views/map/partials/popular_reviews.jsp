<%@ page contentType="text/html; charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
</head>
<body>
    <div class="popular-reviews">
        <span class="review-title">인기 후기</span>

        <div class="carousel">

        <%-- 왼쪽 화살표 --%>
        <button class="carousel-btn prev">❮</button>

            <div class="carousel-track-container">
                <div class="carousel-track">
                <%-- 인기 후기 카드들 (최대 9개) --%>
                <c:choose>
                    <c:when test="${empty popularReviews}">
                        <div class="no-reviews">등록된 후기가 없습니다.</div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="review" items="${popularReviews}" varStatus="status">
                            <a href="/post/detail/${review.postId}" class="review-card">
                                <c:choose>
                                    <c:when test="${not empty review.thumbnailImage}">
                                        <img class="thumbnail" src="${review.thumbnailImage}" alt="썸네일">
                                    </c:when>
                                    <c:otherwise>
                                        <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                                    </c:otherwise>
                                </c:choose>
                                <div class="info">
                                    <span class="country">${review.countryName}</span>
                                    <span class="review-card-title" title="${review.title}">
                                        <c:choose>
                                            <c:when test="${fn:length(review.title) > 25}">
                                                ${fn:substring(review.title, 0, 25)}...
                                            </c:when>
                                            <c:otherwise>
                                                ${review.title}
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                    <span class="date"><fmt:formatDate value="${review.createdAt}" pattern="yyyy-MM-dd" /></span>
                                </div>
                            </a>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

           <%-- 인디케이터 (최대 3개 - 9개 카드를 3개씩 나누면 3페이지) --%>
            <div class="carousel-indicators">
                <c:if test="${not empty popularReviews}">
                    <c:choose>
                        <c:when test="${fn:length(popularReviews) <= 3}">
                            <span class="dot active"></span>
                        </c:when>
                        <c:when test="${fn:length(popularReviews) <= 6}">
                            <span class="dot active"></span>
                            <span class="dot"></span>
                        </c:when>
                        <c:otherwise>
                            <span class="dot active"></span>
                            <span class="dot"></span>
                            <span class="dot"></span>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </div>
        </div>

        <%-- 오른쪽 화살표 --%>
        <button class="carousel-btn next">❯</button>
        </div>

           <%-- 캐러셀 호환성 확인 스크립트 --%>
            <script>
                document.addEventListener("DOMContentLoaded", function() {
                    // 캐러셀이 제대로 동작하는지 확인
                    const reviewCards = document.querySelectorAll('.review-card');
                    const dots = document.querySelectorAll('.carousel-indicators .dot');
                    // 리뷰 카드가 없는 경우 캐러셀 버튼과 인디케이터 숨김
                    if (reviewCards.length === 0) {
                        const prevBtn = document.querySelector('.carousel-btn.prev');
                        const nextBtn = document.querySelector('.carousel-btn.next');
                        const indicators = document.querySelector('.carousel-indicators');
                        if (prevBtn) prevBtn.style.display = 'none';
                        if (nextBtn) nextBtn.style.display = 'none';
                        if (indicators) indicators.style.display = 'none';
                    }
                    // 3개 이하인 경우 네비게이션 버튼 숨김 (한 페이지에 다 들어가므로)
                    if (reviewCards.length <= 3) {
                        const prevBtn = document.querySelector('.carousel-btn.prev');
                        const nextBtn = document.querySelector('.carousel-btn.next');
                        if (prevBtn) prevBtn.style.display = 'none';
                        if (nextBtn) nextBtn.style.display = 'none';
                    }
                });
            </script>

    <%-- 커설 js 연결 --%>
    <script src="/js/map/carousel.js"></script>
</body>
</html>