<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- 상단 공통 헤더 --%>
<header class="header">

    <%-- 로고 --%>
    <div class="logo">
        <img src="/images/logo.png" alt="OnePlane Logo" class="logo-img" />
        <span>OnePlane</span>
    </div>

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
        <a href="/post"
            class="${activeMenu eq 'post' ? 'active' : ''}">
            게시판
        </a>
    </nav>

    <%-- 유저 --%>
    <div class="user">
        <a href="/oauth2/authorization/kakao">로그인</a>
        <a href="/mypage">
            <img src="/images/profile.png" alt="Profile" class="profile-img" />
        </a>
    </div>
</header>
