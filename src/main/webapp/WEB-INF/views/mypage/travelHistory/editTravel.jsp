<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html lang="ko">
<link rel="stylesheet" href="/css/review.css" />
<head>
    <meta charset="UTF-8">
    <title>여행지 수정</title>

</head>
<body>

<div class="page-wrapper">
    <main class="content">
        <form action="${pageContext.request.contextPath}/mypage/travelHistory/edit/${travelId}" method="post" enctype="multipart/form-data" class="form-card">
            <input type="hidden" name="countryId" value="1">

            <!-- 수정 후 -->
            <div class="form-row">
                <label>국가</label>
                <input type="text"
                       name="countryCode"
                       value="${dto.countryCode}"
                       placeholder="방문한 국가를 입력해주세요."
                       required />
            </div>

            <!-- 도시 -->
            <div class="form-row">
                <label>도시</label>
                <input type="text"
                       name="city"
                        value="${dto.city}"         <!-- dto.city 값이 있어야 함 -->
                        required />
            </div>

            <!-- 날짜 -->
            <div class="form-row">
                <label>날짜</label>
                <input type="date" name="travelDate" value="${dto.travelDate}" required>
            </div>

            <!-- 제목 -->
            <div class="form-row">
                <label>제목</label>
                <input type="text" name="title" value="${dto.title}" placeholder="제목을 입력해주세요." required>
            </div>

            <!-- 평가 -->
            <div class="form-row">
                <label>평가</label>
                <div class="rating">
                    <c:forEach var="i" begin="1" end="5">
                        <input type="radio" id="star${6-i}" name="rating" value="${6-i}"
                            ${dto.rating == (6-i) ? 'checked' : ''}>
                        <label for="star${6-i}">★</label>
                    </c:forEach>
                </div>
            </div>

            <div class="form-group">
                <label>테마</label>
                <div class="option-grid">
                    <div class="option-item">
                        <input type="radio" id="theme1" name="travelPurpose" value="휴식" ${dto.travelPurpose == '휴식' ? 'checked' : ''}>
                        <label for="theme1">🌴 휴식</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="theme2" name="travelPurpose" value="관광" ${dto.travelPurpose == '관광' ? 'checked' : ''}>
                        <label for="theme2">🌇 관광</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="theme3" name="travelPurpose" value="문화" ${dto.travelPurpose == '문화' ? 'checked' : ''}>
                        <label for="theme3">🏛 문화 체험</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="theme4" name="travelPurpose" value="출장" ${dto.travelPurpose == '출장' ? 'checked' : ''}>
                        <label for="theme4">💼 출장/업무</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="theme5" name="travelPurpose" value="모험" ${dto.travelPurpose == '모험' ? 'checked' : ''}>
                        <label for="theme5">🏔️ 모험/체험</label>
                    </div>
                </div>
            </div>

            <!-- 동행 -->
            <div class="form-group">
                <label>동행</label>
                <div class="option-grid">
                    <div class="option-item">
                        <input type="radio" id="comp1" name="companion" value="가족" ${dto.companion == '가족' ? 'checked' : ''}>
                        <label for="comp1">👪 가족과 함께</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="comp2" name="companion" value="친구" ${dto.companion == '친구' ? 'checked' : ''}>
                        <label for="comp2">👫 친구와 함께</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="comp3" name="companion" value="연인" ${dto.companion == '연인' ? 'checked' : ''}>
                        <label for="comp3">💑 연인과 함께</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="comp4" name="companion" value="혼자" ${dto.companion == '혼자' ? 'checked' : ''}>
                        <label for="comp4">👤 혼자서</label>
                    </div>
                    <div class="option-item">
                        <input type="radio" id="comp5" name="companion" value="단체" ${dto.companion == '단체' ? 'checked' : ''}>
                        <label for="comp5">👥 단체로</label>
                    </div>
                </div>
            </div>

            <!-- 내용 -->
            <div class="form-row">
                <label>내용</label>
                <textarea name="content" placeholder="내용을 입력해주세요." required>${dto.content}</textarea>
            </div>

            <!-- 사진 첨부 -->
            <div class="form-row">
                <label>사진 변경 (선택)</label>
                <!-- 기존 경로 유지용 hidden -->
                <input type="hidden" name="imagePath" value="${dto.imagePath}"/>

                <div class="file-upload" onclick="document.getElementById('imgInput').click()">
                    <p>파일 선택</p>
                    <small>새 사진을 선택하지 않으면 기존 사진이 유지됩니다.</small>
                    <input type="file" id="imgInput" name="travelImg" accept=".jpg,.jpeg,.png"/>
                </div>
            </div>

            <button type="submit" class="btn-submit">수정하기</button>
        </form>
    </main>
</div>
</body>
</html>

