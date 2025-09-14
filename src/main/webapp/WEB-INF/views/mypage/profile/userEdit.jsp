<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>프로필 수정 - 원믹스</title>
    <link rel="stylesheet" href="/css/mypage/mypageContent.css" />
    <link rel="stylesheet" href="/css/mypage/userprofile.css" />

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
                        <li><a href="/section/recommend" class="${activeMenu == 'recommend' ? 'active' : ''}">추천받은</a></li>
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
                            </c:otherwise>
                        </c:choose>

                    </ul>
                </div>
            </div>

            <div class="main">
                <div class="header">
                    <h1>프로필 수정</h1>
                    <p>개인정보를 안전하게 관리하세요</p>
                </div>

                <div class="form-container">
                    <!-- 성공/오류 메시지 -->
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success">${successMessage}</div>
                    </c:if>
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">${errorMessage}</div>
                    </c:if>

                    <form action="<c:url value='/mypage/profile/edit'/>" method="post">
                        <!-- 이름 (읽기 전용) -->
                        <div class="form-group">
                            <label for="name">이름</label>
                            <input type="text"
                                   id="name"
                                   class="form-control"
                                   value="${profile.name}"
                                   readonly>
                        </div>

                        <!-- 나이 (읽기 전용) -->
                        <div class="form-group">
                            <label for="age">나이</label>
                            <input type="number"
                                   id="age"
                                   class="form-control"
                                   value="${profile.age}"
                                   readonly>
                        </div>

                        <!-- 등급 (읽기 전용) -->
                        <div class="form-group">
                            <label for="grade">등급</label>
                            <input type="text"
                                   id="grade"
                                   class="form-control"
                                   value="${profile.grade}"
                                   readonly>
                        </div>

                        <!-- 성별 (읽기 전용) -->
                        <div class="form-group">
                            <label for="gender">성별</label>
                            <input type="text"
                                   id="gender"
                                   class="form-control"
                                   value="${profile.gender == 'male' ? '남자' : '여자'}"
                                   readonly>
                        </div>

                        <!-- 이메일 (수정 가능) -->
                        <div class="form-group">
                            <label for="email">이메일</label>
                            <input type="email"
                                   id="email"
                                   name="email"
                                   class="form-control"
                                   value="${profile.email}"
                                   required>
                            <div class="form-text">로그인 및 알림에 사용됩니다.</div>
                        </div>

                        <!-- 닉네임 (수정 가능) -->
                        <div class="form-group">
                            <label for="nickname">닉네임</label>
                            <input type="text"
                                   id="nickname"
                                   name="nickname"
                                   class="form-control"
                                   value="${profile.nickname}"
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
                                    ${profile.disease eq 'true' ? 'checked' : ''}>
                                    <label for="disease_true">있음</label>
                                </div>
                                <div class="radio-option">
                                    <input type="radio"
                                           id="disease_false"
                                           name="disease"
                                           value="false"
                                    ${profile.disease eq 'false' ? 'checked' : ''}>
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
                                    ${profile.disability eq 'true' ? 'checked' : ''}>
                                    <label for="disability_true">있음</label>
                                </div>
                                <div class="radio-option">
                                    <input type="radio"
                                           id="disability_false"
                                           name="disability"
                                           value="false"
                                    ${profile.disability eq 'false' ? 'checked' : ''}>
                                    <label for="disability_false">없음</label>
                                </div>
                            </div>
                            <div class="form-text">맞춤형 운동 프로그램 제공을 위해 필요합니다.</div>
                        </div>

                        <!-- 복용약 여부 -->
                        <div class="form-group">
                            <label>복용약 여부</label>
                            <div class="radio-group">
                                <div class="radio-option">
                                    <input type="radio"
                                           id="medication_true"
                                           name="medication"
                                           value="true"
                                    ${profile.medication eq 'true' ? 'checked' : ''}>
                                    <label for="medication_true">복용 중</label>
                                </div>
                                <div class="radio-option">
                                    <input type="radio"
                                           id="medication_false"
                                           name="medication"
                                           value="false"
                                    ${profile.medication eq 'false' ? 'checked' : ''}>
                                    <label for="medication_false">복용하지 않음</label>
                                </div>
                            </div>
                            <div class="form-text">안전한 운동을 위해 중요한 정보입니다.</div>
                        </div>

                        <div class="btn-group">
                            <button type="submit" class="btn btn-primary">저장하기</button>
                            <a href="<c:url value='/mypage'/>" class="btn btn-secondary">취소</a>
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
