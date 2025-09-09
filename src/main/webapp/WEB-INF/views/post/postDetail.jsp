<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- 사용자 정보를 JavaScript에 전달하기 위한 메타태그 -->
<sec:authorize access="isAuthenticated()">
  <sec:authentication property="principal" var="userDetails" />
  <meta name="user-id" content="${userDetails.userId}">
  <meta name="user-nickname" content="${userDetails.nickname}">
</sec:authorize>

<sec:authorize access="!isAuthenticated()">
  <meta name="user-id" content="">
  <meta name="user-nickname" content="">
</sec:authorize>

<!-- CSRF 토큰 메타태그 -->
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">

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
          <button type="button" class="follow-btn" onclick="toggleFollow(${post.userId})">
            <i class="bi bi-person-plus"></i>
            <span class="follow-text">팔로우</span>
          </button>
        </sec:authorize>
      </div>
    </div>
  </div>

  <!-- 게시글 내용 -->
  <div class="post-content">
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

  <!-- 댓글 섹션 -->
  <div class="post-additional">
    <div class="comments-section">
      <h3 class="section-title">댓글 <span class="comment-count-text">${post.commentCount}</span></h3>

      <!-- 댓글 작성 섹션 -->
      <sec:authorize access="isAuthenticated()">
        <div class="comment-write">
          <textarea class="comment-input" placeholder="댓글을 작성해주세요..." maxlength="100"></textarea>
          <div class="comment-write-footer">
            <span class="comment-length">0/100</span>
            <button type="button" class="comment-submit-btn">댓글 등록</button>
          </div>
        </div>
      </sec:authorize>

      <sec:authorize access="!isAuthenticated()">
        <div class="comment-login-required">
          <p>댓글을 작성하려면 <a href="/oauth2/authorization/kakao">로그인</a>이 필요합니다.</p>
        </div>
      </sec:authorize>

      <!-- 댓글 목록 -->
      <div class="comments-list" id="comments-list">
        <div class="loading" style="display: none;">
          <p>댓글을 불러오는 중...</p>
        </div>
        <div class="no-comments">
          <p>아직 댓글이 없습니다. 첫 번째 댓글을 작성해보세요!</p>
        </div>
      </div>

      <!-- 댓글 페이지네이션 -->
      <div class="comment-pagination" id="comment-pagination"></div>
    </div>
  </div>
</main>

<!-- Bootstrap Icons -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

<script>
  // 전역 변수
  let currentPostId = null;
  let currentUserId = null;
  let currentPage = 1;

  $(document).ready(function() {
    // 초기화
    initializePage();

    // 이벤트 바인딩
    bindEvents();
  });

  // 페이지 초기화
  function initializePage() {
    // 현재 게시글 ID 가져오기
    currentPostId = getPostIdFromUrl();

    // 현재 사용자 ID 가져오기
    const userIdMeta = $('meta[name="user-id"]').attr('content');
    currentUserId = userIdMeta && userIdMeta !== '' ? parseInt(userIdMeta) : null;

    // 좋아요 상태 로드
    if (currentPostId) {
      loadLikeStatus(currentPostId);
      loadComments(currentPostId, 1);
    }

    // 이미지 클릭 시 확대 보기
    $('.content-body img').on('click', function() {
      const imgSrc = $(this).attr('src');
      showImageModal(imgSrc);
    });

    // 외부 링크는 새 창에서 열기
    $('.content-body a[href^="http"]').attr('target', '_blank');
  }

  // 이벤트 바인딩
  function bindEvents() {
    // 댓글 작성 버튼
    $('.comment-submit-btn').on('click', function() {
      submitComment();
    });

    // 댓글 입력창 이벤트
    $('.comment-input').on('input', function() {
      updateCommentLength();
    }).on('keydown', function(e) {
      if (e.ctrlKey && e.key === 'Enter') {
        submitComment();
      }
    });

    // ESC 키로 모달 닫기
    $(document).on('keydown', function(e) {
      if (e.key === 'Escape') {
        closeImageModal();
      }
    });
  }

  // URL에서 게시글 ID 추출
  function getPostIdFromUrl() {
    const pathParts = window.location.pathname.split('/');
    const detailIndex = pathParts.indexOf('detail');
    if (detailIndex !== -1 && detailIndex + 1 < pathParts.length) {
      return parseInt(pathParts[detailIndex + 1]);
    }
    return null;
  }

  // 댓글 글자수 업데이트
  function updateCommentLength() {
    const content = $('.comment-input').val();
    const length = content.length;
    $('.comment-length').text(length + '/100');

    // 100자 넘으면 빨간색
    if (length > 100) {
      $('.comment-length').css('color', '#ff4d4f');
    } else {
      $('.comment-length').css('color', '#666');
    }
  }

  // 댓글 목록 로드
  function loadComments(postId, page) {
    if (!postId) return;

    currentPage = page || 1;

    // 로딩 표시
    showLoading();

    $.ajax({
      url: '/comment/list/' + postId,
      type: 'GET',
      data: {
        page: currentPage,
        size: 10
      },
      success: function(response) {
        hideLoading();

        if (response.success) {
          displayComments(response.comments);
          updateCommentCount(response.totalCount);
          displayPagination(postId, response.currentPage, response.totalPages);
        } else {
          showError('댓글을 불러오는 중 오류가 발생했습니다.');
        }
      },
      error: function(xhr) {
        hideLoading();
        console.error('댓글 로드 실패:', xhr);
        showError('댓글을 불러오는 중 오류가 발생했습니다.');
      }
    });
  }

  // 댓글 목록 표시
  function displayComments(comments) {
    const commentsList = $('#comments-list');

    if (!comments || comments.length === 0) {
      commentsList.html(`
        <div class="no-comments">
          <p>아직 댓글이 없습니다. 첫 번째 댓글을 작성해보세요!</p>
        </div>
      `);
      return;
    }

    let html = '';
    comments.forEach(function(comment) {
      html += createCommentHtml(comment);
    });

    commentsList.html(html);
  }

  function createCommentHtml(comment) {
    const isDeleted = comment.deletedAt !== null;
    const isAuthor = currentUserId && currentUserId === comment.userId;
    const formattedDate = formatDate(new Date(comment.createdAt));

    // 프로필 이미지 결정 로직 개선
    let profileImg = '/images/profile.png'; // 기본값

    // 1순위: comment.user.profileImg (Association 매핑된 사용자 정보)
    if (comment.user && comment.user.profileImg) {
      profileImg = comment.user.profileImg;
    }
    // 2순위: comment.profileImg (직접 매핑된 프로필 이미지)
    else if (comment.profileImg) {
      profileImg = comment.profileImg;
    }

    // 닉네임 결정 로직 개선
    let nickname = '익명';
    if (comment.user && comment.user.nickname) {
      nickname = comment.user.nickname;
    } else if (comment.nickname) {
      nickname = comment.nickname;
    } else if (comment.authorName) {
      nickname = comment.authorName;
    }

    // 삭제 버튼 HTML을 별도로 생성
    let deleteButtonHtml = '';
    if (isAuthor && !isDeleted) {
      deleteButtonHtml = '<button type="button" class="comment-delete-btn" onclick="deleteComment(' + comment.commentId + ')">' +
              '<i class="bi bi-trash"></i>' +
              '</button>';
    }

    return '<div class="comment-item" data-comment-id="' + comment.commentId + '">' +
            '  <div class="comment-header">' +
            '    <div class="comment-author">' +
            '      <img src="' + profileImg + '" alt="' + escapeHtml(nickname) + '의 프로필" class="comment-avatar" ' +
            '           onerror="this.src=\'/images/profile.png\'" ' +
            '           onload="this.style.opacity=1" style="opacity:0; transition: opacity 0.3s ease;">' +
            '      <span class="comment-nickname">' + escapeHtml(nickname) + '</span>' +
            '      <span class="comment-date">' + formattedDate + '</span>' +
            '    </div>' +
            '    <div class="comment-actions">' +
            '      ' + deleteButtonHtml +
            '    </div>' +
            '  </div>' +
            '  <div class="comment-content">' +
            (isDeleted ?
                            '<span class="deleted-comment">삭제된 댓글입니다.</span>' :
                            escapeHtml(comment.content)
            ) +
            '  </div>' +
            '</div>';
  }

  // 프로필 이미지 로드 실패 시 기본 이미지로 대체하는 함수
  function handleProfileImageError(img) {
    img.src = '/images/profile.png';
    img.onerror = null; // 무한 루프 방지
  }

  // HTML 이스케이프
  function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
  }

  // 댓글 수 업데이트
  function updateCommentCount(count) {
    $('.comment-count-text').text(count);
    $('.comment-count').text('댓글 ' + count);
  }

  // 페이지네이션 표시
  function displayPagination(postId, currentPage, totalPages) {
    const pagination = $('#comment-pagination');

    if (totalPages <= 1) {
      pagination.empty();
      return;
    }

    let html = '';

    // 이전 페이지
    if (currentPage > 1) {
      html += '<button class="page-btn" onclick="loadComments(' + postId + ', ' + (currentPage - 1) + ')">이전</button>';
    }

    // 페이지 번호
    const startPage = Math.max(1, currentPage - 2);
    const endPage = Math.min(totalPages, currentPage + 2);

    for (let i = startPage; i <= endPage; i++) {
      const activeClass = i === currentPage ? 'active' : '';
      html += '<button class="page-btn ' + activeClass + '" onclick="loadComments(' + postId + ', ' + i + ')">' + i + '</button>';
    }

    // 다음 페이지
    if (currentPage < totalPages) {
      html += '<button class="page-btn" onclick="loadComments(' + postId + ', ' + (currentPage + 1) + ')">다음</button>';
    }

    pagination.html(html);
  }

  // 댓글 작성
  function submitComment() {
    if (!currentUserId) {
      if (confirm('로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?')) {
        window.location.href = '/oauth2/authorization/kakao';
      }
      return;
    }

    const content = $('.comment-input').val().trim();

    if (!content) {
      alert('댓글 내용을 입력해주세요.');
      $('.comment-input').focus();
      return;
    }

    if (content.length > 100) {
      alert('댓글은 100자 이내로 입력해주세요.');
      $('.comment-input').focus();
      return;
    }

    // 버튼 비활성화
    $('.comment-submit-btn').prop('disabled', true).text('등록 중...');

    $.ajax({
      url: '/comment/write',
      type: 'POST',
      data: {
        postId: currentPostId,
        content: content
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
          $('.comment-input').val('');
          updateCommentLength();
          loadComments(currentPostId, 1); // 첫 페이지로 이동
          showSuccess('댓글이 작성되었습니다.');
        } else {
          showError(response.message || '댓글 작성에 실패했습니다.');
        }
      },
      error: function(xhr) {
        if (xhr.status === 401) {
          if (confirm('로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?')) {
            window.location.href = '/oauth2/authorization/kakao';
          }
        } else {
          showError('댓글 작성 중 오류가 발생했습니다.');
        }
      },
      complete: function() {
        // 버튼 활성화
        $('.comment-submit-btn').prop('disabled', false).text('댓글 등록');
      }
    });
  }

  // 댓글 삭제
  function deleteComment(commentId) {
    if (!confirm('댓글을 삭제하시겠습니까?')) {
      return;
    }

    $.ajax({
      url: '/comment/' + commentId,
      type: 'DELETE',
      beforeSend: function(xhr) {
        const token = $('meta[name="_csrf"]').attr('content');
        const header = $('meta[name="_csrf_header"]').attr('content');
        if (token && header) {
          xhr.setRequestHeader(header, token);
        }
      },
      success: function(response) {
        if (response.success) {
          loadComments(currentPostId, currentPage);
          showSuccess('댓글이 삭제되었습니다.');
        } else {
          showError(response.message || '댓글 삭제에 실패했습니다.');
        }
      },
      error: function(xhr) {
        showError('댓글 삭제 중 오류가 발생했습니다.');
      }
    });
  }

  // 날짜 포맷팅
  function formatDate(date) {
    const now = new Date();
    const diff = now - date;
    const minutes = Math.floor(diff / (1000 * 60));
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const days = Math.floor(diff / (1000 * 60 * 60 * 24));

    if (minutes < 1) {
      return '방금 전';
    } else if (minutes < 60) {
      return minutes + '분 전';
    } else if (hours < 24) {
      return hours + '시간 전';
    } else if (days < 7) {
      return days + '일 전';
    } else {
      return date.getFullYear() + '-' +
              String(date.getMonth() + 1).padStart(2, '0') + '-' +
              String(date.getDate()).padStart(2, '0') + ' ' +
              String(date.getHours()).padStart(2, '0') + ':' +
              String(date.getMinutes()).padStart(2, '0');
    }
  }

  // 로딩 표시/숨김
  function showLoading() {
    $('#comments-list .loading').show();
    $('#comments-list .no-comments').hide();
  }

  function hideLoading() {
    $('#comments-list .loading').hide();
  }

  // 메시지 표시
  function showSuccess(message) {
    alert(message);
  }

  function showError(message) {
    alert(message);
  }

  // =========================
  // 기존 기능들 (좋아요, 팔로우 등)
  // =========================

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
      likeIcon.css('color', '#FF4D4F');
      likeBtn.find('span:not(.reaction-count)').css('color', '#000');
    } else {
      likeBtn.removeClass('liked');
      likeIcon.removeClass('bi-heart-fill').addClass('bi-heart');
      likeIcon.css('color', '');
      likeBtn.find('span:not(.reaction-count)').css('color', '');
    }
    likeCountSpan.text(likeCount);
  }

  // 좋아요 토글
  function toggleLike(postId) {
    $.ajax({
      url: '/api/post/like/' + postId,
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
    const followBtn = $('.follow-btn');
    const followText = $('.follow-text');
    const isFollowing = followBtn.hasClass('following');

    if (isFollowing) {
      followBtn.removeClass('following');
      followText.text('팔로우');
      followBtn.find('i').removeClass('bi-person-check').addClass('bi-person-plus');
      alert('언팔로우했습니다.');
    } else {
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
</script>