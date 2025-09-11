<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
            justify-content: space-between;  /* 왼쪽에는 제목, 오른쪽에는 버튼 */
            align-items: center;
            margin-bottom: 30px;
            justify-items: center;
        }

        /* 추천받기 버튼 스타일 */
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

        /* 기본 그리드 레이아웃 */
        #recommendations-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
            gap: 20px;
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
        }

        /* 카드가 1개일 때 */
        #recommendations-container.single-item {
            grid-template-columns: 1fr;
            max-width: 400px;
            justify-items: center;
        }

        /* 카드가 2개일 때 */
        #recommendations-container.two-items {
            grid-template-columns: repeat(2, 1fr);
            max-width: 800px;
            justify-content: center;
        }

        /* 카드가 3개일 때 */
        #recommendations-container.three-items {
            grid-template-columns: repeat(3, 1fr);
            max-width: 1200px;
        }

        .recommendation-card {
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            transition: all 0.3s ease;
            height: 350px;
            display: flex;
            flex-direction: column;
            width: 100%;
            max-width: 400px;
        }

        .recommendation-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
        }

        .card-image {
            position: relative;
            width: 100%;
            height: 200px;
            background-color: #e0e0e0;
            display: flex;
            justify-content: center;
            align-items: center;
            overflow: hidden;
        }

        .card-image.no-image {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            font-size: 3rem;
        }

        .card-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .destination-type {
            position: absolute;
            top: 10px;
            right: 10px;
            background-color: rgba(0, 0, 0, 0.7);
            color: white;
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 0.8rem;
            font-weight: bold;
        }

        .card-content {
            padding: 20px;
            flex-grow: 1;
            display: flex;
            flex-direction: column;
        }

        .destination-name {
            font-size: 1.3rem;
            font-weight: bold;
            color: #333;
            margin-bottom: 10px;
        }

        .destination-description {
            display: none;
            margin: 10px 0;
            font-size: 0.9rem;
            color: #666;
            line-height: 1.5;
        }

        .recommendation-card:hover .destination-description {
            display: block;
        }

        .card-footer {
            display: none;
            font-size: 0.8rem;
            color: #888;
            text-align: right;
            margin-top: 10px;
        }

        .recommendation-card:hover .card-footer {
            display: block;
        }

        .created-date {
            font-weight: bold;
            color: #007bff;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            grid-column: 1 / -1;
        }

        .empty-state h3 {
            font-size: 1.8rem;
            font-weight: bold;
            color: #333;
            margin-bottom: 15px;
        }

        .empty-state p {
            color: #777;
            font-size: 1.1rem;
            line-height: 1.6;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
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
            color: white;
            transform: translateY(-2px);
        }

        .pagination .active {
            background-color: #007bff;
            color: white;
            border: 2px solid #007bff;
        }

        .pagination .disabled {
            background-color: #f8f9fa;
            color: #6c757d;
            border: 2px solid #dee2e6;
            cursor: not-allowed;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="top">
        <h1 class="page-title">추천받은 여행지</h1>
        <!-- 추천받기 버튼 -->
        <a href="/recommend" class="recommend-button">추천받기</a>
    </div>

    <!-- 동적 그리드 클래스 적용 -->
    <c:set var="itemCount" value="${fn:length(recommendHistory)}" />
    <c:choose>
        <c:when test="${itemCount == 1}">
            <c:set var="gridClass" value="single-item" />
        </c:when>
        <c:when test="${itemCount == 2}">
            <c:set var="gridClass" value="two-items" />
        </c:when>
        <c:when test="${itemCount == 3}">
            <c:set var="gridClass" value="three-items" />
        </c:when>
        <c:otherwise>
            <c:set var="gridClass" value="" />
        </c:otherwise>
    </c:choose>

    <div id="recommendations-container" class="${gridClass}">
        <!-- 추천 이력이 없는 경우 -->
        <c:if test="${empty recommendHistory and empty errorMessage}">
            <div class="empty-state">
                <h3>아직 추천받은 여행지가 없어요</h3>
                <p>새로운 여행지 추천을 받아보세요!<br>맞춤형 추천으로 완벽한 여행을 계획해보세요.</p>
            </div>
        </c:if>

        <!-- 추천 이력 표시 -->
        <c:forEach var="recommend" items="${recommendHistory}">
            <div class="recommendation-card">
                <div class="card-image ${empty recommend.countryImg ? 'no-image' : ''}">
                    <c:choose>
                        <c:when test="${not empty recommend.countryImg}">
                            <img src="${recommend.countryImg}" alt="${recommend.countryNameKo}" />
                        </c:when>
                        <c:otherwise>
                            <span>🌍</span>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${not empty recommend.continent}">
                        <span class="destination-type">${recommend.continent}</span>
                    </c:if>
                </div>
                <div class="card-content">
                    <h3 class="destination-name">${recommend.countryNameKo}</h3>
                    <p class="destination-description">
                        여행 목적: ${recommend.travelPurpose}<br>
                        동반자: ${recommend.companion}<br>
                        <c:if test="${not empty recommend.recommendRating}">
                            평점: ${recommend.recommendRating}/5
                        </c:if>
                    </p>
                    <div class="card-footer">
                        <c:choose>
                            <c:when test="${not empty recommend.createdAt}">
                                <fmt:formatDate value="${recommend.createdAt}" pattern="yyyy년 MM월 dd일" var="formattedDate"/>
                                <span class="created-date">${formattedDate}</span>
                            </c:when>
                            <c:otherwise>
                                <span class="created-date">날짜 정보 없음</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- 페이지네이션 -->
    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <!-- 이전 페이지 -->
            <c:choose>
                <c:when test="${hasPrevious}">
                    <a href="?page=1" title="첫 페이지">&laquo;</a>
                    <a href="?page=${currentPage-1}" title="이전 페이지">&lt;</a>
                </c:when>
                <c:otherwise>
                    <span class="disabled">&laquo;</span>
                    <span class="disabled">&lt;</span>
                </c:otherwise>
            </c:choose>

            <!-- 페이지 번호 -->
            <c:forEach var="i" begin="${currentPage - 2 < 1 ? 1 : currentPage - 2}"
                       end="${currentPage + 2 > totalPages ? totalPages : currentPage + 2}" step="1">
                <c:choose>
                    <c:when test="${currentPage == i}">
                        <span class="active">${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="?page=${i}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <!-- 다음 페이지 -->
            <c:choose>
                <c:when test="${hasNext}">
                    <a href="?page=${currentPage+1}" title="다음 페이지">&gt;</a>
                    <a href="?page=${totalPages}" title="마지막 페이지">&raquo;</a>
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
</html>