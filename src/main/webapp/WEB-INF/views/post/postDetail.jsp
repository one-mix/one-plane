<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<link href="/css/postDetail.css" rel="stylesheet">

<main class="post-detail-container">
  <!-- 상단 네비게이션 -->
  <div class="post-nav">
    <a href="/post/list" class="back-btn">
      <i class="bi bi-arrow-left"></i> 목록으로
    </a>

    <c:if test="${canEdit || canDelete}">
      <div class="post-actions">
        <c:if test="${canEdit}">
          <a href="/post/edit/${post.postId}" class="btn-edit">수정</a>
        </c:if>
        <c:if test="${canDelete}">
          <button type="button" class="btn-delete" onclick="deletePost(${post.postId})">삭제</button>
        </c:if>
      </div>
    </c:if>
  </div>

  <!-- 게시글 헤더 -->
  <div class="post-header">

    <h1 class="post-title">${post.title}</h1>

    <div class="post-meta">
      <div class="author-info">
        <c:choose>
          <c:when test="${not empty post.user.profileImg}">
            <img src="${post.user.profileImg}" alt="프로필" class="author-avatar">
          </c:when>
          <c:otherwise>
            <img src="/images/profile.png" alt="프로필" class="author-avatar">
          </c:otherwise>
        </c:choose>

        <div class="author-details">
          <span class="author-name">${post.authorName}</span>
          <div class="post-stats">
                        <span class="post-date">
                            <fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd HH:mm" />
                        </span>
            <span class="divider">•</span>
            <span class="view-count">조회 <span id="viewCount">${post.viewCount}</span></span>
            <span class="divider">•</span>
            <span class="like-count">좋아요 ${post.likeCount}</span>
            <span class="divider">•</span>
            <span class="comment-count">댓글 ${post.commentCount}</span>
          </div>
        </div>
      </div>

      <div class="post-actions-right">
        <!-- 팔로우 버튼 -->
        <sec:authorize access="isAuthenticated()">
<%--          <c:if test="${post.userId != sessionScope.userId}">--%>
            <button type="button" class="follow-btn" onclick="toggleFollow(${post.userId})">
              <i class="bi bi-person-plus"></i>
              <span class="follow-text">팔로우</span>
            </button>
<%--          </c:if>--%>
        </sec:authorize>
      </div>

    </div>
  </div>

  <!-- 게시글 내용 -->
  <div class="post-content">
    <!-- Summernote로 작성된 HTML 콘텐츠를 그대로 출력 -->
    <div class="content-body">
      ${post.content}
    </div>
  </div>

  <!-- 게시글 푸터 (좋아요, 공유 등) -->
  <div class="post-footer">
    <div class="post-reactions">
      <button type="button" class="reaction-btn like-btn" onclick="toggleLike(${post.postId})">
        <i class="bi bi-heart"></i>
        <span>좋아요</span>
        <span class="reaction-count">${post.likeCount}</span>
      </button>
    </div>
  </div>

  <!-- 관련 게시글 또는 댓글 섹션 -->
  <div class="post-additional">
    <!-- 댓글 섹션 (추후 구현) -->
    <div class="comments-section">
      <h3 class="section-title">댓글 <span class="comment-count-text">${post.commentCount}</span></h3>

      <sec:authorize access="isAuthenticated()">
        <div class="comment-write">
          <textarea class="comment-input" placeholder="댓글을 작성해주세요..."></textarea>
          <button type="button" class="comment-submit-btn">댓글 등록</button>
        </div>
      </sec:authorize>

      <sec:authorize access="!isAuthenticated()">
        <div class="comment-login-required">
          <p>댓글을 작성하려면 <a href="/oauth2/authorization/kakao">로그인</a>이 필요합니다.</p>
        </div>
      </sec:authorize>

      <div class="comments-list">
        <!-- 댓글 목록이 여기에 동적으로 로드됩니다 -->
        <div class="no-comments">
          <p>아직 댓글이 없습니다. 첫 번째 댓글을 작성해보세요!</p>
        </div>
      </div>
    </div>
  </div>
</main>
<!-- Bootstrap Icons -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

<script>
  $(document).ready(function() {
    // 이미지 클릭 시 확대 보기
    $('.content-body img').on('click', function() {
      const imgSrc = $(this).attr('src');
      showImageModal(imgSrc);
    });

    // 외부 링크는 새 창에서 열기
    $('.content-body a[href^="http"]').attr('target', '_blank');
  });

  // 게시글 삭제
  function deletePost(postId) {
    if (!confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
      return;
    }

    $.ajax({
      url: '/post/delete/' + postId,
      type: 'POST',
      beforeSend: function(xhr) {
        const token = $('meta[name="_csrf"]').attr('content');
        const header = $('meta[name="_csrf_header"]').attr('content');
        if (token && header) {
          xhr.setRequestHeader(header, token);
        }
      },
      success: function(response) {
        if (response.success) {
          alert('게시글이 삭제되었습니다.');
          window.location.href = '/post/list';
        } else {
          alert(response.message || '게시글 삭제에 실패했습니다.');
        }
      },
      error: function(xhr) {
        let errorMsg = '게시글 삭제 중 오류가 발생했습니다.';
        if (xhr.responseJSON && xhr.responseJSON.message) {
          errorMsg = xhr.responseJSON.message;
        }
        alert(errorMsg);
      }
    });
  }

  // 좋아요 토글 (추후 구현)
  $(document).ready(function() {
    // 페이지 로드 시 좋아요 상태 확인
    const postId = getPostIdFromUrl();
    if (postId) {
      loadLikeStatus(postId);
    }
  });

  // URL에서 게시글 ID 추출
  function getPostIdFromUrl() {
    const pathParts = window.location.pathname.split('/');
    const detailIndex = pathParts.indexOf('detail');
    if (detailIndex !== -1 && detailIndex + 1 < pathParts.length) {
      return parseInt(pathParts[detailIndex + 1]);
    }
    return null;
  }

  // 좋아요 상태 로드
  function loadLikeStatus(postId) {
    $.ajax({
      url: '/api/post/like/status/' + postId,
      type: 'GET',
      success: function(response) {
        if (response.success) {
          updateLikeButton(response.isLiked, response.likeCount);
        }
      }
    });
  }

  // 좋아요 버튼 상태 업데이트
  function updateLikeButton(isLiked, likeCount) {
    const likeBtn = $('.like-btn');
    const likeIcon = likeBtn.find('i');
    const likeCountSpan = likeBtn.find('.reaction-count');

    if (isLiked) {
      likeBtn.addClass('liked');
      likeIcon.removeClass('bi-heart').addClass('bi-heart-fill');
      likeIcon.css('color', '#FF4D4F'); // 하트만 빨간색
      likeBtn.find('span:not(.reaction-count)').css('color', '#000'); // 좋아요 텍스트는 검정
    } else {
      likeBtn.removeClass('liked');
      likeIcon.removeClass('bi-heart-fill').addClass('bi-heart');
      likeIcon.css('color', ''); // 하트 기본색
      likeBtn.find('span:not(.reaction-count)').css('color', ''); // 텍스트 기본색
    }
    likeCountSpan.text(likeCount);
  }

  // 좋아요 토글 (메인 함수)
  function toggleLike(postId) {
    $.ajax({
      url: '/api/post/like/' + postId,
      type: 'POST',
      beforeSend: function(xhr) {
        // CSRF 토큰 설정
        const token = $('meta[name="_csrf"]').attr('content');
        const header = $('meta[name="_csrf_header"]').attr('content');
        if (token && header) {
          xhr.setRequestHeader(header, token);
        }
      },
      success: function(response) {
        if (response.success) {
          updateLikeButton(response.isLiked, response.likeCount);
        } else if (response.needLogin) {
          if (confirm('로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?')) {
            window.location.href = '/oauth2/authorization/kakao';
          }
        } else {
          alert(response.message);
        }
      },
      error: function(xhr) {
        if (xhr.status === 401) {
          if (confirm('로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?')) {
            window.location.href = '/oauth2/authorization/kakao';
          }
        } else {
          alert('좋아요 처리 중 오류가 발생했습니다.');
        }
      }
    });
  }

  // 팔로우 토글 (추후 구현)
  function toggleFollow(userId) {
    // TODO: 팔로우 기능 구현
    const followBtn = $('.follow-btn');
    const followText = $('.follow-text');
    const isFollowing = followBtn.hasClass('following');

    if (isFollowing) {
      // 언팔로우
      followBtn.removeClass('following');
      followText.text('팔로우');
      followBtn.find('i').removeClass('bi-person-check').addClass('bi-person-plus');
      alert('언팔로우했습니다.');
    } else {
      // 팔로우
      followBtn.addClass('following');
      followText.text('팔로잉');
      followBtn.find('i').removeClass('bi-person-plus').addClass('bi-person-check');
      alert('팔로우했습니다.');
    }
  }

  // 이미지 확대 모달
  function showImageModal(imgSrc) {
    const modal = $('<div class="image-modal">' +
            '<div class="modal-backdrop" onclick="closeImageModal()"></div>' +
            '<div class="modal-content">' +
            '<img src="' + imgSrc + '" alt="확대 이미지">' +
            '<button class="modal-close" onclick="closeImageModal()">×</button>' +
            '</div>' +
            '</div>');

    $('body').append(modal);
    modal.fadeIn(200);
  }

  function closeImageModal() {
    $('.image-modal').fadeOut(200, function() {
      $(this).remove();
    });
  }

  // ESC 키로 모달 닫기
  $(document).on('keydown', function(e) {
    if (e.key === 'Escape') {
      closeImageModal();
    }
  });
</script>