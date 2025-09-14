<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<link rel="stylesheet" href="/css/admin/postDetail.css"/>

<div class="post-detail-container">
  <!-- 게시글 헤더 -->
  <div class="post-header">
    <div class="post-category">
      <c:choose>
        <c:when test="${post.category.name() == 'READY'}">
          <span class="category-badge category-ready">여행 준비</span>
        </c:when>
        <c:when test="${post.category.name() == 'REVIEW'}">
          <span class="category-badge category-review">여행 후기</span>
        </c:when>
        <c:when test="${post.category.name() == 'ACCOMPANY'}">
          <span class="category-badge category-accompany">동행 구하기</span>
        </c:when>
        <c:when test="${post.category.name() == 'FREE'}">
          <span class="category-badge category-free">자유게시판</span>
        </c:when>
      </c:choose>

      <c:if test="${not empty post.country}">
        <span class="country-tag">${post.country}</span>
      </c:if>
    </div>
    <div class="post-title-bar">
      <h1 class="post-title">${post.title}</h1>
      <form method="post"
            action="/admin/post/${post.postId}/delete"
            class="post-management"
            onsubmit="return confirm('정말로 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.');">
        <button type="submit" class="btn btn-outline-danger">게시물 삭제</button>
      </form>
    </div>

    <!-- 작성자 정보 -->
    <div class="author-info">
      <div class="author-profile">
        <img src="${not empty post.user.profileImg ? post.user.profileImg : '/images/profile.png'}"
             alt="프로필" class="author-avatar">
        <div class="author-details">
          <span class="author-name">${post.authorName}</span>
          <time class="post-date">
            <div><fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd" /></div>
          </time>
        </div>
      </div>

      <!-- 게시글 통계 -->
      <div class="post-stats">
        <span class="stat-item">
          <i class="bi bi-eye"></i>
          <span class="stat-number">${post.viewCount}</span>
        </span>
        <span class="stat-item">
          <i class="bi bi-heart"></i>
          <span class="stat-number">${post.likeCount}</span>
        </span>
        <span class="stat-item">
          <i class="bi bi-chat"></i>
          <span class="stat-number">${post.commentCount}</span>
        </span>
      </div>
    </div>
  </div>

  <!-- 게시글 내용 -->
  <div class="post-content">
    <c:if test="${not empty post.thumbnailImage}">
      <div class="post-thumbnail">
        <img src="${post.thumbnailImage}" alt="대표 이미지" class="thumbnail-image">
      </div>
    </c:if>

    <div class="post-body">
      ${post.content}
    </div>
  </div>
</div>

<script>
  let currentPage = 1;
  let isLiked = false;

  $(document).ready(function() {

    // 댓글 목록 로드
    loadComments(1);
  });
  /**
   * 게시글 삭제
   */
  function deletePost(postId) {
    if (!confirm('게시글을 삭제하시겠습니까?')) return;

    $.post('/post/delete/' + postId)
            .done(function(response) {
              if (response.success) {
                alert('게시글이 삭제되었습니다.');
                window.location.href = '/post/list';
              } else {
                alert(response.message || '게시글 삭제에 실패했습니다.');
              }
            });
  }

  /**
   * 게시글 공유
   */
  function sharePost() {
    if (navigator.share) {
      navigator.share({
        title: '${post.title}',
        url: window.location.href
      });
    } else {
      // 클립보드에 URL 복사
      navigator.clipboard.writeText(window.location.href).then(function() {
        alert('링크가 클립보드에 복사되었습니다.');
      });
    }
  }


</script>