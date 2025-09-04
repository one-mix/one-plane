<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>레이아웃</title>
    </head>
    <body>
        <%-- 공통 헤더 --%>
         <jsp:include page="header.jsp" />

         <div class="container">

            <%-- 공통 사이드바 --%>
            <jsp:include page="sidebar.jsp" />

            <%-- 컨텐츠 영역 --%>
            <div class="content">
               <jsp:include page="${contentPage}" />
            </div>
         </div>

         <%-- 공통 푸터 --%>
         <jsp:include page="footer.jsp" />
    </body>
</html>