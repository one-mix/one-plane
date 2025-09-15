<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>방문한 여행지 추가</title>
    <link rel="stylesheet" href="/css/mypage/review.css" />
    <style>
        .option-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
            gap: 12px;
            margin-top: 8px;
        }

        .option-item {
            position: relative;
        }

        /* input 숨기고 label을 버튼처럼 */
        .option-item input[type="radio"] {
            display: none;
        }

        .option-item label {
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 10px 14px;
            border: 1px solid #ccc;
            border-radius: 8px;
            background: #fff;
            cursor: pointer;
            font-size: 14px;
            transition: all 0.2s ease;
            user-select: none;
        }

        .option-item label:hover {
            border-color: #888;
            background: #f9f9f9;
        }

        /* 체크된 상태 스타일 */
        .option-item input[type="radio"]:checked + label {
            background: #007AFF;
            border-color: #007AFF;
            color: #fff;
        }


    </style>
</head>
<body>

<main class="content">
    <h2 class="page-title">방문한 여행지</h2>
    <form action="${pageContext.request.contextPath}/mypage/travelHistory/add" method="post" enctype="multipart/form-data" class="form-card">
        <div class="form-row">
            <label>국가</label>
            <input type="text" name="country" placeholder="방문한 국가를 입력해주세요."/>
        </div>
        <div class="form-row">
            <label>날짜</label>
            <input type="date" name="travelDate"/>
        </div>
        <div class="form-row">
            <label>제목</label>
            <input type="text" name="title" placeholder="제목을 입력해주세요."/>
        </div>
        <div class="form-row">
            <label>평가</label>
            <div class="rating">
                <c:forEach var="i" begin="1" end="5">
                    <input type="radio"
                           id="star${i}"
                           name="rating"
                           value="${6 - i}"
                           <c:if test="${dto.rating == 6 - i}">checked</c:if>/>
                    <label for="star${i}">★</label>
                </c:forEach>
            </div>
        </div>
        <!-- 테마 -->
        <div class="form-group">
            <label>테마</label>
            <div class="option-grid">
                <div class="option-item">
                    <input type="radio" id="theme1" name="travelPurpose" value="휴식" ${travel.travelPurpose == '휴식' ? 'checked' : ''}>
                    <label for="theme1">🌴 휴식</label>
                </div>
                <div class="option-item">
                    <input type="radio" id="theme2" name="travelPurpose" value="관광" ${travel.travelPurpose == '관광' ? 'checked' : ''}>
                    <label for="theme2">🌇 관광</label>
                </div>
                <div class="option-item">
                    <input type="radio" id="theme3" name="travelPurpose" value="문화 체험" ${travel.travelPurpose == '문화 체험' ? 'checked' : ''}>
                    <label for="theme3">🏛 문화 체험</label>
                </div>
                <div class="option-item">
                    <input type="radio" id="theme4" name="travelPurpose" value="출장/업무" ${travel.travelPurpose == '출장/업무' ? 'checked' : ''}>
                    <label for="theme4">💼 출장/업무</label>
                </div>
                <div class="option-item">
                    <input type="radio" id="theme5" name="travelPurpose" value="모험/체험" ${travel.travelPurpose == '모험/체험' ? 'checked' : ''}>
                    <label for="theme5">� 모험/체험</label>
                </div>
            </div>
        </div>

        <!-- 동행 -->
        <div class="form-group">
            <label>동행</label>
            <div class="option-grid">
                <div class="option-item">
                    <input type="radio" id="comp1" name="companion" value="가족과 함께" ${travel.companion == '가족과 함께' ? 'checked' : ''}>
                    <label for="comp1">👪 가족과 함께</label>
                </div>
                <div class="option-item">
                    <input type="radio" id="comp2" name="companion" value="친구와 함께" ${travel.companion == '친구와 함께' ? 'checked' : ''}>
                    <label for="comp2">👫 친구와 함께</label>
                </div>
                <div class="option-item">
                    <input type="radio" id="comp3" name="companion" value="연인과 함께" ${travel.companion == '연인과 함께' ? 'checked' : ''}>
                    <label for="comp3">💑 연인과 함께</label>
                </div>
                <div class="option-item">
                    <input type="radio" id="comp4" name="companion" value="혼자서" ${travel.companion == '혼자서' ? 'checked' : ''}>
                    <label for="comp4">👤 혼자서</label>
                </div>
                <div class="option-item">
                    <input type="radio" id="comp5" name="companion" value="단체로" ${travel.companion == '단체로' ? 'checked' : ''}>
                    <label for="comp5">👥 단체로</label>
                </div>
            </div>
        </div>
        <div class="form-row">
            <label>내용</label>
            <textarea name="content" placeholder="내용을 입력해주세요."></textarea>
        </div>
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