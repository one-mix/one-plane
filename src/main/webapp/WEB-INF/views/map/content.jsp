<%@ page contentType="text/html; charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <%-- CSS 연결 --%>
    <link rel="stylesheet" href="/css/map.css" />

    <%-- Leaflet 라이브러 사용을 위한 CSS 연결 --%>
    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css" />

    <!-- Chart.js CDN 연결 -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

    <%-- 국가 검색 입력창 --%>
    <jsp:include page="./partials/search.jsp" />

    <%-- 지도 (객체를 사용하기 위해 id 추가) --%>
    <div id="map" class="map">

        <%-- 지도 안 즐겨찾기 + 가이드라인 버튼 --%>
        <jsp:include page="./partials/guideline.jsp" />

        <%-- 국가 통계 패널 --%>
        <jsp:include page="./partials/info_panel.jsp" />
     </div>

    <%-- Leaflet 라이브러 사용을 위한 JS 연결 --%>
    <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>

    <script>
        // 지도 객체 생성
        const map = L.map("map", {
            minZoom: 2,
            maxZoom: 10,
            maxBounds: [
                [30, 70],   // 남서쪽 좌표 (중국 남부~타이완 서쪽 근처)
                [45, 70]    // 북동쪽 좌표 (일본 홋카이도 포함, 태평양 쪽 잘림)
            ],
            maxBoundsViscosity: 1.0
        }).setView([37.5665, 126.9780], 2); // 서울 중심

        // 지도 타일 불러오기
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; OpenStreetMap contributors'
        }).addTo(map);

        // 지도에 여행경보 색칠
        function getColor(level) {
            const rootStyles = getComputedStyle(document.documentElement);
            switch(level) {
                case "여행유의":
                    return rootStyles.getPropertyValue("--semantic-success").trim();
                case "여행자제":
                    return rootStyles.getPropertyValue("--semantic-caution").trim();
                case "철수권고":
                    return rootStyles.getPropertyValue("--semantic-error").trim();
                case "여행금지":
                    return rootStyles.getPropertyValue("--gray-900").trim();
                default:
                    return rootStyles.getPropertyValue("--gray-100").trim();
            }
        }

        // 전역 변수로 GeoJSON 레이어 저장
        let geoLayer;

        // 여행경보 불러오기
        fetch("/alerts/all")
          .then(res => res.json())
          .then(alertData => {
              const alertMap = {};
              // alertMap 채우기
                alertData.forEach(d => {
                    const rawIso = d.country.isoCode || d.country.iso_code || d.country.ISO_CODE;
                    if (rawIso) {
                        const iso = rawIso.trim().toUpperCase();
                        alertMap[iso] = d.levelValue;
                    }
                });

              // GeoJSON 불러오기
              fetch("/geojson/custom.geo.json")
                .then(res => res.json())
                .then(geoData => {
                    geoLayer = L.geoJson(geoData, {
                        style: feature => {
                            const iso = (feature.properties.iso_a3 || "").trim().toUpperCase();
                            const level = alertMap[iso];
                            return {
                                fillColor: getColor(level),
                                weight: 1,
                                color: "white",
                                fillOpacity: 0.7,
                                interactive: true
                            };
                        },
                        onEachFeature: (feature, layer) => {
                            const iso = (feature.properties.iso_a3 || "").trim().toUpperCase();
                            const level = alertMap[iso] || "정보 없음";
                            const countryName =
                                feature.properties.admin ||
                                feature.properties.name ||
                                feature.properties.sovereignt ||
                                "국가명 없음";

                            const popupText = countryName + " : " + level;

                            // 클릭 이벤트
                            layer.on("click", (e) => {
                                L.popup()
                                  .setLatLng(e.latlng)
                                  .setContent(popupText)
                                  .openOn(map);
                            });

                            // 마우스 이벤트
                            layer.on("mouseover", (e) => {
                                L.popup()
                                  .setLatLng(e.latlng)
                                  .setContent(popupText)
                                  .openOn(map);
                            });
                        }
                    }).addTo(map);
                });
          })

            .then(() => {
              document.querySelectorAll(".category-container").forEach(el => {
                el.addEventListener("click", () => {
                  const category = el.querySelector(".category-title").innerText.trim();

                  const continentMap = {
                    "전체": null,
                    "미주": ["North America", "South America"],
                    "유럽": ["Europe"],
                    "아주": ["Asia"],
                    "중동": "MiddleEast",   // 커스텀 태그
                    "아프리카": ["Africa"]
                  };

                  // 중동 국가 목록 (ISO 코드 기준)
                  const middleEastCountries = [
                    "SAU","IRN","IRQ","ISR","JOR","SYR","LBN","TUR",
                    "ARE","QAT","KWT","OMN","YEM","BHR","EGY"
                  ];

                  const selected = continentMap[category];

                  geoLayer.eachLayer(layer => {
                    const cont = layer.feature.properties.continent;
                    const iso = (layer.feature.properties.iso_a3 || "").trim().toUpperCase();

                    let match = false;
                    if (!selected) {
                      match = true; // 전체
                    } else if (selected === "MiddleEast") {
                      match = middleEastCountries.includes(iso);
                    } else if (Array.isArray(selected)) {
                      match = selected.includes(cont);
                    } else {
                      match = cont === selected;
                    }

                    layer.setStyle({
                      fillOpacity: match ? 0.7 : 0.1
                    });
                  });
                });
              });
            });


        // 지도 위에 마커 생성
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
                console.warn("검색어가 비어있습니다.");
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

                    // 지도 이동 & 마커 갱신
                    map.setView([lat, lon], 2);
                    marker.setLatLng([lat, lon])
                          .unbindPopup()
                          .bindPopup("여기는 " + country + " 입니다.")
                          .openPopup();

                    // 국가 상세 조회
                     const countryRes = await fetch("/countries/search?name="
                                            + encodeURIComponent(country));
                     const countryData = await countryRes.json();

                    // 사이드 패널 열기
                    openInfoPanel(countryData);

                } else {
                     alert("국가를 찾을 수 없습니다.");
                }
            } catch (err) {
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
        async function openInfoPanel(countryData) {
            document.getElementById("country-info-panel").classList.add("show");

            // 현재 선택된 국가 ID 저장
            currentCountryId = countryData.countryId;

            // 패널 정보 채우기
            document.getElementById("country-flag").src = countryData.img || `/images/gallery.png`;
            document.getElementById("country-name").innerText = countryData.countryName;
            document.getElementById("country-continent").innerText = countryData.continent;

            /**
            * DOM 반영 보장
            * 0: 화면이 한번 업데이트 된 후 실행
            */
            setTimeout(() => {
                // GDP 데이터 로드
                loadGdpCharts(countryData.countryName);
                 // 탄소 차트 로드
                loadCarbonChart(countryData.countryName);
            }, 0);
        }

        // 통계 패널 닫기 함수
        function closeInfoPanel() {
            document.getElementById("country-info-panel").classList.remove("show");
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
    <jsp:include page="./partials/category.jsp" />

    <%-- 최신글/인기글 --%>
    <jsp:include page="./partials/top_posts.jsp" />

    <%-- 인기 후기 --%>
    <jsp:include page="./partials/popular_reviews.jsp" />


    <%-- 즐겨찾기 국가 js 연결 --%>
    <script src="/js/favorites.js"></script>
</body>
</html>