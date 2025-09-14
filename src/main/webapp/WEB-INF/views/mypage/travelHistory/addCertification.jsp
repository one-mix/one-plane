<%--<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>

<%--<style>--%>
<%--    .add-certification-container {--%>
<%--        max-width: 600px;--%>
<%--        margin: 0 auto;--%>
<%--        padding: 20px;--%>
<%--    }--%>

<%--    .form-header {--%>
<%--        text-align: center;--%>
<%--        margin-bottom: 30px;--%>
<%--    }--%>

<%--    .form-group {--%>
<%--        margin-bottom: 20px;--%>
<%--    }--%>

<%--    .form-group label {--%>
<%--        display: block;--%>
<%--        margin-bottom: 5px;--%>
<%--        font-weight: bold;--%>
<%--    }--%>

<%--    .form-control {--%>
<%--        width: 100%;--%>
<%--        padding: 8px 12px;--%>
<%--        border: 1px solid #ddd;--%>
<%--        border-radius: 4px;--%>
<%--        font-size: 14px;--%>
<%--    }--%>

<%--    .form-control:focus {--%>
<%--        outline: none;--%>
<%--        border-color: #007bff;--%>
<%--    }--%>

<%--    .form-text {--%>
<%--        font-size: 12px;--%>
<%--        color: #666;--%>
<%--        margin-top: 5px;--%>
<%--    }--%>

<%--    .distance-info {--%>
<%--        margin: 15px 0;--%>
<%--    }--%>

<%--    .alert {--%>
<%--        padding: 12px;--%>
<%--        border-radius: 4px;--%>
<%--    }--%>

<%--    .alert-info {--%>
<%--        background-color: #e3f2fd;--%>
<%--        border: 1px solid #90caf9;--%>
<%--        color: #1976d2;--%>
<%--    }--%>

<%--    .form-actions {--%>
<%--        display: flex;--%>
<%--        gap: 10px;--%>
<%--        justify-content: center;--%>
<%--        margin-top: 30px;--%>
<%--    }--%>

<%--    .btn {--%>
<%--        padding: 10px 20px;--%>
<%--        border: none;--%>
<%--        border-radius: 4px;--%>
<%--        cursor: pointer;--%>
<%--        font-size: 14px;--%>
<%--    }--%>

<%--    .btn-primary {--%>
<%--        background-color: #007bff;--%>
<%--        color: white;--%>
<%--    }--%>

<%--    .btn-secondary {--%>
<%--        background-color: #6c757d;--%>
<%--        color: white;--%>
<%--    }--%>

<%--    .btn:hover {--%>
<%--        opacity: 0.9;--%>
<%--    }--%>
<%--</style>--%>

<%--<div class="add-certification-container">--%>
<%--    <div class="form-header">--%>
<%--        <h2>국가 방문 인증</h2>--%>
<%--        <p>방문하신 국가와 여행 날짜, 인증 사진을 업로드해주세요.</p>--%>
<%--    </div>--%>

<%--    <form action="/mypage/certification/add" method="post" enctype="multipart/form-data" class="certification-form">--%>

<%--        <!-- 국가 입력 -->--%>
<%--        <div class="form-group">--%>
<%--            <label for="countryName">방문한 국가 *</label>--%>
<%--            <input type="text"--%>
<%--                   name="countryName"--%>
<%--                   id="countryName"--%>
<%--                   required--%>
<%--                   class="form-control"--%>
<%--                   placeholder="국가명을 직접 입력하세요"/>--%>
<%--        </div>--%>
<%--        <!-- 여행 날짜 -->--%>
<%--        <div class="form-group">--%>
<%--            <label for="travelDate">방문 날짜 *</label>--%>
<%--            <!-- 수정 후 -->--%>
<%--            <input type="date" id="certificationDate" name="certificationDate" required />--%>

<%--        </div>--%>

<%--        <!-- 인증 사진 -->--%>
<%--        <div class="form-group">--%>
<%--            <label for="certificationImg">방문 인증 사진 *</label>--%>
<%--            <input type="file" name="certificationImg" id="certificationImg"--%>
<%--                   accept="image/*" required class="form-control">--%>
<%--            <small class="form-text text-muted">--%>
<%--                항공권, 입국 스탬프, 현지 랜드마크 사진 등 해당 국가 방문을 인증할 수 있는 사진을 업로드해주세요.--%>
<%--            </small>--%>
<%--        </div>--%>

<%--        <!-- 거리 표시 -->--%>
<%--        <div class="distance-info" style="display: none;">--%>
<%--            <div class="alert alert-info">--%>
<%--                <strong>한국으로부터의 거리:</strong> <span id="distanceValue">0</span>km--%>
<%--            </div>--%>
<%--        </div>--%>

<%--        <div class="form-actions">--%>
<%--            <button type="button" onclick="history.back()" class="btn btn-secondary">취소</button>--%>
<%--            <button type="submit" class="btn btn-primary">인증 등록</button>--%>
<%--        </div>--%>
<%--    </form>--%>
<%--</div>--%>

<%--<script>--%>
<%--    // 국가 선택 시 거리 표시--%>
<%--    document.getElementById('countryId').addEventListener('change', function() {--%>
<%--        const selectedOption = this.options[this.selectedIndex];--%>
<%--        const distance = selectedOption.getAttribute('data-distance');--%>
<%--        const distanceInfo = document.querySelector('.distance-info');--%>
<%--        const distanceValue = document.getElementById('distanceValue');--%>

<%--        if (distance && distance !== '0') {--%>
<%--            distanceValue.textContent = distance;--%>
<%--            distanceInfo.style.display = 'block';--%>
<%--        } else {--%>
<%--            distanceInfo.style.display = 'none';--%>
<%--        }--%>
<%--    });--%>

<%--    // 폼 제출 전 검증--%>
<%--    document.querySelector('.certification-form').addEventListener('submit', function(e) {--%>
<%--        const countryId = document.getElementById('countryId').value;--%>
<%--        const travelDate = document.getElementById('travelDate').value;--%>
<%--        const certificationImg = document.getElementById('certificationImg').files[0];--%>

<%--        if (!countryId) {--%>
<%--            alert('국가를 선택해주세요.');--%>
<%--            e.preventDefault();--%>
<%--            return;--%>
<%--        }--%>

<%--        if (!travelDate) {--%>
<%--            alert('방문 날짜를 입력해주세요.');--%>
<%--            e.preventDefault();--%>
<%--            return;--%>
<%--        }--%>

<%--        if (!certificationImg) {--%>
<%--            alert('방문 인증 사진을 업로드해주세요.');--%>
<%--            e.preventDefault();--%>
<%--            return;--%>
<%--        }--%>
<%--    });--%>
<%--</script>--%>
