<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>프로필 수정 - 원믹스</title>
    <link rel="stylesheet" href="/css/mypage/userInfo.css" />

</head>
<body>
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

        <div class="menu-section">
            <div class="menu-title">게시글</div>
            <a href="<c:url value='/myPost/myPost'/>"
               class="menu-item ${activeMenu == 'myPost' ? 'active' : ''}">내가 작성</a>
            <a href="<c:url value='/api/posts/following/${currentUserId}'/>"
               class="menu-item ${activeMenu == 'followerPost' ? 'active' : ''}">
                팔로워가 작성
            </a>
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
            <a href="<c:url value='/mypage/profile/userEdit'/>"
               class="menu-item ${activeMenu == 'editProfile' ? 'active' : ''}">프로필 편집</a>
            <a href="<c:url value='/mypage/profile/out'/>" class="menu-item danger">회원탈퇴</a>
        </div>
    </div>

    <div class="main-content">
        <div class="header">
            <h1>프로필 수정</h1>
            <p>개인정보를 안전하게 관리하세요</p>
        </div>

        <div class="form-container">
            <!-- 성공/오류 메시지 -->
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">
                        ${successMessage}
                </div>
            </c:if>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                        ${errorMessage}
                </div>
            </c:if>

            <!-- 🚀 핵심: 실제 폼 구현 -->
            <form action="/mypage/profile/userEdit" method="post">
                <!-- 이메일 -->
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email"
                           id="email"
                           name="email"
                           class="form-control"
                           value="${user.email}"
                           required>
                    <div class="form-text">로그인 및 알림에 사용됩니다.</div>
                </div>

                <!-- 닉네임 -->
                <div class="form-group">
                    <label for="nickname">닉네임</label>
                    <input type="text"
                           id="nickname"
                           name="nickname"
                           class="form-control"
                           value="${user.nickname}"
                           maxlength="20"
                           required>
                    <div class="form-text">다른 사용자에게 표시되는 이름입니다.</div>
                </div>

                <!-- 질병 여부 -->
                <div class="form-group">
                    <label>질병 여부</label>
                    <div class="radio-group">
                        <div class="radio-option">
                            <input type="radio"
                                   id="disease_true"
                                   name="disease"
                                   value="true"
                            ${user.disease eq 'true' ? 'checked' : ''}>
                            <label for="disease_true">있음</label>
                        </div>
                        <div class="radio-option">
                            <input type="radio"
                                   id="disease_false"
                                   name="disease"
                                   value="false"
                            ${user.disease eq 'false' ? 'checked' : ''}>
                            <label for="disease_false">없음</label>
                        </div>
                    </div>
                    <div class="form-text">운동 계획 수립에 참고됩니다.</div>
                </div>

                <!-- 장애 여부 -->
                <div class="form-group">
                    <label>장애 여부</label>
                    <div class="radio-group">
                        <div class="radio-option">
                            <input type="radio"
                                   id="disability_true"
                                   name="disability"
                                   value="true"
                            ${user.disability eq 'true' ? 'checked' : ''}>
                            <label for="disability_true">있음</label>
                        </div>
                        <div class="radio-option">
                            <input type="radio"
                                   id="disability_false"
                                   name="disability"
                                   value="false"
                            ${user.disability eq 'false' ? 'checked' : ''}>
                            <label for="disability_false">없음</label>
                        </div>
                    </div>
                    <div class="form-text">맞춤형 운동 프로그램 제공을 위해 필요합니다.</div>
                </div>

                <!-- 투약 여부 -->
                <div class="form-group">
                    <label>투약 여부</label>
                    <div class="radio-group">
                        <div class="radio-option">
                            <input type="radio"
                                   id="medication_true"
                                   name="medication"
                                   value="true"
                            ${user.medication eq 'true' ? 'checked' : ''}>
                            <label for="medication_true">복용 중</label>
                        </div>
                        <div class="radio-option">
                            <input type="radio"
                                   id="medication_false"
                                   name="medication"
                                   value="false"
                            ${user.medication eq 'false' ? 'checked' : ''}>
                            <label for="medication_false">복용하지 않음</label>
                        </div>
                    </div>
                    <div class="form-text">안전한 운동을 위해 중요한 정보입니다.</div>
                </div>

                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">
                        저장하기
                    </button>
                    <a href="/mypage" class="btn btn-secondary">
                        취소
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // 폼 유효성 검사
    document.querySelector('form').addEventListener('submit', function(e) {
        const email = document.getElementById('email').value.trim();
        const nickname = document.getElementById('nickname').value.trim();

        if (!email) {
            e.preventDefault();
            alert('이메일을 입력해주세요.');
            return;
        }

        if (!nickname) {
            e.preventDefault();
            alert('닉네임을 입력해주세요.');
            return;
        }

        // 이메일 형식 검사
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            e.preventDefault();
            alert('올바른 이메일 형식을 입력해주세요.');
            return;
        }
    });

    // 라디오 버튼 스타일 개선
    document.querySelectorAll('input[type="radio"]').forEach(function(radio) {
        radio.addEventListener('change', function() {
            document.querySelectorAll('input[name="' + this.name + '"]').forEach(function(r) {
                r.closest('.radio-option').classList.remove('active');
            });
            this.closest('.radio-option').classList.add('active');
        });
    });

    // 페이지 로드 시 선택된 라디오 버튼에 active 클래스 추가
    document.addEventListener('DOMContentLoaded', function() {
        document.querySelectorAll('input[type="radio"]:checked').forEach(function(radio) {
            radio.closest('.radio-option').classList.add('active');
        });
    });
</script>
</body>
</html>