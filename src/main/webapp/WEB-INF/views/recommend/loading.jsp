<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<main class="loading-screen">
    <link rel="stylesheet" href="<c:url value='/css/recommend/loading.css'/>">

    <div class="loading-content">
        <img src="<c:url value='/images/airplane.png'/>" alt="AI 이미지" class="plane-icon">
        <p class="loading-text">AI 추천 받는 중</p>
        <div class="dots">
            <span></span><span></span><span></span>
        </div>
    </div>
</main>

<script>
    setTimeout(() => {
        window.location.href = "<c:url value='/recommend/result'/>";
    }, 3000);
</script>
