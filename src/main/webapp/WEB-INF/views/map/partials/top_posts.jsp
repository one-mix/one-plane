<%@ page contentType="text/html; charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
</head>
<body>
    <div class="new-and-popular-posts">
        <%-- 최신글 --%>
        <div class="section posts">

           <%-- 최신글 + 더보기--%>
           <div class="title-and-more">
               <span class="title">최신글</span>
               <a href="/post/list" class="more">더보기 →</a>
           </div>

            <%-- 최신글 목록 (5개만 표시) --%>
            <div class="post-list">
                <c:choose>
                    <c:when test="${empty latestPosts}">
                        <div class="no-posts">등록된 게시글이 없습니다.</div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="post" items="${latestPosts}" varStatus="status">
                            <a href="/post/detail/${post.postId}" class="post-item">
                                <div class="country-and-title">
                                    <span>
                                        <c:choose>
                                            <c:when test="${post.category == 'READY'}">준비</c:when>
                                            <c:when test="${post.category == 'REVIEW'}">후기</c:when>
                                            <c:when test="${post.category == 'ACCOMPANY'}">동행</c:when>
                                            <c:when test="${post.category == 'FREE'}">자유</c:when>
                                            <c:otherwise>${post.category}</c:otherwise>
                                        </c:choose>
                                    </span>
                                    <span title="${post.title}">
                                            <c:choose>
                                                <c:when test="${fn:length(post.title) > 20}">
                                                    ${fn:substring(post.title, 0, 20)}...
                                                </c:when>
                                                <c:otherwise>
                                                    ${post.title}
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                </div>
                                <c:if test="${not empty post.createdAt}">
                                    <span><fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd" /></span>
                                </c:if>
                            </a>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <%-- 인기글 --%>
        <div class="section posts">

            <%-- 인기글 + 더보기--%>
            <div class="title-and-more">
                <span class="title">인기글</span>
                <a href="/post/list" class="more">더보기 →</a>
            </div>

            <%-- 인기글 목록 (5개만 표시) --%>
            <div class="post-list">
                <c:choose>
                    <c:when test="${empty popularPosts}">
                        <div class="no-posts">등록된 게시글이 없습니다.</div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="post" items="${popularPosts}" varStatus="status">
                            <a href="/post/detail/${post.postId}" class="post-item">
                                <div class="country-and-title">
                                    <span class="country">
                                        <c:choose>
                                            <c:when test="${post.category == 'READY'}">준비</c:when>
                                            <c:when test="${post.category == 'REVIEW'}">후기</c:when>
                                            <c:when test="${post.category == 'ACCOMPANY'}">동행</c:when>
                                            <c:when test="${post.category == 'FREE'}">자유</c:when>
                                            <c:otherwise>${post.category}</c:otherwise>
                                        </c:choose>
                                    </span>
                                    <span title="${post.title}">
                                            <c:choose>
                                                <c:when test="${fn:length(post.title) > 20}">
                                                    ${fn:substring(post.title, 0, 20)}...
                                                </c:when>
                                                <c:otherwise>
                                                    ${post.title}
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                </div>
                                <span><fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd" /></span>
                            </a>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</body>
</html>