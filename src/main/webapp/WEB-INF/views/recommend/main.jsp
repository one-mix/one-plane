<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>여행 추천</title>
    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            font-family: Pretendard, sans-serif;
            background: linear-gradient(to right, #eaf4ff 0%, #fffbe6 100%);
            min-height: 100vh;
        }

        .hero {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 3rem 8rem;
        }

        .intro h1 {
            font-size: 60px;
            font-weight: 600;
            line-height: 120%;
        }

        .highlight {
            color: #30609d;
        }

        .intro p {
            color: #555;
            font-size: 22px;
            font-weight: 400;
            line-height: 140%;
        }

        .consent {
            display: block;
            font-size: 16px;
            color: #888;
            margin-top: 1rem;
        }

        .notice p {
            font-size: 12px;
            color: #c4c4c4;
            margin-top: 0.5rem;
        }

        button {
            padding: 0.8rem 1.4rem;
            border: none;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
            font-weight: 600;
            cursor: pointer;
            background: #fefefe;
            color: #404040;
            margin-right: 1rem;
        }

        button:disabled {
            background: #e1e1e1;
            color: #888;
            cursor: not-allowed;
            box-shadow: none;
        }

        .hero-img img {
            width: 250px;
        }

        .features {
            display: flex;
            justify-content: space-around;
            padding: 2rem 8rem;
            margin-top: 1rem;
        }

        .card {
            flex: 1;
            margin: 0 0.5rem;
            padding: 1rem;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            text-align: center;
            transition: all 0.3s ease;
        }

        .card:hover {
            transform: scale(1.1);
            box-shadow: 0 6px 16px rgba(48, 96, 157, 0.25);
        }

        .card-icon img {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            margin-bottom: 1rem;
        }

        .card h3 {
            font-size: 1.1rem;
            margin-bottom: 0.5rem;
        }

        .card p {
            font-size: 0.9rem;
            color: #666;
        }
    </style>
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

        <div class="mt-3">
            <button class="btn btn-outline-secondary">이력보기</button>
            <button class="btn btn-dark" id="startBtn" disabled>시작하기</button>
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
        <p>회원님의 취향과 여행 목적을<br>반영하여 추천받을 수 있어요!</p>
    </div>
    <div class="card">
        <div class="card-icon">
            <img src="<c:url value='/images/star.png'/>" alt="별 이미지">
        </div>
        <h3>추천 목록에 담기</h3>
        <p>마음에 드는 추천 국가를 저장하고<br>나만의 여행 리스트를 만들어 보세요!</p>
    </div>
    <div class="card">
        <div class="card-icon">
            <img src="<c:url value='/images/feedback.png'/>" alt="피드백 이미지">
        </div>
        <h3>좋아요/싫어요 피드백</h3>
        <p>추천에 피드백을 남기면,<br>회원님 취향에 더 맞는 추천을 해줘요!</p>
    </div>
    <div class="card">
        <div class="card-icon">
            <img src="<c:url value='/images/conversation.png'/>" alt="소통 이미지">
        </div>
        <h3>함께 나누는 여행</h3>
        <p>다른 여행자들의 후기를 보고<br>나만의 여행 팁도 공유해보세요!</p>
    </div>
</section>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const consentCheck = document.getElementById("consentCheck");
        const startBtn = document.getElementById("startBtn");

        if (consentCheck && startBtn) {
            consentCheck.addEventListener("change", function() {
                startBtn.disabled = !this.checked;
            });

            startBtn.addEventListener("click", function() {
                if (!startBtn.disabled) {
                    window.location.href = "/recommend/input";
                }
            });
        }
    });
</script>
</body>
</html>
