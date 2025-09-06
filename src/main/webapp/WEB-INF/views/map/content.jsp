<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <%-- CSS 연결 --%>
    <link rel="stylesheet" href="/css/map.css" />
</head>
<body>
    <%-- 지도 --%>
    <div class="map"></div>

    <%-- 대륙 카테고리 목록 --%>
    <div class="category-list">
            <div class="category-container">
                <img class="category" src="/images/category1.png" alt="카테고리">
                <span class="category-title">전체</span>
            </div>
            <div class="category-container">
                <img class="category" src="/images/category2.png" alt="카테고리">
                <span class="category-title">미주</span>
            </div>
            <div class="category-container">
                <img class="category" src="/images/category3.png" alt="카테고리">
                <span class="category-title">유럽</span>
            </div>
            <div class="category-container">
                <img class="category" src="/images/category4.png" alt="카테고리">
                <span class="category-title">아주</span>
            </div>
            <div class="category-container">
                <img class="category" src="/images/category5.png" alt="카테고리">
                <span class="category-title">중동</span>
            </div>
            <div class="category-container">
                <img class="category" src="/images/category6.png" alt="카테고리">
                <span class="category-title">아프리카</span>
            </div>
        </div>

    <%-- 최신글/인기글 --%>
    <div class="new-and-popular-posts">

        <%-- 최신글 --%>
        <div class="section posts">

           <%-- 최신글 + 더보기--%>
           <div class="title-and-more">
               <span class="title">최신글</span>
               <span class="more"> 더보기 -></span>
           </div>

            <%-- 최신글 목록 (5개만 표시) --%>
            <div class="post-list">
                <div class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                    <span>YYYY-MM-DD</span>
                </div>
                 <div class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                     <span>YYYY-MM-DD</span>
                 </div>
                 <div class="post-item">
                     <div class="country-and-title">
                         <span>나라</span>
                         <span>제목</span>
                     </div>
                     <span>YYYY-MM-DD</span>
                 </div>
                 <div class="post-item">
                     <div class="country-and-title">
                         <span>나라</span>
                         <span>제목</span>
                     </div>
                     <span>YYYY-MM-DD</span>
                 </div>
                 <div class="post-item">
                     <div class="country-and-title">
                         <span>나라</span>
                         <span>제목</span>
                     </div>
                     <span>YYYY-MM-DD</span>
                 </div>
            </div>
        </div>

        <%-- 인기글 --%>
        <div class="section posts">

           <%-- 인기글 + 더보기--%>
           <div class="title-and-more">
               <span class="title">인기글</span>
               <span class="more"> 더보기 -></span>
           </div>

           <%-- 인기글 목록 (5개만 표시) --%>
           <div class="post-list">
                <div class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                    <span>YYYY-MM-DD</span>
                </div>
                 <div class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                     <span>YYYY-MM-DD</span>
                 </div>
                 <div class="post-item">
                     <div class="country-and-title">
                         <span>나라</span>
                         <span>제목</span>
                     </div>
                     <span>YYYY-MM-DD</span>
                 </div>
                 <div class="post-item">
                     <div class="country-and-title">
                         <span>나라</span>
                         <span>제목</span>
                     </div>
                     <span>YYYY-MM-DD</span>
                 </div>
                 <div class="post-item">
                     <div class="country-and-title">
                         <span>나라</span>
                         <span>제목</span>
                     </div>
                     <span>YYYY-MM-DD</span>
                 </div>
            </div>
        </div>
    </div>

    <%-- 인기 후기 --%>
    <div class="popular-reviews">
        <span class="title">인기 후기</span>

        <div class="carousel">

        <%-- 왼쪽 화살표 --%>
        <button class="carousel-btn prev">❮</button>

            <div class="carousel-track-container">
                <div class="carousel-track">

                <%-- 슬라이드 1p (3개의 카드 표시) --%>
                <div class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </div>
                <div class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </div>
                <div class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </div>

                <%-- 슬라이드 2p (3개의 카드 표시) --%>
                <div class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </div>
                <div class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </div>
                <div class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </div>

                <%-- 슬라이드 3p (3개의 카드 표시) --%>
                <div class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </div>
                <div class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </div>
                <div class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </div>
            </div>

        <%-- 인디케이터 --%>
        <div class="carousel-indicators">
            <span class="dot active"></span>
            <span class="dot"></span>
            <span class="dot"></span>
        </div>
    </div>

        <%-- 오른쪽 화살표 --%>
        <button class="carousel-btn next">❯</button>
        </div>


    <%-- 커설 js 연결 --%>
    <script src="/js/carousel.js"></script>

</body>
</html>
