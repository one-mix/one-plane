<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<style>
    /* 부모 컨테이너 */
    .container {
        width: 1200px;
        margin: 0 auto;
        padding: 0;
    }

    /* 1. .page-wrapper에 중앙 정렬 속성 추가 */
    .page-wrapper {
        display: flex;
        justify-content: center;   /* 가로 중앙 정렬 */
        align-items: flex-start;   /* 세로는 상단 정렬(필요 시 center로 변경 가능) */
        gap: 40px;
    }

    /* 2. .main-container에 너비 및 내부 여백 지정 */
    .main-container {
        width: 800px;              /* 원하는 고정 너비 */
        max-width: 100%;           /* 모바일 등 좁은 화면 대응 */
        padding: 40px;             /* 내부 여백 확대 */
        background: #fff;          /* 배경색 (선택) */
        border: 1px solid #ddd;    /* 테두리 (선택) */
        border-radius: 8px;        /* 모서리 둥글게 */
        box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        margin-top: 70px;
    }
</style>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>방문한 여행지</title>

    <link rel="stylesheet" href="/css/mypage/mypageContent.css" />
    <link rel="stylesheet" href="/css/mypage/userOut.css" />

</head>

<body>
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


    <div class="main-container">
        <h2 class="withdraw-title">회원탈퇴</h2>
        <div class="withdraw-warning">
            회원탈퇴 시 개인정보 및 OnePlane에서 만들어진 모든 데이터는 삭제됩니다.
        </div>

        <div class="withdraw-notice">
            <p>유의사항</p>
            <ol>
                <li>회원 탈퇴 시 더 이상 OnePlane 서비스 사용이 불가하며, 즉시 탈퇴 처리됩니다.</li>
                <li>고객 정보 및 개인정보 서비스 이용 기록은 개인정보보호 처리 방침 기준에 따라 삭제됩니다.</li>
                <li>업로드하신 모든 여행 사진 및 이미지 데이터의 영구 삭제되어 복구가 불가합니다.</li>
                <li>획득한 활동 포인트가 모두 소멸됩니다.</li>
                <li>탈퇴 후 동일한 이메일로 재가입 시에도 기존 데이터는 복구되지 않으며, 새로운 계정으로 다시 시작해야 합니다.</li>
            </ol>
        </div>

        <form action="${pageContext.request.contextPath}/mypage/profile/out" method="post" class="withdraw-form">
            <div class="form-group">
                <label for="reason">탈퇴사유</label>
                <select name="reason" id="reason" class="withdraw-select" required>
                    <option value="" disabled selected>탈퇴사유를 선택해주세요.</option>
                    <option value="service">서비스 불만</option>
                    <option value="privacy">개인정보 우려</option>
                    <option value="temporary">일시 탈퇴</option>
                    <option value="other">기타</option>
                </select>
            </div>

            <div class="form-group checkbox-group">
                <input type="checkbox" id="confirm" name="confirm" />
                <label for="confirm">위의 모든 안내사항을 확인하였으며, 데이터 삭제 및 복구 불가에 대해 동의합니다.</label>
            </div>

            <button type="submit" class="btn-withdraw" disabled>탈퇴하기</button>
        </form>
    </div>

    <script>
        const reasonSelect = document.getElementById('reason');
        const confirmCheckbox = document.getElementById('confirm');
        const submitBtn = document.querySelector('.btn-withdraw');

        function toggleButton() {
            submitBtn.disabled = !(reasonSelect.value && confirmCheckbox.checked);
        }

        reasonSelect.addEventListener('change', toggleButton);
        confirmCheckbox.addEventListener('change', toggleButton);
    </script>

</div>

</body>