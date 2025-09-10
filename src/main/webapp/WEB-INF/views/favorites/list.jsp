<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>즐겨찾기 국가</h2>
<table border="1">
    <tr>
        <th>번호</th>
        <th>국가명</th>
        <th>영문명</th>
        <th>대륙</th>
        <th>이미지</th>
        <th>삭제</th>
    </tr>
    <c:forEach var="fav" items="${favorites}" varStatus="status">
        <tr>
            <td>${status.index + 1}</td>
            <td>${fav.country.countryName}</td>
            <td>${fav.country.countryEnName}</td>
            <td>${fav.country.continent}</td>
            <td><img src="${fav.country.img}" width="50"/></td>
            <td>
                <form action="/favorites/remove" method="post">
                    <input type="hidden" name="favoritesCountryId" value="${fav.favoritesCountryId}"/>
                    <input type="hidden" name="userId" value="${fav.userId}"/>
                    <button type="submit">삭제</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
