<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- 상단 공통 헤더 --%>
<header class="header">

    <%-- 로고 --%>
    <div class="logo">OnePlane</div>

    <%-- 네비게이션 메뉴 --%>
    <nav class="nav">
        <a href="/"
            class="${activeMenu eq 'home' ? 'active' : ''}">
            지도
        </a>
        <a href="/recommend"
            class="${activeMenu eq 'recommend' ? 'active' : ''}">
            추천
        </a>
        <a href="/board"
            class="${activeMenu eq 'board' ? 'active' : ''}">
            게시판
        </a>
    </nav>

    <%-- 유저 --%>
    <div class="user">
        <a href="/login">로그인</a>
        <a href="/mypage">마이페이지</a>
    </div>
</header>
