<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="card p-4 shadow-sm">
    <h4 class="mb-4">국가 정보</h4>

    <!-- 국가 이미지 -->
    <div class="text-left mb-4">
        <c:if test="${not empty country.countryImg}">
            <img src="${country.countryImg}"
                 alt="${country.countryName} 이미지"
                 class="img-fluid rounded"
                 style="max-height: 150px;">
        </c:if>
    </div>

    <form action="/admin/countries/update" method="post">
        <input type="hidden" name="countryId" value="${country.countryId}"/>

        <div class="row mb-3">
            <div class="col-6">
                <label class="form-label fw-bold">한글명</label>
                <input type="text" name="countryName" class="form-control" value="${country.countryName}"/>
            </div>
            <div class="col-6">
                <label class="form-label fw-bold">영문명</label>
                <input type="text" name="countryEnName" class="form-control" value="${country.countryEnName}"/>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-6">
                <label class="form-label fw-bold">ISO 코드</label>
                <input type="text" name="isoCode" class="form-control" value="${country.isoCode}"/>
            </div>
            <div class="col-6">
                <label class="form-label fw-bold">여행경보</label>
                <select name="levelValue" class="form-select">
                    <option value="">-</option>
                    <option value="여행유의" <c:if test="${country.levelValue eq '여행유의'}">selected</c:if>>여행유의</option>
                    <option value="여행자제" <c:if test="${country.levelValue eq '여행자제'}">selected</c:if>>여행자제</option>
                    <option value="철수권고" <c:if test="${country.levelValue eq '철수권고'}">selected</c:if>>철수권고</option>
                    <option value="여행금지" <c:if test="${country.levelValue eq '여행금지'}">selected</c:if>>여행금지</option>
                </select>
            </div>
        </div>

        <div class="text-center">
            <button type="submit" class="btn btn-primary w-100">수정하기</button>
        </div>
    </form>
</div>
