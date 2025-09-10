<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"      %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"       %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>방문한 여행지</title>
    <link rel="stylesheet" href="/css/mypage.css" />
    <link rel="stylesheet" href="/css/mypageContent.css" />

</head>
<body>


<div class="page-wrapper">
    <div class="timeline-container">
        <div class="timeline-bar">
            <!-- 기준선 -->
            <div class="timeline-line"></div>
            <div class="timeline-label-max">${maxDistance}km</div>
            <!-- 방문 국가 표시 -->
            <!-- 방문 국가·비행기 아이콘과 날짜·거리 표시 -->
            <c:forEach var="item" items="${timeline}">
                <!-- 누적거리 비율 계산 -->
                <c:set var="pos" value="${item.cumulativeDistance / maxDistance * 100}" />

                <div class="timeline-item" style="left:${pos}%">
                    <!-- 국기 대신 비행기 아이콘 사용 시 아래 img 태그를 교체하세요. -->
                    <div class="icon-wrapper">
                        <img src="${pageContext.request.contextPath}/images/airplane.png"
                             alt="plane"
                             class="icon-plane"/>
                    </div>
                    <div class="info">
                        <div class="date"><c:out value="${item.certificationDate}" /></div>
                        <div class="dist"><c:out value="${item.cumulativeDistance}" />km</div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- 메인 컨텐츠 -->
    <div class="content-area">
        <div class="content-title">방문한 여행지</div>

        <!-- 통계 영역 -->
        <div class="stats-container">
            <div class="stat-item">
                <div class="stat-number"><c:out value="${countryCount}" /></div>
                <div class="stat-label">국가 수</div>
            </div>
            <div class="stat-item">
                <div class="stat-number"><fmt:formatNumber value="${totalDistance}" pattern="#,###" /></div>
                <div class="stat-label">이동 거리 (km)</div>
            </div>
            <div class="stat-item">
                <div class="stat-number"><c:out value="${photoCount}" /></div>
                <div class="stat-label">업로드 사진</div>
            </div>
        </div>

        <div class="recommend-title">추천받은 여행지</div>

        <!-- 추천받은 여행지 섹션 -->
        <div class="recommend-section">
            <div class="recommend-empty">
                <p>아직 여러분의 다음 여행지는 정해지지 않았습니다.</p>
                <a href="${pageContext.request.contextPath}/recommend" class="btn-recommend">
                    여행지 추천 받으러 가기 →
                </a>
            </div>
        </div>

        <c:if test="${not empty recommendedPlaces}">
            <div class="recommend-container">
                <c:forEach items="${recommendedPlaces}" var="place">
                    <div class="recommend-card">
                        <div class="card-thumb"
                             style="background-image:url(${place.thumbUrl});">
                        </div>
                        <div class="card-info">
                            <div class="card-country">
                                <c:out value="${place.country}" />
                            </div>
                            <div class="card-name">
                                <c:out value="${place.name}" />
                            </div>
                            <div class="card-desc">
                                <c:out value="${place.description}" />
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
    </div>

</div>
</body>


</html>
