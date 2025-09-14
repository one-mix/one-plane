<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>방문한 여행지 추가</title>
    <link rel="stylesheet" href="/css/review.css" />
</head>
<body>
<!-- 콘텐츠 래퍼 -->
<div class="page-wrapper">
    <main class="content">
        <h2 class="page-title">방문한 여행지</h2>
        <form action="${pageContext.request.contextPath}/mypage/travelHistory/add"
              method="post"
              enctype="multipart/form-data"
              class="form-card">

            <!-- 국가명 (DTO의 countryCode) -->
            <div class="form-row">
                <label>국가</label>
                <input type="text" name="countryCode" placeholder="방문한 국가를 입력해주세요."/>
            </div>

            <!-- 도시 (DTO의 city) -->
            <div class="form-row">
                <label>도시</label>
                <input type="text" name="city" placeholder="방문한 도시를 입력해주세요."/>
            </div>

            <!-- 날짜 -->
            <div class="form-row">
                <label>날짜</label>
                <input type="date" name="travelDate"/>
            </div>

            <!-- 제목 -->
            <div class="form-row">
                <label>제목</label>
                <input type="text" name="title" placeholder="제목을 입력해주세요."/>
            </div>

            <!-- 평가 -->
            <div class="form-row">
                <label>평가</label>
                <div class="rating">
                    <c:forEach var="i" begin="1" end="5">
                        <input type="radio"
                               id="star${i}"
                               name="rating"
                               value="${6 - i}"/>
                        <label for="star${i}">★</label>
                    </c:forEach>
                </div>
            </div>

            <!-- 테마 -->
            <div class="form-group">
                <label>테마</label>
                <div class="option-grid">
                    <div class="option-item">
                        <input type="radio" id="theme1" name="travelPurpose" value="휴식"/>
                        <label for="theme1">🌴 휴식</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="theme2" name="travelPurpose" value="관광"/>
                        <label for="theme2">🌇 관광</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="theme3" name="travelPurpose" value="문화"/>
                        <label for="theme3">🏛 문화 체험</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="theme4" name="travelPurpose" value="출장"/>
                        <label for="theme4">💼 출장/업무</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="theme5" name="travelPurpose" value="모험"/>
                        <label for="theme5">🎯 모험/체험</label>
                    </div>
                </div>
            </div>

            <!-- 동행 -->
            <div class="form-group">
                <label>동행</label>
                <div class="option-grid">
                    <div class="option-item">
                        <input type="radio" id="comp1" name="companion" value="가족"/>
                        <label for="comp1">👪 가족과 함께</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="comp2" name="companion" value="친구"/>
                        <label for="comp2">👫 친구와 함께</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="comp3" name="companion" value="연인"/>
                        <label for="comp3">💑 연인과 함께</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="comp4" name="companion" value="혼자"/>
                        <label for="comp4">👤 혼자서</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="comp5" name="companion" value="단체"/>
                        <label for="comp5">👥 단체로</label>
                    </div>
                </div>
            </div>

            <!-- 내용 -->
            <div class="form-row">
                <label>내용</label>
                <textarea name="content" placeholder="내용을 입력해주세요."></textarea>
            </div>

            <!-- 사진 첨부 -->
            <div class="form-row">
                <label>사진 첨부</label>
                <div class="file-upload" onclick="document.getElementById('imgInput').click()">
                    <p>파일 선택</p>
                    <small>JPG, PNG, JPEG 사진만 최대 1장 업로드 가능합니다.</small>
                    <input type="file" id="imgInput" name="travelImg" accept=".jpg,.jpeg,.png"/>
                </div>
            </div>

            <button type="submit" class="btn-submit">추가하기</button>
        </form>
    </main>
</div>
</body>
</html>