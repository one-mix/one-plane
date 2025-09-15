<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="/css/admin/userDetail.css"/>

<!-- Page Header -->
<div class="page-header">
  <div class="d-flex justify-content-between align-items-center">
    <div>
    </div>
    <div class="page-actions">
      <c:if test="${not user.admin}">
        <form method="post" action="/admin/users/${user.user_id}/delete" class="d-inline"
              onsubmit="return confirm('정말로 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.');">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
          <button type="submit" class="btn btn-outline-danger">
            <i class="bi bi-trash"></i> 계정 삭제
          </button>
        </form>
      </c:if>
    </div>
  </div>
</div>

<c:choose>
  <c:when test="${not empty user}">
    <div class="row">
      <!-- 사용자 정보 카드 -->
    <div class="col-lg-7">
      <div class="col-12">
        <div class="user-info-card">
          <div class="card-header">
            <h5 class="card-title">
              <i class="bi bi-person-vcard"></i> 회원 정보
            </h5>
          </div>
          <div class="card-body">
            <div class="user-info-form">
              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">이름</label>
                  <div class="form-value">
                    <c:choose>
                      <c:when test="${not empty user.name}">
                        ${user.name}
                      </c:when>
                      <c:otherwise>
                        <span class="text-muted">미설정</span>
                      </c:otherwise>
                    </c:choose>
                  </div>
                </div>
                <div class="form-group">
                  <label class="form-label">닉네임</label>
                  <div class="form-value">
                    <c:choose>
                      <c:when test="${not empty user.nickname}">
                        ${user.nickname}
                      </c:when>
                      <c:otherwise>
                        <span class="text-muted">미설정</span>
                      </c:otherwise>
                    </c:choose>
                  </div>
                </div>
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">나이</label>
                  <div class="form-value">
                    <c:choose>
                      <c:when test="${not empty user.age}">
                        ${user.age}
                      </c:when>
                      <c:otherwise>
                        <span class="text-muted">미설정</span>
                      </c:otherwise>
                    </c:choose>
                  </div>
                </div>
                <div class="form-group">
                  <label class="form-label">등급</label>
                  <div class="form-value">
                            <span class="badge grade-badge" data-grade="${user.grade}">
                                ${user.gradeKorean}
                            </span>
                  </div>
                </div>
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">성별</label>
                  <div class="form-value">
                    <c:choose>
                      <c:when test="${not empty user.gender}">
                        ${user.genderKorean}
                      </c:when>
                      <c:otherwise>
                        <span class="text-muted">미설정</span>
                      </c:otherwise>
                    </c:choose>
                  </div>
                </div>
                <div class="form-group">
                  <label class="form-label">이메일</label>
                  <div class="form-value">
                      ${user.email}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
      <!-- 프로필 및 건강정보 카드 -->
      <div class="col-lg-4">

        <!-- 건강 정보 -->
        <div class="user-detail-card">
          <div class="card-header">
            <h5 class="card-title">
              <i class="bi bi-heart-pulse"></i> 건강 정보
            </h5>
          </div>
          <div class="card-body">
            <div class="health-info-grid">
              <div class="health-item">
                <label class="health-label">질병 유무</label>
                <div class="health-value">
                  <c:choose>
                    <c:when test="${user.diseaseAsBoolean}">
                                            <span class="badge bg-warning">
                                                <i class="bi bi-exclamation-triangle"></i> 있음
                                            </span>
                    </c:when>
                    <c:otherwise>
                                            <span class="badge bg-success">
                                                <i class="bi bi-check-circle"></i> 없음
                                            </span>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>

              <div class="health-item">
                <label class="health-label">장애 유무</label>
                <div class="health-value">
                  <c:choose>
                    <c:when test="${user.disabilityAsBoolean}">
                                            <span class="badge bg-warning">
                                                <i class="bi bi-exclamation-triangle"></i> 있음
                                            </span>
                    </c:when>
                    <c:otherwise>
                                            <span class="badge bg-success">
                                                <i class="bi bi-check-circle"></i> 없음
                                            </span>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>

              <div class="health-item">
                <label class="health-label">복용약물</label>
                <div class="health-value">
                  <c:choose>
                    <c:when test="${user.medicationAsBoolean}">
                                            <span class="badge bg-warning">
                                                <i class="bi bi-exclamation-triangle"></i> 있음
                                            </span>
                    </c:when>
                    <c:otherwise>
                                            <span class="badge bg-success">
                                                <i class="bi bi-check-circle"></i> 없음
                                            </span>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>
            </div>

            <!-- 여행 주의 알림 -->
            <c:if test="${user.needsTravelCaution()}">
              <div class="alert alert-warning mt-3">
                <i class="bi bi-exclamation-triangle-fill"></i>
                <strong>여행 주의 대상</strong><br>
                <small>건강 정보 또는 연령으로 인해 여행 시 주의가 필요한 사용자입니다.</small>
              </div>
            </c:if>
          </div>
        </div>
      </div>
    </div>
  </c:when>
  <c:otherwise>
    <!-- 사용자 정보 없음 -->
    <div class="error-state">
      <div class="error-icon">
        <i class="bi bi-person-x"></i>
      </div>
      <h3>사용자를 찾을 수 없습니다</h3>
      <p>요청하신 사용자 정보를 찾을 수 없습니다.</p>
      <button class="btn btn-primary" onclick="history.back()">
        <i class="bi bi-arrow-left"></i> 이전 페이지로
      </button>
    </div>
  </c:otherwise>
</c:choose>

<script>

</script>