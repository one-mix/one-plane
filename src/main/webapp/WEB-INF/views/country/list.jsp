<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>국가 목록</h2>

<table border="1" style="width: 100%; border-collapse: collapse; text-align: center;">
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
                    <c:choose>
                        <c:when test="${not empty country.img}">
                            <img src="<c:out value='${country.img}' escapeXml='false'/>"
                                 alt="${country.countryName}" width="80" height="50">
                        </c:when>
                        <c:otherwise>
                            <span>?</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
