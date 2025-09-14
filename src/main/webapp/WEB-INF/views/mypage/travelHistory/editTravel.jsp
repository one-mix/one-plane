<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>여행지 수정</title>
    <link rel="stylesheet" href="/css/review.css" />
</head>
<body>

<div class="page-wrapper">
    <%--    <div class="sidebar">--%>
    <%--        <!-- 프로필 섹션 -->--%>
    <%--        <div class="user-profile">--%>
    <%--            <div class="profile-avatar" id="profileAvatar" title="클릭하여 이미지 변경">--%>
    <%--                <img id="avatarImg"--%>
    <%--                     src="${not empty userProfile.profileImagePath ? userProfile.profileImagePath : '/images/default-avatar.png'}"--%>
    <%--                     alt="프로필 아바타"/>--%>
    <%--                <input type="file"--%>
    <%--                       id="avatarInput"--%>
    <%--                       name="profileImage"--%>
    <%--                       accept="image/*"--%>
    <%--                       style="display:none;"--%>
    <%--                       onchange="uploadAvatar()" />--%>
    <%--            </div>--%>
    <%--            <div class="user-name">--%>
    <%--                ${userProfile.nickname != null ? userProfile.nickname : '닉네임'}--%>
    <%--            </div>--%>
    <%--            <div class="user-info">--%>
    <%--                <span>${userProfile.gender != null ? userProfile.gender : '성별'}</span> ·--%>
    <%--                <span>${userProfile.age != null ? userProfile.age : '나이'}</span>--%>
    <%--            </div>--%>
    <%--        </div>--%>

    <%--        <!-- 여행지 섹션 -->--%>
    <%--        <div class="menu-section">--%>
    <%--            <div class="menu-title">여행지</div>--%>
    <%--            <a href="<c:url value='/mypage/travelHistory'/>"--%>
    <%--               class="menu-item ${activeMenu == 'travelHistory' ? 'active' : ''}">방문한</a>--%>
    <%--            <a href="<c:url value='/mypage/recommend'/>"--%>
    <%--               class="menu-item ${activeMenu == 'recommend' ? 'active' : ''}">추천받은</a>--%>
    <%--        </div>--%>

    <%--        <!-- 게시글 섹션 -->--%>
    <%--        <div class="menu-section">--%>
    <%--            <div class="menu-title">게시글</div>--%>
    <%--            <a href="<c:url value='/myPost/myBoard'/>"--%>
    <%--               class="menu-item ${activeMenu == 'myBoard' ? 'active' : ''}">내가 작성</a>--%>
    <%--            <a href="<c:url value='/myPost/followerBoard'/>"--%>
    <%--               class="menu-item ${activeMenu == 'followerBoard' ? 'active' : ''}">팔로워가 작성</a>--%>
    <%--        </div>--%>

    <%--        <!-- 친구 섹션 -->--%>
    <%--        <div class="menu-section">--%>
    <%--            <div class="menu-title">친구</div>--%>
    <%--            <a href="/friends/following" class="menu-item">내가 팔로잉</a>--%>
    <%--            <a href="/friends/followers" class="menu-item">나를 팔로우</a>--%>
    <%--        </div>--%>

    <%--        <!-- 회원정보 섹션 -->--%>
    <%--        <div class="menu-section">--%>
    <%--            <div class="menu-title">회원정보</div>--%>
    <%--            <a href="<c:url value='/mypage/profile/edit'/>"--%>
    <%--               class="menu-item ${activeMenu == 'editProfile' ? 'active' : ''}">프로필 편집</a>--%>
    <%--            <a href="<c:url value='/mypage/profile/out'/>" class="menu-item danger">회원탈퇴</a>--%>
    <%--        </div>--%>
    <%--    </div>--%>

    <main class="content">
        <%--        <h2 class="page-title">방문한 여행지 수정</h2>--%>

        <form action="${pageContext.request.contextPath}/mypage/travelHistory/edit/${travelId}" method="post" enctype="multipart/form-data" class="form-card">
            <input type="hidden" name="countryId" value="1">

            <!-- 도시 -->
            <div class="form-row">
                <label>국가</label>
                <input type="text" name="city" value="${dto.city}" placeholder="방문한 국가를 입력해주세요." required>
            </div>

            <!-- 날짜 -->
            <div class="form-row">
                <label>날짜</label>
                <input type="date" name="travelDate" value="${dto.travelDate}" required>
            </div>

            <!-- 제목 -->
            <div class="form-row">
                <label>제목</label>
                <input type="text" name="title" value="${dto.title}" placeholder="제목을 입력해주세요." required>
            </div>

            <!-- 평가 -->
            <div class="form-row">
                <label>평가</label>
                <div class="rating">
                    <c:forEach var="i" begin="1" end="5">
                        <input type="radio" id="star${6-i}" name="rating" value="${6-i}"
                            ${dto.rating == (6-i) ? 'checked' : ''}>
                        <label for="star${6-i}">★</label>
                    </c:forEach>
                </div>
            </div>

            <div class="form-group">
                <label>테마</label>
                <div class="option-grid">
                    <div class="option-item">
                        <input type="radio" id="theme1" name="travelPurpose" value="휴식" ${dto.travelPurpose == '휴식' ? 'checked' : ''}>
                        <label for="theme1">🌴 휴식</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="theme2" name="travelPurpose" value="관광" ${dto.travelPurpose == '관광' ? 'checked' : ''}>
                        <label for="theme2">🌇 관광</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="theme3" name="travelPurpose" value="문화 체험" ${dto.travelPurpose == '문화 체험' ? 'checked' : ''}>
                        <label for="theme3">🏛 문화 체험</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="theme4" name="travelPurpose" value="출장/업무" ${dto.travelPurpose == '출장/업무' ? 'checked' : ''}>
                        <label for="theme4">💼 출장/업무</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="theme5" name="travelPurpose" value="모험/체험" ${dto.travelPurpose == '모험/체험' ? 'checked' : ''}>
                        <label for="theme5">🏔️ 모험/체험</label>
                    </div>
                </div>
            </div>

            <!-- 동행 -->
            <div class="form-group">
                <label>동행</label>
                <div class="option-grid">
                    <div class="option-item">
                        <input type="radio" id="comp1" name="companion" value="가족과 함께" ${dto.companion == '가족과 함께' ? 'checked' : ''}>
                        <label for="comp1">👪 가족과 함께</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="comp2" name="companion" value="친구와 함께" ${dto.companion == '친구와 함께' ? 'checked' : ''}>
                        <label for="comp2">👫 친구와 함께</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="comp3" name="companion" value="연인과 함께" ${dto.companion == '연인과 함께' ? 'checked' : ''}>
                        <label for="comp3">💑 연인과 함께</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="comp4" name="companion" value="혼자서" ${dto.companion == '혼자서' ? 'checked' : ''}>
                        <label for="comp4">👤 혼자서</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="comp5" name="companion" value="단체로" ${dto.companion == '단체로' ? 'checked' : ''}>
                        <label for="comp5">👥 단체로</label>
                    </div>
                </div>
            </div>

            <!-- 내용 -->
            <div class="form-row">
                <label>내용</label>
                <textarea name="content" placeholder="내용을 입력해주세요." required>${dto.content}</textarea>
            </div>

            <%--            <!-- 기존 이미지 출력 -->--%>
            <%--            <c:if test="${not empty dto.imagePath}">--%>
            <%--                <div class="form-row">--%>
            <%--                    <label>현재 사진</label>--%>
            <%--                    <img src="${pageContext.request.contextPath}/uploads/${dto.imagePath}"--%>
            <%--                         alt="기존 여행 사진"--%>
            <%--                         style="max-width:200px; border:1px solid #ccc; padding:4px;"/>--%>
            <%--                </div>--%>
            <%--            </c:if>--%>

            <!-- 사진 첨부 -->
            <div class="form-row">
                <label>사진 변경 (선택)</label>
                <!-- 기존 경로 유지용 hidden -->
                <input type="hidden" name="imagePath" value="${dto.imagePath}"/>

                <div class="file-upload" onclick="document.getElementById('imgInput').click()">
                    <p>파일 선택</p>
                    <small>새 사진을 선택하지 않으면 기존 사진이 유지됩니다.</small>
                    <input type="file" id="imgInput" name="travelImg" accept=".jpg,.jpeg,.png"/>
                </div>
            </div>

            <button type="submit" class="btn-submit">수정하기</button>
        </form>
    </main>
</div>
</body>
</html>

