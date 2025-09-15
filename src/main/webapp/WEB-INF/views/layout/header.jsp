<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%-- 상단 공통 헤더 --%>
<header class="header">

    <%-- 로고 --%>
    <div class="logo">
        <a href="/">
            <img src="/images/logo.png" alt="OnePlane Logo" class="logo-img" />
            <span>OnePlane</span>
        </a>
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
        <a href="/post/list"
            class="${activeMenu eq 'post' ? 'active' : ''}">
            커뮤니티
        </a>
    </nav>

    <%-- 유저 --%>
    <sec:authorize access="!isAuthenticated()">
    <div class="user">
        <a href="/oauth2/authorization/kakao">로그인</a>
    </div>
    </sec:authorize>
        <%-- 유저 - 로그인한 상태 --%>
        <sec:authorize access="isAuthenticated()">
            <div class="user">
                    <%-- 로그아웃 폼 --%>
                <form action="/logout" method="post" style="display: inline;">
                    <sec:csrfInput />
                    <button type="submit" class="logout-btn">로그아웃</button>
                </form>

                    <%-- 프로필 이미지 --%>
                        <a href="/mypage/dashboard">
                            <c:choose>
                                <c:when test="${not empty sessionScope.userProfile.profileImagePath}">
                                    <img src="${sessionScope.userProfile.profileImagePath}" alt="Profile" class="profile-img" />
                                </c:when>
                                <c:when test="${not empty sessionScope.user.profileImg}">
                                    <img src="${sessionScope.user.profileImg}" alt="Profile" class="profile-img" />
                                </c:when>
                                <c:otherwise>
                                    <img src="/images/profile.png" alt="Profile" class="profile-img" />
                                </c:otherwise>
                            </c:choose>
                        </a>
            </div>
        </sec:authorize>
</header>
