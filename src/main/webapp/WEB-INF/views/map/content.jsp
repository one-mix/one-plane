<%@ page contentType="text/html; charset=UTF-8" %>
<%-- EL 해석 무시 --%>
<%@ page isELIgnored="true" %>
<html>
<head>
    <%-- CSS 연결 --%>
    <link rel="stylesheet" href="/css/map.css" />

    <%-- Leaflet 라이브러 사용을 위한 CSS 연결 --%>
    <%-- 예: 지도 기본 스타일, 확대/축소 버튼 모양 등 --%>
    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css" />
</head>
<body>

    <%-- 국가 검색 입력창 --%>
    <div style="position:absolute; top:105px; left:65px; z-index:1000;">
        <input type="text" id="country-search" placeholder="국가 검색" style="padding:6px; width:200px;" />
    </div>

    <%-- 지도 (객체를 사용하기 위해 id 추가) --%>
    <div id="map" class="map">

         <%-- 행동지침 버튼 --%>
         <div class="guideline-buttons">
            <!-- 즐겨찾기 -->
            <div class="tooltip-container">
                <button class="blue">
                    <span>즐겨찾기</span>
                </button>
                <div class="tooltip-content scrollable">
                    <ui>
                        <li class="tooltip-country">가나</li>
                        <li class="tooltip-country">대한민국</li>
                        <li class="tooltip-country">미국</li>
                        <li class="tooltip-country">일본</li>
                        <li class="tooltip-country">중국</li>
                        <li class="tooltip-country">프랑스</li>
                        <li class="tooltip-country">영국</li>
                        <li class="tooltip-country">호주</li>
                        <li class="tooltip-country">브라질</li>
                        <li class="tooltip-country">캐나다</li>
                    </ui>
                </div>
            </div>
            <div class="tooltip-container">
                <button class="yellow">
                    <span class="level">1단계</span>
                    <span>여행유의</span>
                </button>
                <div class="tooltip-content">
                    <div class="tooltip-title">여행 예정자</div>
                    <ul>
                        <li class="tooltip-description">여행 시 신변안전에 유의</li>
                        <li class="tooltip-description">현지 상황 지속 확인</li>
                    </ul>
                    <div class="tooltip-title">체류자</div>
                    <ul>
                        <li class="tooltip-description">주의 깊게 일상 활동</li>
                        <li class="tooltip-description">비상 연락망 확보</li>
                    </ul>
                </div>
            </div>
            <div class="tooltip-container">
                <button class="orange">
                    <span class="level">2단계</span>
                    <span>여행자제</span>
                </button>
                <div class="tooltip-content">
                    <div class="tooltip-title">여행 예정자</div>
                    <ul>
                        <li class="tooltip-description">불필요한 여행은 자제</li>
                        <li class="tooltip-description">부득이할 경우 철저한 준비</li>
                    </ul>
                    <div class="tooltip-title">체류자</div>
                    <ul>
                        <li class="tooltip-description">위험 지역 방문 자제</li>
                        <li class="tooltip-description">주변 상황 모니터링</li>
                    </ul>
                </div>
            </div>
            <div class="tooltip-container">
                <button class="red">
                    <span class="level">3단계</span>
                    <span>철수권고</span>
                </button>
                <div class="tooltip-content">
                    <div class="tooltip-title">여행 예정자</div>
                    <ul>
                        <li class="tooltip-description">여행 취소 권고</li>
                        <li class="tooltip-description">대체 목적지 고려</li>
                    </ul>
                    <div class="tooltip-title">체류자</div>
                    <ul>
                        <li class="tooltip-description">즉시 철수 준비</li>
                        <li class="tooltip-description">대사관 지침 준수</li>
                    </ul>
                </div>
            </div>
            <div class="tooltip-container">
                <button class="black">
                    <span class="level">4단계</span>
                    <span>여행금지</span>
                </button>
                <div class="tooltip-content">
                    <div class="tooltip-title">여행 예정자</div>
                    <ul>
                        <li class="tooltip-description">모든 여행 금지</li>
                        <li class="tooltip-description">대체 목적지 필수</li>
                    </ul>
                    <div class="tooltip-title">체류자</div>
                    <ul>
                        <li class="tooltip-description">즉시 철수</li>
                        <li class="tooltip-description">필수 시 대사관 보호 요청</li>
                    </ul>
                </div>
            </div>
         </div>

     </div>

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
        * 변수형 마커 추가 (검색 시 재활용하기 위함)
        * L.marker([위도, 경도]): 지도 위에 마커 생성
        * .addTo(map): 지도에 마커 추가
        * .bindPopup("텍스트"): 마커 클릭 시 뜨는 말풍선 내용
        * .openPopup(): 기본으로 열어둔 상태
        */
        let marker = L.marker([37.5665, 126.9780])
            .addTo(map)
            .bindPopup("여기는 서울입니다.")
            .openPopup();

        // 검색 기능
        const searchInput = document.getElementById("country-search");

        searchInput.addEventListener("change", async function () {

            // 입력값 가져오기
            const country = this.value.trim();

            // 국가명이 비어있을 경우
            if (!country) {

                // 콘솔 경고 메시지 확인
                console.warn("검색어가 비어있습니다.");

                // 검색 요청 보내지 않음
                return;
            }

            try {
                // Nominatim API로 나라 검색
                const res = await fetch(
                    "https://nominatim.openstreetmap.org/search?country="
                    + encodeURIComponent(country)
                    + "&format=json"
                );

                // 응답 받기
                const data = await res.json();

                // 응답이 있을 경우
                if (data.length > 0) {
                    const lat = data[0].lat;
                    const lon = data[0].lon;

                    // 지도 이동
                    map.setView([lat, lon], 6);

                    // 기존 마커 제거 후 새 마커 추가
                    marker.setLatLng([lat, lon])

                    // 기존 팝업 완전히 제거
                    marker.unbindPopup();

                    /*
                    * ${} 사용 시 JSP EL 문법과 JS 템플릿 리터럴이 충돌나고 서버에서 가로채서 country 출력 안됨
                    * + (문자열 연결 방식) 로 변경
                    */
                    marker.bindPopup("여기는 " + country + " 입니다.").openPopup();
                } else {
                     // 응답이 없을 경우 알림창 뜸
                     alert("국가를 찾을 수 없습니다.");
                }
            } catch (err) {
                // 콘솔로 에러 메시지 확인
                console.error("검색 오류", err);
            }
        });

        // 모달 열기
        function openGuideline(title, body) {
            document.getElementById("modalTitle").innerText = title;
            document.getElementById("modalBody").innerText = body;
            document.getElementById("guidelineModal").style.display = "flex";
        }
        // 모달 닫기
        function closeGuideline() {
            document.getElementById("guidelineModal").style.display = "none";
        }
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
