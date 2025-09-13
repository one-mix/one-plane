<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="/css/admin/dashboard.css"/>

<!-- 통계 카드 섹션 -->
<div class="stats-cards mb-4">
    <div class="row">
        <div class="col-lg-3 col-md-6 mb-3">
            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-card-icon users">
                        <i class="bi bi-people-fill"></i>
                    </div>
                    <h6 class="stat-card-title">전체 사용자</h6>
                </div>
                <div class="stat-card-body">
                    <div class="stat-card-value">${stats.totalUsers != null ? stats.totalUsers : 0}</div>
                    <div class="stat-card-subtitle">총 가입자 수</div>
                </div>
            </div>
        </div>

        <div class="col-lg-3 col-md-6 mb-3">
            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-card-icon active">
                        <i class="bi bi-person-check-fill"></i>
                    </div>
                    <h6 class="stat-card-title">활성 사용자</h6>
                </div>
                <div class="stat-card-body">
                    <div class="stat-card-value">${stats.activeUsers != null ? stats.activeUsers : 0}</div>
                    <div class="stat-card-subtitle">현재 활성 상태</div>
                </div>
            </div>
        </div>

        <div class="col-lg-3 col-md-6 mb-3">
            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-card-icon monthly">
                        <i class="bi bi-calendar3"></i>
                    </div>
                    <h6 class="stat-card-title">이번달 가입</h6>
                </div>
                <div class="stat-card-body">
                    <div class="stat-card-value">${stats.monthlySignups != null ? stats.monthlySignups : 0}</div>
                    <div class="stat-card-subtitle">월간 가입자</div>
                </div>
            </div>
        </div>

        <div class="col-lg-3 col-md-6 mb-3">
            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-card-icon today">
                        <i class="bi bi-person-x-fill"></i>
                    </div>
                    <h6 class="stat-card-title">비활성 사용자</h6>
                </div>
                <div class="stat-card-body">
                    <div class="stat-card-value">${stats.deletedUsers != null ? stats.deletedUsers : 0}</div>
                    <div class="stat-card-subtitle">비활성 사용자</div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="stats-cards mb-4">
    <div class="row">
        <div class="col-lg-3 col-md-6 mb-3">
            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-card-icon post">
                        <i class="bi bi-sticky-fill"></i>
                    </div>
                    <h6 class="stat-card-title">전체 게시글</h6>
                </div>
                <div class="stat-card-body">
                    <div class="stat-card-value">${stats.totalUsers != null ? stats.totalUsers : 0}</div>
                    <div class="stat-card-subtitle">총 게시글 수</div>
                </div>
            </div>
        </div>

        <div class="col-lg-3 col-md-6 mb-3">
            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-card-icon comment">
                        <i class="bi bi-chat-dots-fill"></i>
                    </div>
                    <h6 class="stat-card-title">전체 댓글</h6>
                </div>
                <div class="stat-card-body">
                    <div class="stat-card-value">${stats.activeUsers != null ? stats.activeUsers : 0}</div>
                    <div class="stat-card-subtitle">총 댓글 수</div>
                </div>
            </div>
        </div>

        <div class="col-lg-3 col-md-6 mb-3">
            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-card-icon country">
                        <i class="bi bi-globe-americas"></i>
                    </div>
                    <h6 class="stat-card-title">전체 국가</h6>
                </div>
                <div class="stat-card-body">
                    <div class="stat-card-value">${stats.monthlySignups != null ? stats.monthlySignups : 0}</div>
                    <div class="stat-card-subtitle">총 국가 수</div>
                </div>
            </div>
        </div>

        <div class="col-lg-3 col-md-6 mb-3">
            <div class="stat-card">
                <div class="stat-card-header">
                    <div class="stat-card-icon save-country">
                        <i class="bi bi-shield-shaded"></i>
                    </div>
                    <h6 class="stat-card-title">안전 국가</h6>
                </div>
                <div class="stat-card-body">
                    <div class="stat-card-value">${stats.deletedUsers != null ? stats.deletedUsers : 0}</div>
                    <div class="stat-card-subtitle">총 안전 국가</div>
                </div>
            </div>
        </div>
    </div>
</div>