<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>임시 메인 화면</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <style>
        body { background: #f8f9fa; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        .hero-section { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 100px 0; text-align: center; }
        .hero-title { font-size: 3.5rem; font-weight: 700; margin-bottom: 20px; }
        .hero-subtitle { font-size: 1.3rem; margin-bottom: 40px; opacity: 0.9; }
        .navbar-brand { font-weight: 700; font-size: 1.5rem; }
        .feature-card { border: none; border-radius: 15px; box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08); transition: transform 0.3s ease; height: 100%; }
        .feature-card:hover { transform: translateY(-5px); }
        .feature-icon { font-size: 3rem; color: #667eea; margin-bottom: 20px; }
        .btn-hero { background: white; color: #667eea; font-weight: 600; padding: 12px 30px; border-radius: 50px; border: none; transition: all 0.3s ease; }
        .btn-hero:hover { color: #5a67d8; transform: translateY(-2px); box-shadow: 0 8px 25px rgba(255, 255, 255, 0.3); }
        .user-welcome { background: #e3f2fd; border-radius: 15px; padding: 20px; margin-bottom: 30px; }
        .user-avatar { width: 50px; height: 50px; border-radius: 50%; border: 3px solid #667eea; }
    </style>
</head>
<body>
<!-- Navigation -->
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
    <div class="container">
        <a class="navbar-brand text-primary" href="/">
            <i class="bi bi-airplane-engines"></i> OnePlane
        </a>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">

                <li><a class="dropdown-item" href="/post/list">게시판</a></li>

                <!-- 로그인 버튼 (비로그인 상태) -->
                <sec:authorize access="!isAuthenticated()">
                    <li class="nav-item">
                        <a class="nav-link" href="/oauth2/authorization/kakao"><i class="bi bi-box-arrow-in-right"></i> 로그인</a>
                    </li>
                </sec:authorize>

                <!-- 유저 메뉴 (로그인 상태) -->
                <sec:authorize access="isAuthenticated()">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown">
                            <c:if test="${not empty sessionScope.user.profileImg}">
                                <img src="${sessionScope.user.profileImg}" alt="프로필" class="user-avatar me-2">
                            </c:if>
                                ${sessionScope.userName != null ? sessionScope.userName : sessionScope.userNickname}님
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="/post/list">게시판</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="/logout">로그아웃</a></li>
                        </ul>
                    </li>
                </sec:authorize>

            </ul>
        </div>
    </div>
</nav>

<!-- Hero Section -->
<section class="hero-section">
    <div class="container">

        <!-- 로그인 사용자 환영 메시지 -->
        <sec:authorize access="isAuthenticated()">
            <div class="user-welcome row align-items-center">
                <div class="col-md-2 text-center">
                    <c:if test="${not empty sessionScope.user.profileImg}">
                        <img src="${sessionScope.user.profileImg}" alt="프로필" class="user-avatar">
                    </c:if>
                </div>
                <div class="col-md-8">
                    <h5 class="mb-1 text-primary">${sessionScope.userName != null ? sessionScope.userName : sessionScope.userNickname}님, 환영합니다!</h5>
                    <p class="mb-0 text-muted">
                        <c:choose>
                            <c:when test="${sessionScope.needsTravelCaution}">
                                <i class="bi bi-shield-exclamation text-warning"></i> 여행 시 건강 상태를 고려한 맞춤 정보를 제공해드립니다.
                            </c:when>
                            <c:otherwise>
                                <i class="bi bi-shield-check text-success"></i> 안전한 여행을 위한 모든 준비가 완료되었습니다.
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
                <div class="col-md-2 text-center">
                    <span class="badge bg-primary fs-6">${sessionScope.userRole == 'ROLE_ADMIN' ? '관리자' : '일반회원'}</span>
                </div>
            </div>
        </sec:authorize>

        <h1 class="hero-title">안전한 여행의 시작</h1>
        <p class="hero-subtitle">실시간 여행경보와 맞춤형 안전 가이드로 걱정 없는 여행을 떠나세요</p>

        <!-- CTA 버튼 -->
        <sec:authorize access="!isAuthenticated()">
            <a href="/oauth2/authorization/kakao" class="btn btn-hero btn-lg"><i class="bi bi-airplane-engines me-2"></i> 지금 시작하기</a>
        </sec:authorize>

        <sec:authorize access="isAuthenticated()">
            <a href="/travel/search" class="btn btn-hero btn-lg"><i class="bi bi-search me-2"></i> 여행지 검색</a>
        </sec:authorize>

    </div>
</section>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- 세션 로그 (개발용) -->
<sec:authorize access="isAuthenticated()">
    <script>
        console.group('User Session Info');
        console.log('User ID:', '${sessionScope.userId}');
        console.log('User Name:', '${sessionScope.userName}');
        console.log('User Nickname:', '${sessionScope.userNickname}');
        console.log('User Role:', '${sessionScope.userRole}');
        console.log('Needs Travel Caution:', ${sessionScope.needsTravelCaution});
        console.log('Has Health Info:', ${sessionScope.hasHealthInfo});
        console.groupEnd();
    </script>
</sec:authorize>

</body>
</html>
