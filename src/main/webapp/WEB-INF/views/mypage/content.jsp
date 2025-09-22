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

    <link rel="stylesheet" href="/css/mypage/mypageContent.css" />

</head>
<body>

<%--<p>Debug - timeline count: ${fn:length(timeline)}</p>--%>
<%--<p>Debug - maxDistance: ${maxDistance}</p>--%>


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

    <!-- 메인 컨텐츠 -->
    <div class="content-area">
        <c:if test="${empty timeline}">
            <p>아직 타임라인 데이터가 없습니다.</p>
        </c:if>

        <c:if test="${not empty timeline}">
            <!-- 타임라인 영역 -->
            <div class="timeline-container">
                <div class="timeline-bar">
                    <div class="timeline-line"></div>
                    <div class="timeline-label-max">${maxDistance}km</div>

                    <c:forEach var="item" items="${timeline}">
                        <c:set var="pos" value="${item.cumulativeDistance / maxDistance * 100}" />
                        <div class="timeline-item" style="left:${pos}%">
                            <div class="icon-wrapper">
                                <img src="${pageContext.request.contextPath}/images/airplane.png"
                                     alt="plane" class="icon-plane"/>
                            </div>
                            <div class="info">
                                <div class="date"><c:out value="${item.certificationDate}" /></div>
                                <div class="dist"><c:out value="${item.cumulativeDistance}" />km</div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>


        <div class="content-title">
            <span class="title-text">방문한 여행지</span>
            <div class="actions">
                <a href="${pageContext.request.contextPath}/mypage/certification/add" class="btn-add">추가하기</a>
            </div>
        </div>




        <!-- 통계 영역 -->
        <div class="stats-container">
            <div class="stat-item">
                <div class="stat-number"><c:out value="${countryCount}" /></div>
                <div class="stat-label">국가 수</div>
            </div>
            <div class="stat-item">
                <div class="stat-number"><fmt:formatNumber value="${totalDistance}" pattern="#,###" /></div>
                <div class="stat-label">이동 거리 (km)</div>
            </div>
            <div class="stat-item">
                <div class="stat-number"><c:out value="${photoCount}" /></div>
                <div class="stat-label">업로드 사진</div>
            </div>
        </div>

        <div class="recommend-title">추천받은 여행지</div>

        <div class="recommend-section">
            <c:choose>
                <%-- 추천 없음 --%>
                <c:when test="${empty recommendedPlaces}">
                    <div class="recommend-empty">
                        <p>아직 여러분의 다음 여행지는 정해지지 않았습니다.</p>
                        <a href="${pageContext.request.contextPath}/recommend" class="btn-recommend">
                            여행지 추천 받으러 가기 →
                        </a>
                    </div>
                </c:when>

                <%-- 추천 있음 --%>
                <c:otherwise>
                    <div class="recommend-container">
                        <c:forEach items="${recommendedPlaces}" var="place" begin="0" end="3">
                            <div class="recommend-card">
                                <div class="card-thumb"
                                     style="background-image:url(${place.thumbUrl});">
                                </div>
                                <div class="card-info">
                                    <div class="card-country">
                                        <c:out value="${place.country}" />
                                    </div>
                                    <div class="card-name">
                                        <c:out value="${place.name}" />
                                    </div>
                                    <div class="card-desc">
                                        <c:out value="${place.description}" />
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <script>
        // 프로필 아바타 클릭 시 파일 선택창 열기
        document.getElementById('profileAvatar').addEventListener('click', function () {
            document.getElementById('avatarInput').click();
        });

        function uploadAvatar() {
            const input = document.getElementById('avatarInput');
            const file = input.files[0];
            if (!file) return;

            // 파일 타입 확인
            if (!file.type.startsWith('image/')) {
                alert('이미지 파일만 업로드 가능합니다.');
                input.value = '';
                return;
            }
            // 파일 크기 확인 (5MB 제한)
            if (file.size > 5 * 1024 * 1024) {
                alert('파일 크기는 5MB 이하이어야 합니다.');
                input.value = '';
                return;
            }

            const formData = new FormData();
            formData.append('profileImage', file);

            // 업로드 진행
            fetch('/mypage/profile/uploadImage', {
                method: 'POST',
                body: formData
            })
                .then(response => response.text())
                .then(result => {
                    if (result === 'success') {
                        // 즉시 미리보기
                        const reader = new FileReader();
                        reader.onload = e => {
                            document.getElementById('avatarImg').src = e.target.result;
                        };
                        reader.readAsDataURL(file);
                        alert('프로필 사진이 변경되었습니다.');
                    } else {
                        alert('업로드에 실패했습니다.');
                    }
                })
                .catch(() => {
                    alert('업로드 중 오류가 발생했습니다.');
                })
                .finally(() => {
                    input.value = '';
                });
        }
    </script>

</div>
</body>


</html>