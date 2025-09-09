<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<main class="input">
    <link rel="stylesheet" href="<c:url value='/css/recommend/input.css'/>">
    <div class="top">
        <img src="<c:url value='/images/airplane.png'/>" alt="AI 이미지" class="plane-icon">
        <h2>AI와 대화중</h2>
    </div>

    <div class="bottom">
        <p id="question" class="question"></p>
        <div id="options" class="options"></div>
    </div>
</main>

<script src="<c:url value='/js/recommend/input.js'/>"></script>
