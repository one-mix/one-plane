<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>OnePlane Admin</title>

  <!-- Pretendard Font -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css">

  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

  <!-- Bootstrap Icons -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

  <!-- Admin CSS -->
  <link rel="stylesheet" href="/css/variables.css" />
  <link rel="stylesheet" href="/css/admin/admin.css" />
</head>
<body>
  <!-- Admin Header -->
  <jsp:include page="adminHeader.jsp" />

  <div class="admin-container">
    <!-- Admin Sidebar -->
    <jsp:include page="adminSidebar.jsp" />

    <!-- Main Content -->
      <main class="admin-main">
          <c:import url="/WEB-INF/views/admin/${contentPage}" />
      </main>
  </div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

<!-- Admin JS -->
<script src="/js/admin/admin.js"></script>
</body>
</html>