<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>친구 관리 - OnePlane</title>

    <!-- CSRF 토큰 메타태그 -->
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">

    <sec:authorize access="isAuthenticated()">
        <sec:authentication property="principal" var="userDetails" />
        <meta name="user-id" content="${userDetails.userId}">
        <meta name="user-nickname" content="${userDetails.nickname}">
    </sec:authorize>

    <link href="/css/layout.css" rel="stylesheet">
    <link href="/css/friends.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>

<div class="friends-container">

    <!-- 메인 컨텐츠 -->
    <main class="main-content">
        <!-- 탭 메뉴 -->
        <div class="tab-menu">
            <button class="tab-btn ${tab == 'following' || empty tab ? 'active' : ''}"
                    onclick="changeTab('following')">
                내가 팔로우한
            </button>
            <button class="tab-btn ${tab == 'followers' ? 'active' : ''}"
                    onclick="changeTab('followers')">
                나를 팔로우한
            </button>
        </div>

        <!-- 검색 섹션 -->
        <div class="search-section">
            <div class="search-box">
                <i class="bi bi-search"></i>
                <input type="text" id="searchInput" placeholder="검색할 닉네임을 입력해주세요"
                       value="${search}" onkeypress="handleSearchKeyPress(event)">
                <button type="button" class="search-btn" onclick="performSearch()">검색</button>
            </div>
        </div>

        <!-- 친구 목록 -->
        <div class="friends-grid">
            <c:choose>
                <c:when test="${tab == 'search'}">
                    <!-- 검색 결과 -->
                    <c:choose>
                        <c:when test="${not empty searchResults}">
                            <c:forEach var="friend" items="${searchResults}" varStatus="status">
                                <div class="friend-card">
                                    <div class="friend-avatar">
                                        <c:choose>
                                            <c:when test="${not empty friend.followingProfileImg}">
                                                <img src="${friend.followingProfileImg}" alt="${friend.followingNickname}의 프로필">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="/images/profile.png" alt="기본 프로필">
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="friend-info">
                                        <h4 class="friend-name">${friend.followingNickname}</h4>
                                        <button class="follow-action-btn"
                                                data-user-id="${friend.followingId}"
                                                onclick="toggleFollow(${friend.followingId}, this)">
                                            <span class="follow-text">언팔로우</span>
                                        </button>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="bi bi-search"></i>
                                <p>'${search}' 검색 결과가 없습니다.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <!-- 일반 친구 목록 -->
                    <c:choose>
                        <c:when test="${not empty friendList}">
                            <c:forEach var="friend" items="${friendList}" varStatus="status">
                                <div class="friend-card">
                                    <div class="friend-avatar">
                                        <c:choose>
                                            <c:when test="${tab == 'followers'}">
                                                <c:choose>
                                                    <c:when test="${not empty friend.followerProfileImg}">
                                                        <img src="${friend.followerProfileImg}" alt="${friend.followerNickname}의 프로필">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="/images/profile.png" alt="기본 프로필">
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${not empty friend.followingProfileImg}">
                                                        <img src="${friend.followingProfileImg}" alt="${friend.followingNickname}의 프로필">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="/images/profile.png" alt="기본 프로필">
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="friend-info">
                                        <c:choose>
                                            <c:when test="${tab == 'followers'}">
                                                <h4 class="friend-name">${friend.followerNickname}</h4>
                                                <button class="follow-action-btn"
                                                        data-user-id="${friend.followerId}"
                                                        onclick="toggleFollow(${friend.followerId}, this)">
                                                    <span class="follow-text">언팔로우</span>
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <h4 class="friend-name">${friend.followingNickname}</h4>
                                                <button class="follow-action-btn"
                                                        data-user-id="${friend.followingId}"
                                                        onclick="toggleFollow(${friend.followingId}, this)">
                                                    <span class="follow-text">언팔로우</span>
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="bi bi-people"></i>
                                <c:choose>
                                    <c:when test="${tab == 'followers'}">
                                        <p>아직 나를 팔로우한 사람이 없습니다.</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p>아직 팔로우한 사람이 없습니다.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- 페이지네이션 -->
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="javascript:void(0)" class="page-btn" onclick="goToPage(${currentPage - 1})">
                        <i class="bi bi-chevron-left"></i>
                    </a>
                </c:if>

                <c:forEach begin="${startPage}" end="${endPage}" var="pageNum">
                    <c:choose>
                        <c:when test="${pageNum == currentPage}">
                            <span class="page-btn active">${pageNum}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="javascript:void(0)" class="page-btn" onclick="goToPage(${pageNum})">${pageNum}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="javascript:void(0)" class="page-btn" onclick="goToPage(${currentPage + 1})">
                        <i class="bi bi-chevron-right"></i>
                    </a>
                </c:if>
            </div>
        </c:if>
    </main>
</div>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

<script>
    // 전역 변수
    let currentTab = '${tab}' || 'following';
    let currentPage = ${currentPage} || 1;
    let currentSearch = '${search}' || '';

    $(document).ready(function() {
        initializePage();
    });

    // 페이지 초기화
    function initializePage() {
        // 팔로우 상태 로드
        loadFollowStates();
    }

    // 탭 변경
    function changeTab(tab) {
        if (currentTab === tab) return;

        currentTab = tab;
        currentPage = 1;
        currentSearch = '';

        // 검색창 초기화
        $('#searchInput').val('');

        // URL 변경
        const url = new URL(window.location);
        url.searchParams.set('tab', tab);
        url.searchParams.set('page', '1');
        url.searchParams.delete('search');

        window.location.href = url.toString();
    }

    // 검색 수행
    function performSearch() {
        const searchTerm = $('#searchInput').val().trim();

        if (!searchTerm) {
            alert('검색어를 입력해주세요.');
            return;
        }

        currentSearch = searchTerm;
        currentPage = 1;

        // URL 변경
        const url = new URL(window.location);
        url.searchParams.set('search', searchTerm);
        url.searchParams.set('page', '1');

        window.location.href = url.toString();
    }

    // 검색 엔터키 처리
    function handleSearchKeyPress(event) {
        if (event.key === 'Enter') {
            performSearch();
        }
    }

    // 페이지 이동
    function goToPage(page) {
        if (page === currentPage) return;

        const url = new URL(window.location);
        url.searchParams.set('page', page);

        window.location.href = url.toString();
    }

    // 팔로우/언팔로우 토글
    function toggleFollow(targetUserId, buttonElement) {
        const $button = $(buttonElement);
        const $followText = $button.find('.follow-text');

        // 버튼 비활성화
        $button.prop('disabled', true);

        $.ajax({
            url: '/friends/toggle',
            type: 'POST',
            data: {
                targetUserId: targetUserId
            },
            beforeSend: function(xhr) {
                const token = $('meta[name="_csrf"]').attr('content');
                const header = $('meta[name="_csrf_header"]').attr('content');
                if (token && header) {
                    xhr.setRequestHeader(header, token);
                }
            },
            success: function(response) {
                if (response.success) {
                    if (response.action === 'followed') {
                        $button.addClass('following');
                        $followText.text('팔로잉');
                    } else {
                        $button.removeClass('following');
                        $followText.text('팔로우');
                    }

                    // 성공 메시지
                    showToast(response.message);

                    // 필요한 경우 페이지 새로고침
                    setTimeout(() => {
                        location.reload();
                    }, 1000);

                } else {
                    showToast(response.message || '팔로우 처리에 실패했습니다.', 'error');
                }
            },
            error: function(xhr) {
                if (xhr.status === 401) {
                    if (confirm('로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?')) {
                        window.location.href = '/oauth2/authorization/kakao';
                    }
                } else {
                    showToast('팔로우 처리 중 오류가 발생했습니다.', 'error');
                }
            },
            complete: function() {
                // 버튼 재활성화
                $button.prop('disabled', false);
            }
        });
    }

    // 팔로우 상태 로드
    function loadFollowStates() {
        $('.follow-action-btn').each(function() {
            const $button = $(this);
            const userId = $button.data('user-id');

            if (userId) {
                $.ajax({
                    url: '/friends/status/' + userId,
                    type: 'GET',
                    success: function(response) {
                        if (response.success) {
                            const $followText = $button.find('.follow-text');
                            if (response.isFollowing) {
                                $button.addClass('following');
                                $followText.text('팔로잉');
                            } else {
                                $button.removeClass('following');
                                $followText.text('팔로우');
                            }
                        }
                    },
                    error: function(xhr) {
                        console.error('팔로우 상태 확인 실패:', xhr);
                    }
                });
            }
        });
    }

    function showToast(message, type = 'success') {
        alert(message);
    }
</script>
</body>
</html>