<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>여행 추천 결과</title>
    <link rel="stylesheet" href="<c:url value='/css/style_result.css'/>">
</head>
<body>
<main class="result">
    <h2>
        <span class="username">홍길동</span>님을 위한 3개국,<br>
        어떤 곳이 마음에 드시나요?
    </h2>

    <div class="cards">
        <div class="card">
            <div class="card-image"></div>
            <div class="card-content">
                <p class="country">나라</p>
                <h3 class="place">대표 관광지</h3>
                <p class="extra">유사도: 90%</p>
                <p class="extra">대륙: 아시아</p>
            </div>
        </div>
        <div class="card">
            <div class="card-image"></div>
            <div class="card-content">
                <p class="country">나라</p>
                <h3 class="place">대표 관광지</h3>
                <p class="extra">유사도: 85%</p>
                <p class="extra">대륙: 유럽</p>
            </div>
        </div>
        <div class="card">
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
        <button class="save">저장하기</button>
    </div>
</main>
</body>
</html>
