<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>방문한 여행지</title>
    <link rel="stylesheet" href="/css/mypage/mypage.css" />
    <link rel="stylesheet" href="/css/mypage/history.css" />
</head>
<body>

    <!-- 콘텐츠 래퍼 -->
    <div class="content-wrapper">
        <div class="content">
            <!-- 헤더 -->
            <div class="content-header">
                <h2 class="content-title">방문한 여행지</h2>
                <button class="btn-add" onclick="location.href='/mypage/travelHistory/add'">추가하기</button>
            </div>

            <!-- 메인 콘텐츠 -->
            <c:choose>
                <c:when test="${empty travelList}">
                    <div class="empty-state">
                        <h3>아직 방문한 여행지가 없어요.</h3>
                        <p>추가하기 버튼으로 기록해보세요!</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="travel-grid">
                        <c:forEach items="${travelList}" var="travel">
                            <div class="travel-card">
                                <div class="card-image-container">
                                    <c:choose>
                                        <c:when test="${not empty travel.imagePath}">
                                            <img src="${pageContext.request.contextPath}/uploads/${travel.imagePath}"
                                                 alt="${travel.title}" class="card-image"/>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="card-no-image">${travel.title}</div>
                                        </c:otherwise>
                                    </c:choose>


                                    <!-- 호버 오버레이 -->
                                    <div class="content-overlay">
                                        <div class="overlay-header">
                                            <div class="overlay-title">${travel.title}</div>
                                            <div class="overlay-date">
                                                <fmt:formatDate value="${travel.travelAt}" pattern="yyyy.MM.dd"/>
                                            </div>
                                        </div>

                                        <div class="overlay-body">
                                            <div class="overlay-location">${travel.city}</div>

                                            <div class="overlay-rating">
                                                    <span class="overlay-stars">
                                                        <c:forEach begin="1" end="${travel.rating}">★</c:forEach>
                                                        <c:forEach begin="${travel.rating+1}" end="5">☆</c:forEach>
                                                    </span>
                                                <span class="overlay-rating-text">${travel.rating}/5</span>
                                            </div>

                                            <div class="overlay-tags">
                                                <span class="overlay-tag">${travel.travelPurpose}</span>
                                                <span class="overlay-tag">${travel.companion}</span>
                                            </div>

                                            <div class="overlay-content">${travel.content}</div>
                                        </div>

                                        <!-- 수정 링크 - 이 부분이 핵심입니다 -->
                                        <div class="overlay-actions">
                                            <a href="${pageContext.request.contextPath}/mypage/travelHistory/edit/${travel.travelHistoryId}"
                                               class="btn-edit">수정</a>

                                            <form action="${pageContext.request.contextPath}/mypage/travelHistory/delete/${travel.travelHistoryId}"
                                                  method="post" style="display:inline;"
                                                  onsubmit="return confirm('삭제 하시겠습니까?');">
                                                <button type="submit" class="btn-delete">삭제</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>