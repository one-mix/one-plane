<!-- 작성자: 김동현 -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<aside class="admin-sidebar">
  <nav class="sidebar-nav">
    <!-- 사용자 관리 -->
    <div class="nav-section">
      <div class="nav-header">
        <i class="bi bi-people"></i>
        <span>사용자</span>
        <i class="bi bi-chevron-right expand-icon"></i>
      </div>
      <div class="nav-submenu">
        <a href="/admin/userList" class="${activeMenu eq 'userList' ? 'active' : ''}">
          전체 사용자
        </a>
        <a href="/admin/userDeleted" class="${activeMenu eq 'user-del' ? 'active' : ''}">
          탈퇴 사용자
        </a>
      </div>
    </div>

    <div class="nav-section">
      <div class="nav-header">
        <i class="bi bi-file-text"></i>
        <span>커뮤니티</span>
        <i class="bi bi-chevron-right expand-icon"></i>
      </div>
      <div class="nav-submenu">
        <a href="/admin/posts" class="${activeMenu eq 'posts' ? 'active' : ''}">
          전체 글
        </a>
        <a href="/admin/posts/popularity" class="${activeMenu eq 'post-popularity' ? 'active' : ''}">
          인기 글
        </a>
        <a href="/admin/comments" class="${activeMenu eq 'comments' ? 'active' : ''}">
          전체 댓글
        </a>
      </div>
    </div>

    <!-- 국가 관리 -->
    <div class="nav-section">
      <div class="nav-header">
        <i class="bi bi-globe"></i>
        <span>국가</span>
        <i class="bi bi-chevron-right expand-icon"></i>
      </div>
      <div class="nav-submenu">
        <a href="/admin/countries" class="${activeMenu eq 'countries' ? 'active' : ''}">
          전체국가
        </a>
        <a href="/admin/countries/risk" class="${activeMenu eq 'country-risk' ? 'active' : ''}">
          위험도 국가
        </a>
      </div>
    </div>

    <!-- 통계 -->
    <div class="nav-section">
      <div class="nav-header">
        <i class="bi bi-bar-chart"></i>
        <span>통계</span>
        <i class="bi bi-chevron-right expand-icon"></i>
      </div>
      <div class="nav-submenu">
        <a href="/admin/dashboard/user" class="${activeMenu eq 'user' ? 'active' : ''}">
          사용자 통계
        </a>
        <a href="/admin/dashboard/post" class="${activeMenu eq 'stats-post' ? 'active' : ''}">
          게시판 통계
        </a>
        <a href="/admin/dashboard/country" class="${activeMenu eq 'stats-country' ? 'active' : ''}">
          여행 통계
        </a>
      </div>
    </div>
  </nav>
</aside>