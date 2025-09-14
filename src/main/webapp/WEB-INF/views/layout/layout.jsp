<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ko">
    <head>
        <title>레이아웃</title>

        <!-- Pretendard Font -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css">

        <%-- CSS 연결 --%>
        <link rel="stylesheet" href="/css/variables.css" />
        <link rel="stylesheet" href="/css/style.css" />
        <c:if test="${activeMenu eq 'recommend'}">
            <link rel="stylesheet" href="/css/recommend/recommend.css" />
        </c:if>
        <c:if test="${showSidebar}">
            <link rel="stylesheet" href="/css/sidebar.css">
        </c:if>
    </head>
    <body class="${activeMenu}">
        <%-- 공통 헤더 --%>
         <jsp:include page="header.jsp" />

         <div class="container">
            <%-- 컨텐츠 영역 --%>
            <div class="content">
               <c:import url="/WEB-INF/views/${contentPage}" />
            </div>
         </div>

         <%-- 공통 푸터 --%>
         <jsp:include page="footer.jsp" />

        <!-- hideSidebar가 true가 아닐 때만 사이드바 출력 -->
        <div class="wrapper">
            <!-- showSidebar가 true일 때만 사이드바 출력 -->
            <c:if test="${showSidebar}">
                <jsp:include page="./sidebar.jsp" />
            </c:if>
        </div>
    </body>
    <script src="/js/main/main.js"></script>
</html>
