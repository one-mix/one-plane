<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <%-- CSS 연결 --%>
    <link rel="stylesheet" href="/css/map.css" />

    <%-- Leaflet 라이브러 사용을 위한 CSS 연결 --%>
    <%-- 예: 지도 기본 스타일, 확대/축소 버튼 모양 등 --%>
    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css" />
</head>
<body>
    <%-- 지도 (객체를 사용하기 위해 id 추가) --%>
    <div id="map" class="map"></div>

    <%-- Leaflet 라이브러 사용을 위한 JS 연결 --%>
    <%-- 예: 지도 기능 제어 --%>
    <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>

    <script>
        /*
        * 지도 객체 생성
        * L.map('map'): id="map"인 요소에 지도 객체 연결
        * .setView([위도, 경도], 줌레벨): 시작 위치와 확대 수준 설정
        * [37.5665, 126.9780]: 서울 시청 근처 좌표
        * 13: 줌 레벨 (0=전세계, 18=아주 세밀하게)
        */
        const map = L.map('map').setView([37.5665, 126.9780], 13);

        /*
        * 지도 타일 불러오기
        * 지도 배경 이미지를 어디서 가져올지 설정
        * {z}/{x}/{y}: 줌/위도/경도 타일 좌표 자동 변환
        * attribution: 지도 오른쪽 아래 표시되는 저작권 문구
        * .addTo(map): 위에서 만든 map 객체에 연결
        */
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; OpenStreetMap contributors'
        }).addTo(map);

        /*
        * 마커 추가
        * L.marker([위도, 경도]): 지도 위에 마커 생성
        * .addTo(map): 지도에 마커 추가
        * .bindPopup("텍스트"): 마커 클릭 시 뜨는 말풍선 내용
        * .openPopup(): 기본으로 열어둔 상태
        */
        L.marker([37.5665, 126.9780])
            .addTo(map)
            .bindPopup("여기가 서울입니다!")
            .openPopup();
    </script>

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
               <a href="http://localhost:8080/post/list" class="more">더보기 →</a>
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
               <a href="http://localhost:8080/post/list" class="more">더보기 →</a>
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
                <a href="http://localhost:8080/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>
                <a href="http://localhost:8080/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>
                <a href="http://localhost:8080/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>

                <%-- 슬라이드 2p (3개의 카드 표시) --%>
                <a href="http://localhost:8080/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>
                <a href="http://localhost:8080/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>
                <a href="http://localhost:8080/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>

                <%-- 슬라이드 3p (3개의 카드 표시) --%>
                <a href="http://localhost:8080/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>
                <a href="http://localhost:8080/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>
                <a href="http://localhost:8080/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>
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
