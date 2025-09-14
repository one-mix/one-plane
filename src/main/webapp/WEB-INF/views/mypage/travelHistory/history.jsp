<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>방문한 여행지</title>
    <link rel="stylesheet" href="/css/dasdas.css" />
    <link rel="stylesheet" href="/css/history.css" />
    <%--    <link rel="stylesheet" href="/css/history.css" />--%>
</head>
<body>

<!-- 콘텐츠 래퍼 -->
<div class="page-wrapper">
    <div class="sidebar">
        <!-- 프로필 섹션 -->
        <div class="user-profile">
            <div class="profile-avatar" id="profileAvatar" title="클릭하여 이미지 변경">
                <img id="avatarImg"
                     src="${not empty userProfile.profileImagePath ? userProfile.profileImagePath : '/images/default-avatar.png'}"
                     alt="프로필 아바타"/>
                <input type="file"
                       id="avatarInput"
                       name="profileImage"
                       accept="image/*"
                       style="display:none;"
                       onchange="uploadAvatar()" />
            </div>
            <div class="user-name">
                ${userProfile.nickname != null ? userProfile.nickname : '닉네임'}
            </div>
            <div class="user-info">
                <span>${userProfile.gender != null ? userProfile.gender : '성별'}</span> ·
                <span>${userProfile.age != null ? userProfile.age : '나이'}</span>
            </div>
        </div>

        <!-- 여행지 섹션 -->
        <div class="menu-section">
            <div class="menu-title">여행지</div>
            <a href="<c:url value='/mypage/travelHistory'/>"
               class="menu-item ${activeMenu == 'travelHistory' ? 'active' : ''}">방문한</a>
            <a href="<c:url value='/mypage/recommend'/>"
               class="menu-item ${activeMenu == 'recommend' ? 'active' : ''}">추천받은</a>
        </div>

        <!-- 게시글 섹션 -->
        <div class="menu-section">
            <div class="menu-title">게시글</div>
            <a href="<c:url value='/myPost/myPost'/>"
               class="menu-item ${activeMenu == 'myBoard' ? 'active' : ''}">내가 작성</a>
            <a href="<c:url value='/myPost/followerPost'/>"
               class="menu-item ${activeMenu == 'followerPost' ? 'active' : ''}">팔로워가 작성</a>
        </div>

        <!-- 친구 섹션 -->
        <div class="menu-section">
            <div class="menu-title">친구</div>
            <a href="/friends/following" class="menu-item">내가 팔로잉</a>
            <a href="/friends/followers" class="menu-item">나를 팔로우</a>
        </div>

        <!-- 회원정보 섹션 -->
        <div class="menu-section">
            <div class="menu-title">회원정보</div>
            <a href="<c:url value='/mypage/profile/edit'/>"
               class="menu-item ${activeMenu == 'editProfile' ? 'active' : ''}">프로필 편집</a>
            <a href="<c:url value='/mypage/profile/out'/>" class="menu-item danger">회원탈퇴</a>
        </div>
    </div>


    <div class="content">
        <!-- 헤더 -->
        <div class="content-header">
            <h2 class="content-title">방문한 여행지</h2>
            <button class="btn-add" onclick="location.href='/mypage/travelHistory/add'">추가하기</button>
        </div>

        <!-- 메인 콘텐츠 -->
        <c:choose>
            <c:when test="${empty travelList}">
                <div class="empty-message">
                    <h3>아직 방문한 여행지가 없어요.</h3>
                    <p>추가하기 버튼으로 기록해보세요!</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="travel-grid">
                    <c:forEach items="${travelList}" var="travel">
                        <div class="travel-card">
                            <div class="card-image-container">
                                <c:choose>
                                    <c:when test="${not empty travel.travelImg}">
                                        <img src="<c:url value='/mypage/travelHistory/image/${travel.travelHistoryId}'/>"
                                             alt="${travel.title}" class="card-image"/>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="card-no-image">${travel.title}</div>
                                    </c:otherwise>
                                </c:choose>

                                <!-- 카드 내용 오버레이 -->
                                <div class="content-overlay">
                                    <div class="overlay-header">
                                        <div class="overlay-title">${travel.title}</div>
                                        <div class="overlay-date">
                                            <fmt:formatDate value="${travel.travelAt}" pattern="yyyy.MM.dd"/>
                                        </div>
                                    </div>

                                    <div class="overlay-body">
                                        <div class="overlay-location">${travel.city}</div>
                                        <div class="overlay-rating">
                                        <span class="overlay-stars">
                                            <c:forEach begin="1" end="${travel.rating}">★</c:forEach>
                                            <c:forEach begin="${travel.rating+1}" end="5">☆</c:forEach>
                                        </span>
                                            <span class="overlay-rating-text">${travel.rating}/5</span>
                                        </div>
                                        <div class="overlay-tags">
                                            <span class="overlay-tag">${travel.travelPurpose}</span>
                                            <span class="overlay-tag">${travel.companion}</span>
                                        </div>
                                        <div class="overlay-content">${travel.content}</div>
                                    </div>

                                    <!-- 수정/삭제 버튼 -->
                                    <div class="overlay-actions">
                                        <a href="<c:url value='/mypage/travelHistory/edit/${travel.travelHistoryId}'/>"
                                           class="btn-edit">수정</a>
                                        <form action="<c:url value='/mypage/travelHistory/delete/${travel.travelHistoryId}'/>"
                                              method="post" style="display:inline;"
                                              onsubmit="return confirm('삭제 하시겠습니까?');">
                                            <button type="submit" class="btn-delete">삭제</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>