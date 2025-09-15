<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>추천받은 여행지</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            color: #333;
            margin: 0;
            padding: 0;
        }

        .container {
            display: flex;
            flex-direction: column;
            padding: 20px;
            max-width: 1400px;
            margin: 0 auto;
        }

        .top {
            max-width: 1080px;
            margin-left: 160px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .recommend-button {
            background-color: black;
            color: white;
            padding: 10px 20px;
            font-weight: bold;
            text-decoration: none;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .recommend-button:hover {
            transform: translateY(-2px);
        }

        .page-title {
            font-size: 2rem;
            font-weight: bold;
            color: #333;
            margin-bottom: 20px;
            text-align: center;
        }

        #recommendations-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
            gap: 20px;
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .recommendation-card {
            position: relative;
            border: 1px solid #eee;
            border-radius: 8px;
            overflow: hidden;
            aspect-ratio: 4 / 3;
            background: #fff;
            transition: all 0.3s ease;
        }

        .recommendation-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
        }

        .card-image {
            position: relative;
            width: 100%;
            height: 100%;
            background: #e9ecef;
            overflow: hidden;
        }

        .card-image img {
            position: absolute;
            inset: 0;
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .destination-type {
            position: absolute;
            top: 10px;
            right: 10px;
            background: rgba(0, 0, 0, 0.7);
            color: #fff;
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 0.8rem;
            font-weight: bold;
        }

        /* 오버레이 */
        .card-content {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.6);
            color: #fff;
            opacity: 0;
            transition: opacity 0.2s;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            padding: 15px;
        }

        .recommendation-card:hover .card-content {
            opacity: 1;
        }

        .overlay-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
        }

        .destination-name {
            font-size: 1.1em;
            font-weight: bold;
        }

        .destination-city {
            font-size: 0.9em;
            color: #ccc;
        }

        .overlay-date {
            font-size: 0.85em;
            flex-shrink: 0;
            margin-left: auto;
            padding: 3px 25px;
        }

        .overlay-body {
            flex: 1;
            margin-top: 10px;
            font-size: 0.9em;
            line-height: 1.4;
        }

        .overlay-stars::before {
            content: "★★★★★"; /* 전체 별 5개 */
            letter-spacing: 2px;
            background: linear-gradient(90deg, #f5c518 calc(var(--rating) * 20%), #888 calc(var(--rating) * 20%));
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .overlay-rating {
            --rating: attr(data-rating number); /* ✅ JSP의 rating 값 가져옴 */
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            grid-column: 1 / -1;
        }

        .pagination {
            display: flex;
            justify-content: center;
            margin-top: 30px;
            gap: 5px;
        }

        .pagination a,
        .pagination span {
            padding: 10px 15px;
            text-decoration: none;
            border-radius: 8px;
            transition: all 0.3s ease;
            font-weight: bold;
        }

        .pagination a {
            background-color: #fff;
            color: #007bff;
            border: 2px solid #007bff;
        }

        .pagination a:hover {
            background-color: #007bff;
            color: #fff;
            transform: translateY(-2px);
        }

        .pagination .active {
            background-color: #007bff;
            color: #fff;
            border: 2px solid #007bff;
        }

        .pagination .disabled {
            background-color: #f8f9fa;
            color: #6c757d;
            border: 2px solid #dee2e6;
            cursor: not-allowed;
        }

        .overlay-tags {
            margin-top: 8px;
        }

        .overlay-tag {
            display: inline-block;
            background: rgba(255, 255, 255, 0.2); /* 반투명 배경 */
            padding: 4px 8px;
            border-radius: 4px;
            margin-right: 5px;
            font-size: 0.75em;
            color: #fff;
            white-space: nowrap;
        }

        .delete-button {
            position: absolute;
            bottom: 10px;
            right: 10px;
            background-color: #dc3545; /* 빨간색 */
            color: white;
            border: none;
            padding: 8px 14px;
            font-size: 0.9em;
            font-weight: bold;
            border-radius: 6px;
            cursor: pointer;
            opacity: 0;
            transition: opacity 0.2s ease;
        }

        .recommendation-card:hover .delete-button {
            opacity: 1;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="top">
        <h1 class="page-title">추천받은 여행지</h1>
        <a href="/recommend" class="recommend-button">추천받기</a>
    </div>

    <c:set var="itemCount" value="${fn:length(recommendHistory)}"/>
    <c:choose>
        <c:when test="${itemCount == 1}">
            <c:set var="gridClass" value="single-item"/>
        </c:when>
        <c:when test="${itemCount == 2}">
            <c:set var="gridClass" value="two-items"/>
        </c:when>
        <c:when test="${itemCount == 3}">
            <c:set var="gridClass" value="three-items"/>
        </c:when>
        <c:otherwise>
            <c:set var="gridClass" value=""/>
        </c:otherwise>
    </c:choose>

    <div id="recommendations-container" class="${gridClass}">
        <c:if test="${empty recommendHistory and empty errorMessage}">
            <div class="empty-state">
                <h3>아직 추천받은 여행지가 없어요</h3>
                <p>새로운 여행지 추천을 받아보세요!<br>맞춤형 추천으로 완벽한 여행을 계획해보세요.</p>
            </div>
        </c:if>

        <c:forEach var="recommend" items="${recommendHistory}">
            <div class="recommendation-card">
                <div class="card-image ${empty recommend.countryImg ? 'no-image' : ''}">
                    <c:choose>
                        <c:when test="${not empty recommend.countryImg}">
                            <img src="${recommend.countryImg}" alt="${recommend.countryNameKo}"/>
                        </c:when>
                        <c:otherwise><span>🌍</span></c:otherwise>
                    </c:choose>
                    <c:if test="${not empty recommend.continent}">
                        <span class="destination-type">${recommend.continent}</span>
                    </c:if>
                </div>

                <!-- 오버레이 -->
                <div class="card-content">
                    <div class="overlay-header">
                        <div>
                            <h3 class="destination-name">${recommend.countryNameKo}</h3>
                            <c:if test="${not empty recommend.city}">
                                <p class="destination-city">${recommend.city}</p>
                            </c:if>
                        </div>
                        <div class="overlay-date">
                            <c:choose>
                                <c:when test="${not empty recommend.createdAt}">
                                    <fmt:formatDate value="${recommend.createdAt}" pattern="yyyy.MM.dd"/>
                                </c:when>
                                <c:otherwise>날짜 없음</c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="overlay-body">
                        <c:if test="${not empty recommend.recommendRating}">
                            <div class="overlay-rating" style="--rating:${recommend.recommendRating}">
                                <span class="overlay-stars"></span>
                                <span class="overlay-rating-text">${recommend.recommendRating}/5</span>
                            </div>
                        </c:if>
                        <div class="overlay-tags">
                            <c:if test="${not empty recommend.travelPurpose}">
                                <span class="overlay-tag">${recommend.travelPurpose}</span>
                            </c:if>
                            <c:if test="${not empty recommend.companion}">
                                <span class="overlay-tag">${recommend.companion}</span>
                            </c:if>
                        </div>
                    </div>
                </div>
                <button class="delete-button" onclick="deleteRecommend(${recommend.recommendId})">삭제</button>
            </div>
        </c:forEach>
    </div>

    <!-- 페이지네이션 -->
    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <c:choose>
                <c:when test="${hasPrevious}">
                    <a href="?page=1">&laquo;</a>
                    <a href="?page=${currentPage-1}">&lt;</a>
                </c:when>
                <c:otherwise>
                    <span class="disabled">&laquo;</span>
                    <span class="disabled">&lt;</span>
                </c:otherwise>
            </c:choose>

            <c:forEach var="i" begin="${currentPage - 2 < 1 ? 1 : currentPage - 2}"
                       end="${currentPage + 2 > totalPages ? totalPages : currentPage + 2}">
                <c:choose>
                    <c:when test="${currentPage == i}">
                        <span class="active">${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="?page=${i}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:choose>
                <c:when test="${hasNext}">
                    <a href="?page=${currentPage+1}">&gt;</a>
                    <a href="?page=${totalPages}">&raquo;</a>
                </c:when>
                <c:otherwise>
                    <span class="disabled">&gt;</span>
                    <span class="disabled">&raquo;</span>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>
</div>
</body>
<script>
    function deleteRecommend(id) {
        if (confirm("정말 삭제하시겠습니까?")) {
            fetch("/api/recommend/delete/" + id, {method: "POST"})
                .then(res => res.text())
                .then(msg => {
                    alert(msg);
                    location.reload(); // 삭제 후 새로고침
                });
        }
    }
</script>
</html>
