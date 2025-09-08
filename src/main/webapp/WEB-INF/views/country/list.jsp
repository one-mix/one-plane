<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>국가 목록</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #f4f4f4;
        }
        h2 {
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <h2>국가 목록</h2>
    <table>
        <thead>
            <tr>
                <th>번호</th>
                <th>국가명</th>
                <th>영문명</th>
                <th>대륙</th>
                <th>ISO 코드</th>
                <th>이미지</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="country" items="${countries}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${country.countryName}</td>
                    <td>${country.countryEnName}</td>
                    <td>${country.continent}</td>
                    <td>${country.isoCode}</td>
                    <td>
                        <c:if test="${not empty country.img}">
                            <img src="${country.img}" alt="${country.countryName}" width="50">
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
