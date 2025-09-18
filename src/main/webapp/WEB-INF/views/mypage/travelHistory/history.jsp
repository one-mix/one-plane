<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    /* 부모 컨테이너 */
    .container {
        width: 1200px;
        margin: 0 auto;
        padding: 0;
    }

    /* 2. 자식 .page-wrapper */
    .page-wrapper {
        width: 100%;
        max-width: 100%;
        box-sizing: border-box;
        align-items: flex-start;  /* 높이를 강제로 맞추지 않고 위쪽 기준 정렬 */
        display: flex;
        gap: 40px;  /* <-- 여기서 간격 제어 */
    }
</style>


<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>방문한 여행지</title>
    <link rel="stylesheet" href="/css/mypage/dasdas.css" />
    <link rel="stylesheet" href="/css/mypage/history.css" />
    <%--    <link rel="stylesheet" href="/css/history.css" />--%>
</head>
<body>

<!-- 콘텐츠 래퍼 -->
<div class="page-wrapper">
    <div class="sidebar">
        <div class="profile-avatar" id="profileAvatar" title="클릭하여 이미지 변경">
            <!-- 항상 하나의 <img>만 렌더링 -->
            <img id="avatarImg"
                 src="${not empty userProfile.profileImagePath ? userProfile.profileImagePath : '/images/default-avatar.png'}"
                 alt="프로필 아바타"
                 style="width:80px; height:80px; border-radius:50%; object-fit:cover;"/>

            <!-- 숨겨진 파일 입력 -->
            <input type="file"
                   id="avatarInput"
                   name="profileImage"
                   accept="image/*"
                   style="display:none;"
                   onchange="uploadAvatar()"/>

            <%--            <div class="upload-overlay">📷</div>--%>
        </div>

        <div class="profile-info">
            <h4>${userProfile.nickname != null ? userProfile.nickname : '사용자'}</h4>

            <p class="sub-info">
                <c:if test="${not empty userProfile.gender}">
                    <c:choose>
                        <c:when test="${userProfile.gender eq 'male'}">남</c:when>
                        <c:when test="${userProfile.gender eq 'female'}">여</c:when>
                        <c:otherwise>${userProfile.gender}</c:otherwise>
                    </c:choose>
                </c:if>
                <c:if test="${not empty userProfile.age}">
                    · ${userProfile.age}
                </c:if>
            </p>


            <div class="grade-badge">
                <c:choose>
                    <c:when test="${userProfile.grade == 'ECONOMY'}">
                        <span class="grade-text">이코노미</span>
                        <img src="/images/grade/economy.png" alt="이코노미" class="grade-icon" />
                    </c:when>
                    <c:when test="${userProfile.grade == 'STANDARD'}">
                        <span class="grade-text">스탠다드</span>
                        <img src="/images/grade/standard.png" alt="스탠다드" class="grade-icon" />
                    </c:when>
                    <c:when test="${userProfile.grade == 'BUSINESS'}">
                        <span class="grade-text">비즈니스</span>
                        <img src="/images/grade/business.png" alt="비즈니스" class="grade-icon" />
                    </c:when>
                    <c:when test="${userProfile.grade == 'FIRST'}">
                        <span class="grade-text">퍼스트</span>
                        <img src="/images/grade/first.png" alt="퍼스트" class="grade-icon" />
                    </c:when>
                    <c:when test="${userProfile.grade == 'ELITE'}">
                        <span class="grade-text">엘리트</span>
                        <img src="/images/grade/elite.png" alt="엘리트" class="grade-icon" />
                    </c:when>
                </c:choose>
            </div>
        </div>

        <!-- 메뉴 섹션 -->
        <div class="menu-section">
            <h4>여행지</h4>
            <ul class="menu-list">
                <li><a href="/mypage/travelHistory" class="${activeMenu == 'travelHistory' ? 'active' : ''}">방문한</a>
                </li>
                <li><a href="/mypage/dashboard/recommend" class="${activeMenu == 'recommend' ? 'active' : ''}">추천받은</a></li>
            </ul>
        </div>

        <div class="menu-section">
            <h4>게시글</h4>
            <ul class="menu-list">
                <li><a href="/myPost/myPost" class="${activeMenu == 'write' ? 'active' : ''}">내가 작성한</a></li>
                <li><a href="/myPost/followerPost" class="${activeMenu == 'follower' ? 'active' : ''}">팔로워가 작성한</a></li>
            </ul>
        </div>

        <div class="menu-section">
            <h4>팔로우</h4>
            <ul class="menu-list">
                <li><a href="/friends" class="${activeMenu == 'following' ? 'active' : ''}">팔로우</a></li>
            </ul>
        </div>

        <div class="menu-section">
            <h4>회원정보</h4>
            <ul class="menu-list">
                <li><a href="/mypage/profile/edit" class="${activeMenu == 'profile' ? 'active' : ''}">프로필 편집</a></li>
                <c:choose>
                    <c:when test="${userRole eq 'ROLE_ADMIN'}">
                        <li class="danger"><a href="/admin/dashboard">관리자 페이지</a></li>
                    </c:when>

                    <c:otherwise>
                        <li class="danger"><a href="/mypage/profile/out">회원탈퇴</a></li>
                        <!-- 기존 메뉴 항목 수정: “추가하기” 버튼이 CertificationController의 add 매핑을 호출하도록 변경 -->
                    </c:otherwise>
                </c:choose>

            </ul>
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