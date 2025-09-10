<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<main class="result">
    <link rel="stylesheet" href="<c:url value='/css/recommend/result.css'/>">
    <h2>
        <span class="username">${user.nickname}</span>님을 위한 3개국,<br>
        어떤 곳이 마음에 드시나요?
    </h2>

    <div class="cards">
        <c:forEach var="rec" items="${recommendations}">
            <div class="card" data-country="${rec.countryIso3}">
                <div class="card-image">
                    <img src="${rec.countryImg}" alt="${rec.countryNameKo} 국기">
                </div>
                <div class="card-content">
                    <p class="country">${rec.countryNameKo}</p>
                    <h3 class="place">${rec.city}</h3>
                    <p class="extra">대륙: ${rec.continent}</p>

                    <!-- 여행 경보 표시 -->
                    <c:if test="${not empty rec.alertLevel}">
                        <div class="alert-level">
                            <c:choose>
                                <c:when test="${rec.alertLevel == '여행금지'}">
                                    <span class="alert-danger">여행금지</span>
                                </c:when>
                                <c:when test="${rec.alertLevel == '철수권고'}">
                                    <span class="alert-warning">철수권고</span>
                                </c:when>
                                <c:when test="${rec.alertLevel == '여행자제'}">
                                    <span class="alert-info">여행자제</span>
                                </c:when>
                                <c:when test="${rec.alertLevel == '여행유의'}">
                                    <span class="alert-caution">여행자제</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="alert-safe">안전</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>

    <div class="actions">
        <button class="cancel">취소하기</button>
        <button class="save" disabled>저장하기</button>
    </div>
</main>

<!-- 모달 -->
<div id="feedbackModal" class="modal">
    <div class="modal-content">
        <h3>AI 추천에 만족하시나요?</h3>

        <div class="stars" id="stars">
            <span class="star" data-value="1">★</span>
            <span class="star" data-value="2">★</span>
            <span class="star" data-value="3">★</span>
            <span class="star" data-value="4">★</span>
            <span class="star" data-value="5">★</span>
        </div>

        <textarea id="comment" placeholder="의견을 남겨주세요"></textarea>

        <div class="modal-actions">
            <button class="close" id="closeModal">닫기</button>
            <button class="submit" id="submitFeedback">제출하기</button>
        </div>
    </div>
</div>

<!-- 토스트 -->
<div id="toast" class="toast">제출 완료!</div>

<script src="<c:url value='/js/recommend/result.js'/>"></script>
