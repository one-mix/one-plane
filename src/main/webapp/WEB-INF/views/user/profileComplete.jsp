<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>OnePlane - 프로필 완성</title>
  <!-- Pretendard 폰트 추가 -->
  <link rel="stylesheet" as="style" crossorigin href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.9/dist/web/variable/pretendardvariable.css" />
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Bootstrap Icons -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

  <style>
    body {
      background: #404040;
      min-height: 100vh;
      font-family: "Pretendard Variable", Pretendard, -apple-system, BlinkMacSystemFont, system-ui, sans-serif;
    }

    .profile-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      padding: 20px;
    }

    .profile-card {
      background: white;
      border-radius: 20px;
      box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
      width: 100%;
      max-width: 500px;
      padding: 40px;
      position: relative;
    }

    .profile-header {
      text-align: center;
      margin-bottom: 30px;
    }

    .logo {
      font-size: 1.8rem;
      font-weight: 700;
      color: #215BAF;
      margin-bottom: 10px;
      font-family: "Pretendard Variable", Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo", "Noto Sans KR", "Malgun Gothic", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", sans-serif;
    }

    .welcome-text {
      color: #6c757d;
      margin-bottom: 20px;
    }

    .user-info {
      background: #f8f9fa;
      padding: 10px;
      border-radius: 10px;
      margin-bottom: 20px;
    }

    .form-label {
      font-weight: 500;
      color: #555;
      margin-bottom: 8px;
    }

    .form-control {
      border-radius: 10px;
      border: 1px solid #ddd;
      padding: 12px 15px;
      transition: border-color 0.3s, box-shadow 0.3s;
    }

    .nickname-check .form-control {
      flex: 1;
    }

    .form-control:focus {
      border-color: #667eea;
      box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
    }

    .radio-group {
      display: flex;
      gap: 20px;
      margin-top: 8px;
      flex-wrap: wrap;
    }

    .radio-item {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .form-check-input:checked {
      background-color: #667eea;
      border-color: #667eea;
    }

    .btn-complete {
      width: 100%;
      padding: 12px;
      border-radius: 10px;
      font-weight: 600;
      font-size: 1.1rem;
      background: #245BAB;
      border: none;
      color: white;
      margin-top: 20px;
      transition: transform 0.2s, box-shadow 0.2s;
    }

    .btn-complete:hover {
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
      color: white;  /* 기존 white에서 #5a67d8로 변경 */
      background: #5a67d8;
    }

    .btn-complete:active {
      color: white;
      background: #5a67d8;
      transform: translateY(0px);  /* 클릭 시 눌린 효과 */
      box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
    }
    .btn-complete:focus {
      color: white;
      background: #5a67d8;
      outline: none;
      box-shadow: 0 0 0 0.2rem rgba(90, 103, 216, 0.25);
    }


    .btn-complete:disabled {
      background: #6c757d;
      transform: none;
      box-shadow: none;
    }

    .form-group {
      margin-bottom: 20px;
    }

    .invalid-feedback {
      display: block;
      color: #dc3545;
      font-size: 0.875rem;
      margin-top: 5px;
    }

    .form-control.is-invalid {
      border-color: #dc3545;
    }

    .loading-spinner {
      display: none;
      width: 20px;
      height: 20px;
      border: 2px solid #ffffff;
      border-top: 2px solid transparent;
      border-radius: 50%;
      animation: spin 1s linear infinite;
      margin-right: 10px;
    }

    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }

    .nickname-check {
      display: flex;
      gap: 8px;
      align-items: stretch;
    }

    .nickname-check-btn {
      background: #215CAA;
      color: white;
      border: none;
      border-radius: 10px;
      padding: 8px 16px;
      font-size: 0.9rem;
      font-weight: 500;
      white-space: nowrap;
      transition: all 0.3s ease;
      min-width: 80px;
    }

    .nickname-check-btn:hover {
      background: #5a67d8;
    }

    .section-title {
      font-weight: 600;
      color: #333;
      margin-bottom: 15px;
      padding-bottom: 8px;
      border-bottom: 2px solid #f8f9fa;
    }

    .health-info-description {
      font-size: 0.9rem;
      color: #6c757d;
      margin-bottom: 15px;
      padding: 13px;
      background: #e3f2fd;
      border-radius: 8px;
      border-left: 4px solid #2196f3;
    }
  </style>
</head>
<body>
<div class="profile-container">
  <div class="profile-card">
    <div class="profile-header">
      <div class="logo"><img src="/images/logo.png">OnePlane</div>
    </div>

    <form id="profileCompleteForm" action="/user/profile/complete" method="post">

      <!-- 이름 입력 -->
      <div class="form-group">
        <label for="name" class="form-label">이름 <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="name" name="name"
               placeholder="실명을 입력해주세요" required>
        <div class="invalid-feedback" id="nameError"></div>
      </div>

      <!-- 닉네임 입력 -->
      <div class="form-group">
        <label for="nickname" class="form-label">닉네임 <span class="text-danger">*</span></label>
        <div class="nickname-check">
          <input type="text" class="form-control" id="nickname" name="nickname"
                 placeholder="사용하실 닉네임을 입력해주세요" required>
          <button type="button" class="nickname-check-btn" id="checkNicknameBtn">중복확인</button>
        </div>
        <div class="invalid-feedback" id="nicknameError"></div>
      </div>

      <!-- 나이 입력 -->
      <div class="form-group">
        <label for="age" class="form-label">나이 <span class="text-danger">*</span></label>
        <input type="number" class="form-control" id="age" name="age"
               placeholder="나이를 입력해주세요" min="1" max="120" required>
        <div class="invalid-feedback" id="ageError"></div>
      </div>

      <!-- 성별 선택 -->
      <div class="form-group">
        <label class="form-label">성별 <span class="text-danger">*</span></label>
        <div class="radio-group">
          <div class="radio-item">
            <input class="form-check-input" type="radio" name="gender"
                   id="genderMale" value="male" required>
            <label class="form-check-label" for="genderMale">남성</label>
          </div>
          <div class="radio-item">
            <input class="form-check-input" type="radio" name="gender"
                   id="genderFemale" value="female" required>
            <label class="form-check-label" for="genderFemale">여성</label>
          </div>
        </div>
        <div class="invalid-feedback" id="genderError"></div>
      </div>

      <!-- 건강 정보 섹션 -->
      <div class="section-title mt-4">
        <i class="bi bi-heart-pulse-fill"></i> 건강 정보
      </div>

      <div class="health-info-description">
        <i class="bi bi-info-circle"></i>
        건강 정보는 여행 시 맞춤형 안전 가이드 제공을 위해 수집됩니다. <br>
        개인정보는 안전하게 보호되며, 원하지 않으시면 '없음'을 선택해주세요.
      </div>

      <!-- 질병 여부 -->
      <div class="form-group">
        <label class="form-label">만성질환 또는 주요 병력</label>
        <div class="radio-group">
          <div class="radio-item">
            <input class="form-check-input" type="radio" name="disease"
                   id="diseaseYes" value="true" required>
            <label class="form-check-label" for="diseaseYes">있음</label>
          </div>
          <div class="radio-item">
            <input class="form-check-input" type="radio" name="disease"
                   id="diseaseNo" value="false" required>
            <label class="form-check-label" for="diseaseNo">없음</label>
          </div>
        </div>
        <div class="invalid-feedback" id="diseaseError"></div>
      </div>

      <!-- 장애 여부 -->
      <div class="form-group">
        <label class="form-label">신체적 제약사항 또는 장애</label>
        <div class="radio-group">
          <div class="radio-item">
            <input class="form-check-input" type="radio" name="disability"
                   id="disabilityYes" value="true" required>
            <label class="form-check-label" for="disabilityYes">있음</label>
          </div>
          <div class="radio-item">
            <input class="form-check-input" type="radio" name="disability"
                   id="disabilityNo" value="false" required>
            <label class="form-check-label" for="disabilityNo">없음</label>
          </div>
        </div>
        <div class="invalid-feedback" id="disabilityError"></div>
      </div>

      <!-- 복용약물 여부 -->
      <div class="form-group">
        <label class="form-label">정기 복용 약물</label>
        <div class="radio-group">
          <div class="radio-item">
            <input class="form-check-input" type="radio" name="medication"
                   id="medicationYes" value="true" required>
            <label class="form-check-label" for="medicationYes">있음</label>
          </div>
          <div class="radio-item">
            <input class="form-check-input" type="radio" name="medication"
                   id="medicationNo" value="false" required>
            <label class="form-check-label" for="medicationNo">없음</label>
          </div>
        </div>
        <div class="invalid-feedback" id="medicationError"></div>
      </div>

      <!-- 완료 버튼 -->
      <button type="submit" class="btn btn-complete" id="completeBtn">
        <div class="loading-spinner" id="loadingSpinner"></div>
        프로필 완성하기
      </button>
    </form>
  </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

<script>
  $(document).ready(function() {
    let nicknameChecked = false;
    let nicknameValid = false;

    // 닉네임 중복 확인
    $('#checkNicknameBtn').on('click', function() {
      const nickname = $('#nickname').val().trim();

      if (!nickname) {
        showError($('#nickname'), $('#nicknameError'), '닉네임을 입력해주세요.');
        return;
      }

      if (nickname.length < 2 || nickname.length > 10) {
        showError($('#nickname'), $('#nicknameError'), '닉네임은 2~10글자로 입력해주세요.');
        return;
      }

      $.ajax({
        url: '/user/check-nickname',
        type: 'GET',
        data: { nickname: nickname },
        success: function(response) {
          if (response.available) {
            showSuccess($('#nickname'), $('#nicknameError'), '사용 가능한 닉네임입니다.');
            nicknameChecked = true;
            nicknameValid = true;
          } else {
            showError($('#nickname'), $('#nicknameError'), response.message);
            nicknameChecked = true;
            nicknameValid = false;
          }
        },
        error: function() {
          showError($('#nickname'), $('#nicknameError'), '닉네임 확인 중 오류가 발생했습니다.');
          nicknameChecked = false;
          nicknameValid = false;
        }
      });
    });

    // 닉네임 입력 시 중복확인 상태 초기화
    $('#nickname').on('input', function() {
      nicknameChecked = false;
      nicknameValid = false;
      $(this).removeClass('is-invalid is-valid');
      $('#nicknameError').text('');
    });

    // 폼 제출 처리
    $('#profileCompleteForm').on('submit', function(e) {
      e.preventDefault();

      // 유효성 검사
      if (!validateForm()) {
        return;
      }

      // 닉네임 중복확인 체크
      if (!nicknameChecked) {
        showError($('#nickname'), $('#nicknameError'), '닉네임 중복확인을 해주세요.');
        return;
      }

      if (!nicknameValid) {
        showError($('#nickname'), $('#nicknameError'), '사용할 수 없는 닉네임입니다.');
        return;
      }

      // 로딩 시작
      showLoading(true);

      // AJAX 요청
      $.ajax({
        url: $(this).attr('action'),
        type: 'POST',
        data: $(this).serialize(),
        success: function(response) {
          if (response.success) {
            alert('프로필이 완성되었습니다! OnePlane에 오신 것을 환영합니다.');
            window.location.href = response.redirectUrl || '/';
          } else {
            showLoading(false);
            alert(response.message || '프로필 완성 중 오류가 발생했습니다.');
          }
        },
        error: function(xhr) {
          showLoading(false);
          let errorMsg = '프로필 완성 중 오류가 발생했습니다.';

          if (xhr.responseJSON && xhr.responseJSON.message) {
            errorMsg = xhr.responseJSON.message;
          }

          alert(errorMsg);
        }
      });
    });

    // 실시간 유효성 검사
    $('#name').on('blur', validateName);
    $('#age').on('blur', validateAge);
  });

  // 유효성 검사 함수들
  function validateForm() {
    let isValid = true;

    if (!validateName()) isValid = false;
    if (!validateAge()) isValid = false;
    if (!validateGender()) isValid = false;
    if (!validateDisease()) isValid = false;
    if (!validateDisability()) isValid = false;
    if (!validateMedication()) isValid = false;

    return isValid;
  }

  function validateName() {
    const name = $('#name').val().trim();
    const nameInput = $('#name');
    const errorDiv = $('#nameError');

    if (name === '') {
      showError(nameInput, errorDiv, '이름을 입력해주세요.');
      return false;
    }

    if (name.length < 2 || name.length > 10) {
      showError(nameInput, errorDiv, '이름은 2~10글자로 입력해주세요.');
      return false;
    }

    showSuccess(nameInput, errorDiv);
    return true;
  }

  function validateAge() {
    const age = $('#age').val();
    const ageInput = $('#age');
    const errorDiv = $('#ageError');

    if (age === '' || age < 1 || age > 120) {
      showError(ageInput, errorDiv, '올바른 나이를 입력해주세요. (1~120)');
      return false;
    }

    showSuccess(ageInput, errorDiv);
    return true;
  }

  function validateGender() {
    const gender = $('input[name="gender"]:checked').val();
    const errorDiv = $('#genderError');

    if (!gender) {
      errorDiv.text('성별을 선택해주세요.');
      return false;
    }

    errorDiv.text('');
    return true;
  }

  function validateDisease() {
    const disease = $('input[name="disease"]:checked').val();
    const errorDiv = $('#diseaseError');

    if (disease === undefined) {
      errorDiv.text('질병 여부를 선택해주세요.');
      return false;
    }

    errorDiv.text('');
    return true;
  }

  function validateDisability() {
    const disability = $('input[name="disability"]:checked').val();
    const errorDiv = $('#disabilityError');

    if (disability === undefined) {
      errorDiv.text('장애 여부를 선택해주세요.');
      return false;
    }

    errorDiv.text('');
    return true;
  }

  function validateMedication() {
    const medication = $('input[name="medication"]:checked').val();
    const errorDiv = $('#medicationError');

    if (medication === undefined) {
      errorDiv.text('복용약물 여부를 선택해주세요.');
      return false;
    }

    errorDiv.text('');
    return true;
  }

  function showError(input, errorDiv, message) {
    input.addClass('is-invalid').removeClass('is-valid');
    errorDiv.text(message);
  }

  function showSuccess(input, errorDiv, message = '') {
    input.removeClass('is-invalid').addClass('is-valid');
    errorDiv.text(message);
  }

  function showLoading(show) {
    const spinner = $('#loadingSpinner');
    const button = $('#completeBtn');

    if (show) {
      spinner.show();
      button.prop('disabled', true).text('처리 중...');
    } else {
      spinner.hide();
      button.prop('disabled', false).text('프로필 완성하기');
    }
  }
</script>
</body>
</html>