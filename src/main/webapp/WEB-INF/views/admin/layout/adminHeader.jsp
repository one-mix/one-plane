<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<header class="admin-header">
    <div class="admin-header-content">
        <!-- 로고 -->
        <div class="admin-logo">
            <a href="/admin/userList" class="admin-logo-link" aria-label="OnePlane 홈">
                <img src="/images/logo.png" alt="" class="logo-img" aria-hidden="true" />
                <span>OnePlane</span>
            </a>
        </div>

            <!-- 관리자 정보 -->
            <div class="admin-user">
                <sec:authorize access="isAuthenticated()">
                    <div class="admin-user-info">
                        <span class="admin-username">
                        </span>
                        <span class="admin-role">관리자</span>
                    </div>

                    <div class="admin-user-menu">
                        <div class="dropdown">
                            <button class="admin-user-btn" type="button" data-bs-toggle="dropdown">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.user.profileImg}">
                                        <img src="${sessionScope.user.profileImg}" alt="Profile" class="admin-profile-img" />
                                    </c:when>
                                    <c:otherwise>
                                        <img src="/images/profile.png" alt="Profile" class="admin-profile-img" />
                                    </c:otherwise>
                                </c:choose>
                                <i class="bi bi-chevron-down"></i>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><a class="dropdown-item" href="/mypage">내 정보</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <form action="/logout" method="post" style="display: inline;">
                                        <sec:csrfInput />
                                        <button type="submit" class="dropdown-item">로그아웃</button>
                                    </form>
                                </li>
                            </ul>
                        </div>
                    </div>
                </sec:authorize>
            </div>
        </div>
    </div>
</header>