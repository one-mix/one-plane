<%--작성자:방대혁--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<%--<link rel="stylesheet" href="/css/mypage/mypageContent.css" />--%>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>추천받은 여행지</title>
    <link rel="stylesheet" href="/css/mypage/recommendMain.css" />
    <link rel="stylesheet" href="/css/mypage/myRecommend.css" />
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


    <div class="content-area">
        <div class="top">
            <h1 class="page-title">추천받은 여행지</h1>
            <a href="/recommend" class="recommend-button">추천받기</a>
        </div>

        <c:set var="itemCount" value="${fn:length(recommendHistory)}"/>
        <c:choose>
            <c:when test="${itemCount == 1}">
                <c:set var="gridClass" value="single-item"/>
            </c:when>
            <c:when test="${itemCount == 2}">
                <c:set var="gridClass" value="two-items"/>
            </c:when>
            <c:when test="${itemCount == 3}">
                <c:set var="gridClass" value="three-items"/>
            </c:when>
            <c:otherwise>
                <c:set var="gridClass" value=""/>
            </c:otherwise>
        </c:choose>

        <div id="recommendations-container" class="${gridClass}">
            <c:if test="${empty recommendHistory and empty errorMessage}">
                <div class="empty-state">
                    <h3>아직 추천받은 여행지가 없어요</h3>
                    <p>새로운 여행지 추천을 받아보세요!<br>맞춤형 추천으로 완벽한 여행을 계획해보세요.</p>
                </div>
            </c:if>

            <c:forEach var="recommend" items="${recommendHistory}">
                <div class="recommendation-card">
                    <div class="card-image ${empty recommend.countryImg ? 'no-image' : ''}">
                        <c:choose>
                            <c:when test="${not empty recommend.countryImg}">
                                <img src="${recommend.countryImg}" alt="${recommend.countryNameKo}"/>
                            </c:when>
                            <c:otherwise><span>🌍</span></c:otherwise>
                        </c:choose>
                        <c:if test="${not empty recommend.continent}">
                            <span class="destination-type">${recommend.continent}</span>
                        </c:if>
                    </div>

                    <!-- 오버레이 -->
                    <div class="card-content">
                        <div class="overlay-header">
                            <div>
                                <h3 class="destination-name">${recommend.countryNameKo}</h3>
                                <c:if test="${not empty recommend.city}">
                                    <p class="destination-city">${recommend.city}</p>
                                </c:if>
                            </div>
                            <div class="overlay-date">
                                <c:choose>
                                    <c:when test="${not empty recommend.createdAt}">
                                        <fmt:formatDate value="${recommend.createdAt}" pattern="yyyy.MM.dd"/>
                                    </c:when>
                                    <c:otherwise>날짜 없음</c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="overlay-body">
                            <c:if test="${not empty recommend.recommendRating}">
                                <div class="overlay-rating" style="--rating:${recommend.recommendRating}">
                                    <span class="overlay-stars"></span>
                                    <span class="overlay-rating-text">${recommend.recommendRating}/5</span>
                                </div>
                            </c:if>
                            <div class="overlay-tags">
                                <c:if test="${not empty recommend.travelPurpose}">
                                    <span class="overlay-tag">${recommend.travelPurpose}</span>
                                </c:if>
                                <c:if test="${not empty recommend.companion}">
                                    <span class="overlay-tag">${recommend.companion}</span>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <button class="delete-button" onclick="deleteRecommend(${recommend.recommendId})">삭제</button>
                </div>
            </c:forEach>
        </div>

        <!-- 페이지네이션 -->
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:choose>
                    <c:when test="${hasPrevious}">
                        <a href="?page=1">&laquo;</a>
                        <a href="?page=${currentPage-1}">&lt;</a>
                    </c:when>
                    <c:otherwise>
                        <span class="disabled">&laquo;</span>
                        <span class="disabled">&lt;</span>
                    </c:otherwise>
                </c:choose>

                <c:forEach var="i" begin="${currentPage - 2 < 1 ? 1 : currentPage - 2}"
                           end="${currentPage + 2 > totalPages ? totalPages : currentPage + 2}">
                    <c:choose>
                        <c:when test="${currentPage == i}">
                            <span class="active">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="?page=${i}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:choose>
                    <c:when test="${hasNext}">
                        <a href="?page=${currentPage+1}">&gt;</a>
                        <a href="?page=${totalPages}">&raquo;</a>
                    </c:when>
                    <c:otherwise>
                        <span class="disabled">&gt;</span>
                        <span class="disabled">&raquo;</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </div>
</div>
</body>


<script>
    function deleteRecommend(id) {
        if (confirm("정말 삭제하시겠습니까?")) {
            fetch("/api/recommend/delete/" + id, {method: "POST"})
                .then(res => res.text())
                .then(msg => {
                    alert(msg);
                    location.reload(); // 삭제 후 새로고침
                });
        }
    }
</script>
</html>
