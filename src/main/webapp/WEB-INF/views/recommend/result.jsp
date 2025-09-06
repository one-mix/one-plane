<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<main class="result">
    <link rel="stylesheet" href="<c:url value='/css/recommend/result.css'/>">
    <h2>
        <span class="username">홍길동</span>님을 위한 3개국,<br>
        어떤 곳이 마음에 드시나요?
    </h2>

    <div class="cards">
        <div class="card" data-country="아시아">
            <div class="card-image"></div>
            <div class="card-content">
                <p class="country">나라</p>
                <h3 class="place">대표 관광지</h3>
                <p class="extra">유사도: 90%</p>
                <p class="extra">대륙: 아시아</p>
            </div>
        </div>
        <div class="card" data-country="유럽">
            <div class="card-image"></div>
            <div class="card-content">
                <p class="country">나라</p>
                <h3 class="place">대표 관광지</h3>
                <p class="extra">유사도: 85%</p>
                <p class="extra">대륙: 유럽</p>
            </div>
        </div>
        <div class="card" data-country="아메리카">
            <div class="card-image"></div>
            <div class="card-content">
                <p class="country">나라</p>
                <h3 class="place">대표 관광지</h3>
                <p class="extra">유사도: 82%</p>
                <p class="extra">대륙: 아메리카</p>
            </div>
        </div>
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
