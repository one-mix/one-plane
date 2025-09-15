<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<link href="/css/postList.css" rel="stylesheet">
<link rel="stylesheet" href="/css/mypage/mypageContent.css" />


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


    <main class="main-container">
        <div class="top-tabs-wrapper">
            <div class="top-tabs">
                <a href="/myPost/followerPost" class="top-tab ${empty selectedCategory ? 'active' : 'null'}">전체</a>
                <a href="/myPost/followerPost?category=READY" class="top-tab ${selectedCategory eq 'READY' ? 'active' : ''}">준비</a>
                <a href="/myPost/followerPost?category=REVIEW" class="to  p-tab ${selectedCategory eq 'REVIEW' ? 'active' : ''}">후기</a>
                <a href="/myPost/followerPost?category=ACCOMPANY" class="top-tab ${selectedCategory eq 'ACCOMPANY' ? 'active' : ''}">동행</a>
                <a href="/myPost/followerPost?category=FREE" class="top-tab ${selectedCategory eq 'FREE' ? 'active' : ''}">자유</a>
            </div>


            <div class="conversion">
                <a href="<c:url value='/myPost/myPost'/>"
                   class="top-tab ${tab == 'my' ? 'active' : ''}">내가 작성한</a>
                <a href="<c:url value='/myPost/followerPost'/>"
                   class="top-tab ${tab == 'follower' ? 'active' : ''}">팔로워가 작성한</a>
            </div>


            <div class="search-area">
                <form method="get" action="/myPost/followerPost" style="display: flex; gap: 12px; align-items: center;">
                    <input type="hidden" name="category" value="${selectedCategory}">
                    <select name="searchType" class="country-dropdown">
                        <option value="title" ${searchType eq 'title' ? 'selected' : ''}>제목</option>
                        <option value="content" ${searchType eq 'content' ? 'selected' : ''}>내용</option>
                        <option value="author" ${searchType eq 'author' ? 'selected' : ''}>작성자</option>
                    </select>
                    <input type="text" name="search" class="search-input-top" placeholder="입력해 주세요" value="${searchKeyword}" onkeypress="if(event.keyCode==13) this.form.submit();">
                    <a href="/post/write" class="search-btn-top" style="text-decoration: none;">글쓰기</a>
                </form>
            </div>
        </div>

        <div class="results-section">
            <c:choose>
                <c:when test="${not empty posts}">
                    <c:forEach var="post" items="${posts}">
                        <div class="result-card" onclick="location.href='/post/detail/${post.postId}'">
                            <div class="result-info">
                                <div style="margin-bottom: 8px;">
                                    <span class="country-badge">
                                        <c:choose>
                                            <c:when test="${not empty post.country}">
                                                <c:choose>
                                                    <c:when test="${post.country eq 'japan'}">일본</c:when>
                                                    <c:when test="${post.country eq 'korea'}">한국</c:when>
                                                    <c:when test="${post.country eq 'china'}">중국</c:when>
                                                    <c:when test="${post.country eq 'usa'}">미국</c:when>
                                                    <c:when test="${post.country eq 'thailand'}">태국</c:when>
                                                    <c:when test="${post.country eq 'vietnam'}">베트남</c:when>
                                                    <c:when test="${post.country eq 'singapore'}">싱가포르</c:when>
                                                    <c:when test="${post.country eq 'malaysia'}">말레이시아</c:when>
                                                    <c:when test="${post.country eq 'philippines'}">필리핀</c:when>
                                                    <c:when test="${post.country eq 'france'}">프랑스</c:when>
                                                    <c:when test="${post.country eq 'italy'}">이탈리아</c:when>
                                                    <c:when test="${post.country eq 'spain'}">스페인</c:when>
                                                    <c:when test="${post.country eq 'germany'}">독일</c:when>
                                                    <c:when test="${post.country eq 'uk'}">영국</c:when>
                                                    <c:otherwise>${post.country}</c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>미분류</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>

                                <h3>${post.title}</h3>

                                <p>
                                    <c:set var="content" value="${post.content}" />
                                    <c:set var="plainText" value="${content.replaceAll('<[^>]*>', '').replaceAll('&nbsp;', ' ')}" />
                                    <c:choose>
                                        <c:when test="${plainText.length() > 100}">
                                            ${plainText.substring(0, 100)}...
                                        </c:when>
                                        <c:otherwise>
                                            ${plainText}
                                        </c:otherwise>
                                    </c:choose>
                                </p>

                                <div class="result-details">
                                        ${post.authorName} &nbsp;&nbsp;
                                    <fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd HH:mm" /> &nbsp;&nbsp;
                                    조회 ${post.viewCount} &nbsp;&nbsp;
                                    댓글 ${post.commentCount} &nbsp;&nbsp;
                                    좋아요 ${post.likeCount}
                                </div>
                            </div>

                            <c:if test="${not empty post.thumbnailImage}">
                                <div class="result-icon">
                                    <img src="${post.thumbnailImage}" alt="thumbnail" style="width: 60px; height: 60px; object-fit: cover; border-radius: 8px;">
                                </div>
                            </c:if>
                            <!-- 사진이 없다면 빈 사진으로 -->
                            <c:if test="${empty post.thumbnailImage}">
                                <div class="result-icon">
                                    <img src="/images/aimg.png" alt="no image" style="width: 60px; height: 60px; object-fit: cover; border-radius: 8px; opacity: 0.3;">
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <h3>게시글이 없습니다</h3>
                        <p>
                            <c:choose>
                                <c:when test="${not empty selectedCategory}">
                                    선택한 카테고리에 게시글이 없습니다.
                                </c:when>
                                <c:otherwise>
                                    아직 작성된 게시글이 없습니다. <br>
                                    첫 번째 게시글을 작성해보세요!
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <sec:authorize access="isAuthenticated()">
                            <a href="/post/write" class="search-btn-top" style="margin-top: 20px; display: inline-block;">글쓰기</a>
                        </sec:authorize>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${totalPages > 1}">
            <div class="pagination-container">
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <a href="?page=1&category=${selectedCategory}&searchType=${searchType}&search=${searchKeyword}&sortBy=${sortBy}" class="pagination-btn">«</a>
                    </c:when>
                    <c:otherwise>
                        <span class="pagination-btn disabled">«</span>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${hasPrevious}">
                        <a href="?page=${currentPage - 1}&category=${selectedCategory}&searchType=${searchType}&search=${searchKeyword}&sortBy=${sortBy}" class="pagination-btn">‹</a>
                    </c:when>
                    <c:otherwise>
                        <span class="pagination-btn disabled">‹</span>
                    </c:otherwise>
                </c:choose>

                <c:set var="startPage" value="${((currentPage - 1) / 5) * 5 + 1}" />
                <c:set var="endPage" value="${startPage + 4 > totalPages ? totalPages : startPage + 4}" />

                <c:forEach var="i" begin="${startPage}" end="${endPage}">
                    <c:choose>
                        <c:when test="${i eq currentPage}">
                            <span class="pagination-btn active">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="?page=${i}&category=${selectedCategory}&searchType=${searchType}&search=${searchKeyword}&sortBy=${sortBy}" class="pagination-btn">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:choose>
                    <c:when test="${hasNext}">
                        <a href="?page=${currentPage + 1}&category=${selectedCategory}&searchType=${searchType}&search=${searchKeyword}&sortBy=${sortBy}" class="pagination-btn">›</a>
                    </c:when>
                    <c:otherwise>
                        <span class="pagination-btn disabled">›</span>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <a href="?page=${totalPages}&category=${selectedCategory}&searchType=${searchType}&search=${searchKeyword}&sortBy=${sortBy}" class="pagination-btn">»</a>
                    </c:when>
                    <c:otherwise>
                        <span class="pagination-btn disabled">»</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </main>
</div>

<script>

    document.querySelector('form').addEventListener('submit', function(e) {
        const searchInput = document.querySelector('input[name="search"]');
        if (searchInput && searchInput.value.trim() === '') {
            return true;
        }
    });
</script>