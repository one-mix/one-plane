<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>여행 추천 로딩</title>
    <style>
        body {
            font-family: Pretendard, sans-serif;
            margin: 0;
            background: linear-gradient(to right, #eaf4ff 0%, #fffbe6 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .loading-screen {
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
        }

        .loading-content {
            text-align: center;
        }

        .plane-icon {
            width: 70px;
            margin-bottom: 1rem;
        }

        .loading-text {
            font-size: 1.2rem;
            margin-bottom: 1rem;
            color: #333;
            font-weight: 500;
        }

        .dots {
            display: flex;
            justify-content: center;
            gap: 0.5rem;
        }

        .dots span {
            width: 10px;
            height: 10px;
            background-color: #333;
            border-radius: 50%;
            display: inline-block;
            animation: bounce 1.2s infinite ease-in-out;
        }

        .dots span:nth-child(1) {
            animation-delay: 0s;
        }
        .dots span:nth-child(2) {
            animation-delay: 0.2s;
        }
        .dots span:nth-child(3) {
            animation-delay: 0.4s;
        }

        @keyframes bounce {
            0%, 80%, 100% {
                transform: scale(0);
            }
            40% {
                transform: scale(1);
            }
        }
    </style>
</head>
<body>
<main class="loading-screen">
    <div class="loading-content">
        <img src="<c:url value='/images/airplane.png'/>" alt="AI 이미지" class="plane-icon">
        <p class="loading-text">AI 추천 받는 중</p>
        <div class="dots">
            <span></span>
            <span></span>
            <span></span>
        </div>
    </div>
</main>

<script>
    // 3초 후 결과 페이지로 이동
    setTimeout(() => {
        window.location.href = "<c:url value='/recommend/result'/>";
    }, 3000);
</script>
</body>
</html>
