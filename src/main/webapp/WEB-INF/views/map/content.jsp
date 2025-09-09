<%@ page contentType="text/html; charset=UTF-8" language="java" isELIgnored="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <%-- CSS 연결 --%>
    <link rel="stylesheet" href="/css/map.css" />

    <%-- Leaflet 라이브러 사용을 위한 CSS 연결 --%>
    <%-- 예: 지도 기본 스타일, 확대/축소 버튼 모양 등 --%>
    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css" />

    <!-- Chart.js CDN 연결 -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

    <%-- 국가 검색 입력창 --%>
    <div class="search-container">
        <input type="text" id="country-search" class="country-search-input" placeholder="검색할 국가를 입력해주세요" />
    </div>

    <%-- 지도 (객체를 사용하기 위해 id 추가) --%>
    <div id="map" class="map">

         <div class="guideline-buttons">

            <!-- 즐겨찾기 -->
            <div class="tooltip-container">
                <button class="blue">
                    <span>즐겨찾기</span>
                </button>
                <div class="tooltip-content scrollable">
                    <ul id="favorites-list">
                        <!-- JS로 <li> 자동 생성 -->
                    </ul>
                </div>
            </div>

            <%-- 행동지침 --%>
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

        <!-- 국가 통계 패널 -->
        <div id="country-info-panel" class="country-info-panel hidden">
            <div class="panel-header">
                <div class="panel-country">
                    <img id="country-flag" src="" alt="국기" class="flag">
                    <span id="country-name">국가명</span>
                    <span id="country-continent">대륙명</span>
                </div>
                <button onclick="closeInfoPanel()" class="close-btn">✕</button>
            </div>
            <div class="panel-body">
                <h3>여행경보</h3>
                <canvas id="travelChart"></canvas>
                <h3>방문객</h3>
                <canvas id="visitChart"></canvas>

                <h3>환율</h3>
                <canvas id="currencyChart"></canvas>
                <script>
                    async function loadCurrencyChart(countryId) {
                        const response = await fetch(`/fx/${countryId}`);
                        const data = await response.json();

                        // X축 (baseDate)
                        const labels = data.map(r => {
                            if (typeof r.baseDate === "string") {
                                return r.baseDate;
                            } else if (r.baseDate && r.baseDate.year) {
                                return `${r.baseDate.year}-${String(r.baseDate.monthValue).padStart(2, "0")}-${String(r.baseDate.dayOfMonth).padStart(2, "0")}`;
                            }
                            return "unknown";
                        });

                        // Y축 (dealBasR)
                        const values = data.map(r => r.dealBasR);

                        // 차트 생성
                        new Chart(document.getElementById("currencyChart"), {
                            type: "line",
                            data: {
                                labels: labels,
                                datasets: [{
                                    label: "환율",
                                    data: values,
                                    borderColor: "#30609D",
                                    fill: false
                                }]
                            }
                        });
                    }
                </script>

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
        const map = L.map("map", {
            minZoom: 2,
            maxZoom: 10,
            maxBounds: [
                [30, 70],   // 남서쪽 좌표 (중국 남부~타이완 서쪽 근처)
                [45, 70]    // 북동쪽 좌표 (일본 홋카이도 포함, 태평양 쪽 잘림)
            ],
            maxBoundsViscosity: 1.0
        }).setView([37.5665, 126.9780], 2); // 서울 중심

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

        // 지도에 여행경보 색칠
        function getColor(level) {
            const rootStyles = getComputedStyle(document.documentElement);
            switch(level) {
                case "여행유의":
                    return rootStyles.getPropertyValue("--semantic-caution").trim();   // 노랑
                case "여행자제":
                    return rootStyles.getPropertyValue("--semantic-warning").trim();   // 주황
                case "철수권고":
                    return rootStyles.getPropertyValue("--semantic-error").trim();   // 빨강
                case "여행금지":
                    return rootStyles.getPropertyValue("--main-900").trim();   // 검정
                default:
                    return rootStyles.getPropertyValue("--main-100").trim();  // 정보 없음
            }
        }

        // 여행경보 불러오기
        fetch("/alerts/all")
          .then(res => res.json())
          .then(alertData => {
              console.log("alerts/all 응답:", alertData);
              const alertMap = {};

              // alertMap 채우기
                alertData.forEach(d => {

                    // country 객체 키 전부 출력
                    console.log("country obj:", d.country);

                    const rawIso = d.country.isoCode || d.country.iso_code || d.country.ISO_CODE;
                    if (rawIso) {
                        const iso = rawIso.trim().toUpperCase();
                        alertMap[iso] = d.levelValue;
                    }
                });

              console.log("alertMap 최종:", alertMap);

              // GeoJSON 불러오기
              fetch("/geojson/custom.geo.json")
                .then(res => res.json())
                .then(geoData => {
                    console.log("GeoJSON:", geoData);

                    L.geoJson(geoData, {
                        style: feature => {
                            const iso = (feature.properties.iso_a3 || "").trim().toUpperCase();
                            const level = alertMap[iso];
                            console.log("Feature ISO:", iso, "Level:", level);
                            return {
                                fillColor: getColor(level),
                                weight: 1,
                                color: "white",
                                fillOpacity: 0.7
                            };
                        },
                        onEachFeature: (feature, layer) => {
                            const iso = (feature.properties.iso_a3 || "").trim().toUpperCase();
                            const level = alertMap[iso] || "정보 없음";
                            console.log("Feature ISO:", iso, "Level:", level);
                            layer.bindPopup(`${feature.properties.admin} : ${level}`);
                        }
                    }).addTo(map);
                });
          });


        /*
        * 변수형 마커 추가 (검색 시 재활용하기 위함)
        * L.marker([위도, 경도]): 지도 위에 마커 생성
        * .addTo(map): 지도에 마커 추가
        * .bindPopup("텍스트"): 마커 클릭 시 뜨는 말풍선 내용
        * .openPopup(): 기본으로 열어둔 상태
        */
        let marker = L.marker([37.5665, 126.9780])
            .addTo(map)
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
                    map.setView([lat, lon], 2);

                    // 기존 마커 제거 후 새 마커 추가
                    marker.setLatLng([lat, lon])

                    // 기존 팝업 완전히 제거
                    marker.unbindPopup();

                    /*
                    * ${} 사용 시 JSP EL 문법과 JS 템플릿 리터럴이 충돌나고 서버에서 가로채서 country 출력 안됨
                    * + (문자열 연결 방식) 로 변경
                    */
                    marker.bindPopup("여기는 " + country + " 입니다.").openPopup();

                    // 사이드 패널 열기
                    openInfoPanel(country);

                } else {
                     // 응답이 없을 경우 알림창 뜸
                     alert("국가를 찾을 수 없습니다.");
                }
            } catch (err) {
                // 콘솔로 에러 메시지 확인
                console.error("검색 오류", err);
            }
        });

        // 국가 데이터 (백엔드 → countryMap)
        let countryMap = {};

        async function loadCountries() {
            try {
                const res = await fetch("/countries/all");
                const data = await res.json();
                data.forEach(c => {
                    countryMap[c.isoCode.toUpperCase()] = c;
                });
                console.log("countryMap:", countryMap);
            } catch (err) {
                console.error("국가 데이터 불러오기 실패", err);
            }
        }

        // 통계 패널 열기 함수
        async function openInfoPanel(countryName) {
            document.getElementById("country-info-panel").classList.add("show");

            try {
                const res = await fetch(`/countries/search?name=${encodeURIComponent(countryName)}`);
                const country = await res.json();

                // 패널 정보 채우기
                document.getElementById("country-flag").src = country.img || `/images/aimg.png`;
                document.getElementById("country-name").innerText = country.countryName;
                document.getElementById("country-continent").innerText = country.continent;

                // 차트 렌더링
                renderCharts();

            } catch (err) {
                console.error("국가 데이터 불러오기 오류:", err);
            }
        }

        // 통계 패널 닫기 함수
        function closeInfoPanel() {
            document.getElementById("country-info-panel").classList.remove("show");
        }

        // 차트 인스턴스 전역 변수
         let travelChartInstance, visitChartInstance, currencyChartInstance;

        function renderCharts() {
            const travelCtx = document.getElementById("travelChart").getContext("2d");
            const visitCtx = document.getElementById("visitChart").getContext("2d");
            const currencyCtx = document.getElementById("currencyChart").getContext("2d");

            if (travelChartInstance) travelChartInstance.destroy();
            if (visitChartInstance) visitChartInstance.destroy();
            if (currencyChartInstance) currencyChartInstance.destroy();

            // 여행 경보 통계
            travelChartInstance = new Chart(travelCtx, {
                type: "doughnut",
                data: {
                    labels: ["여행유의", "여행자제"],
                    datasets: [{ data: [80, 20], backgroundColor: ["#FEE33C", "#FAAD14"] }]
                }
            });

            // 방문객 통계
            visitChartInstance = new Chart(visitCtx, {
                type: "bar",
                data: {
                    labels: ["4월","5월","6월","7월","8월","9월"],
                    datasets: [{ data: [10,20,15,25,18,22], backgroundColor: "#5A90D2" }]
                }
            });

            // 환율 통계
            currencyChartInstance = new Chart(currencyCtx, {
                type: "line",
                data: {
                    labels: Array.from({length: 30}, (_,i)=>i+1),
                    datasets: [{ data: Array.from({length:30}, ()=>Math.random()*100), borderColor: "#4caf50" }]
                }
            });
        }

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
               <a href="/post/list" class="more">더보기 →</a>
           </div>

            <%-- 최신글 목록 (5개만 표시) --%>
            <div class="post-list">
                <a href="/post/list" class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                    <span>YYYY-MM-DD</span>
                </a>

                <a href="/post/list" class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                    <span>YYYY-MM-DD</span>
                </a>

                <a href="/post/list" class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                    <span>YYYY-MM-DD</span>
                </a>

                <a href="/post/list" class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                    <span>YYYY-MM-DD</span>
                </a>

                <a href="/post/list" class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                    <span>YYYY-MM-DD</span>
                </a>
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
                <a href="/post/list" class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                    <span>YYYY-MM-DD</span>
                </a>

                <a href="/post/list" class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                    <span>YYYY-MM-DD</span>
                </a>

                <a href="/post/list" class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                    <span>YYYY-MM-DD</span>
                </a>

                <a href="/post/list" class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                    <span>YYYY-MM-DD</span>
                </a>

                <a href="/post/list" class="post-item">
                    <div class="country-and-title">
                        <span>나라</span>
                        <span>제목</span>
                    </div>
                    <span>YYYY-MM-DD</span>
                </a>
            </div>
        </div>
    </div>

    <%-- 인기 후기 --%>
    <div class="popular-reviews">
        <span class="review-title">인기 후기</span>

        <div class="carousel">

        <%-- 왼쪽 화살표 --%>
        <button class="carousel-btn prev">❮</button>

            <div class="carousel-track-container">
                <div class="carousel-track">

                <%-- 슬라이드 1p (3개의 카드 표시) --%>
                <a href="/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="review-title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>
                <a href="/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="review-title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>
                <a href="/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="review-title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>

                <%-- 슬라이드 2p (3개의 카드 표시) --%>
                <a href="/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="review-title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>
                <a href="/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="review-title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>
                <a href="/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="review-title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>

                <%-- 슬라이드 3p (3개의 카드 표시) --%>
                <a href="/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="review-title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>
                <a href="/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="review-title">제목</span>
                        <span class="date">YYYY-MM-DD</span>
                    </div>
                </a>
                <a href="/post/list?category=REVIEW" class="review-card">
                    <img class="thumbnail" src="/images/sample.png" alt="썸네일">
                    <div class="info">
                        <span class="country">나라</span>
                        <span class="review-title">제목</span>
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

    <%-- 즐겨찾기 국가 js 연결 --%>
    <script src="/js/favorites.js"></script>

</body>
</html>
