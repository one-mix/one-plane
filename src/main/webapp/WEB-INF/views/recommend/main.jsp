<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>여행 추천</title>
    <link rel="stylesheet" href="<c:url value='/css/style_main.css'/>">
</head>
<body>
<main class="hero">
    <section class="intro">
        <h1><span class="highlight">AI</span>와 함께<br>여행지 알아보기</h1>
        <p>회원님의 여행 이력과 정보를 바탕으로<br>
            취향과 목적에 맞는 여행 국가를 추천해드려요!</p>

        <label class="consent">
            <input type="checkbox" id="consentCheck"> 개인정보 수집 동의
        </label>

        <div class="notice">
            <p>[주의사항]<br>
                회원님의 방문 목적 및 동행자 정보는
                AI 추천 기능을 제공하는 데에만 사용됩니다.
            </p>
        </div>

        <div class="buttons">
            <button>이력보기</button>
            <button class="secondary" id="startBtn" disabled>시작하기</button>
        </div>
    </section>

    <div class="hero-img">
        <img src="<c:url value='/images/airplane.png'/>" alt="비행기 이미지">
    </div>
</main>

<section class="features">
    <div class="card">
        <div class="card-icon">
            <img src="<c:url value='/images/map.png'/>" alt="지도 이미지">
        </div>
        <h3>AI가 고른 오늘의 여행</h3>
        <p>회원님의 취향과 여행 목적을<br>
            반영하여 추천받을 수 있어요!</p>
    </div>
    <div class="card">
        <div class="card-icon">
            <img src="<c:url value='/images/star.png'/>" alt="별 이미지">
        </div>
        <h3>추천 목록에 담기</h3>
        <p>마음에 드는 추천 국가를 저장하고<br>
            나만의 여행 리스트를 만들어 보세요!</p>
    </div>
    <div class="card">
        <div class="card-icon">
            <img src="<c:url value='/images/feedback.png'/>" alt="피드백 이미지">
        </div>
        <h3>좋아요/싫어요 피드백</h3>
        <p>추천에 피드백을 남기면,<br>
            회원님 취향에 더 맞는 추천을 해줘요!</p>
    </div>
    <div class="card">
        <div class="card-icon">
            <img src="<c:url value='/images/conversation.png'/>" alt="소통 이미지">
        </div>
        <h3>함께 나누는 여행</h3>
        <p>다른 여행자들의 후기를 보고<br>
            나만의 여행 팁도 공유해보세요!</p>
    </div>
</section>
<script src="<c:url value='/js/form-control.js'/>"></script>
</body>
</html>
