<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>여행 목적 선택</title>
    <link rel="stylesheet" href="<c:url value='/css/style_input.css'/>">
</head>
<body>
<main class="input">
    <div class="top">
        <img src="<c:url value='/images/airplane.png'/>" alt="AI 이미지" class="plane-icon">
        <h2>AI와 대화중</h2>
    </div>

    <div class="bottom">
        <p id="question" class="question">
        <div id="options" class="options">
        </div>
    </div>
</main>

<script src="<c:url value='/js/input.js'/>"></script>
</body>
</html>
