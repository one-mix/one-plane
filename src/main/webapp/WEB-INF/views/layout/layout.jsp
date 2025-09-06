<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>레이아웃</title>

        <!-- Pretendard Font -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css">

        <%-- CSS 연결 --%>
        <link rel="stylesheet" href="/css/variables.css" />
        <link rel="stylesheet" href="/css/style.css" />
        <c:if test="${activeMenu eq 'recommend'}">
            <link rel="stylesheet" href="/css/recommend/style_recommend.css" />
        </c:if>
    </head>
    <body class="${activeMenu}">
        <%-- 공통 헤더 --%>
         <jsp:include page="header.jsp" />
         <div class="container">
            <%-- 공통 사이드바 --%>
            <jsp:include page="sidebar.jsp" />
            <%-- 컨텐츠 영역 --%>
            <div class="content">
               <c:import url="/WEB-INF/views/${contentPage}" />
            </div>
         </div>

         <%-- 공통 푸터 --%>
         <jsp:include page="footer.jsp" />
    </body>
</html>