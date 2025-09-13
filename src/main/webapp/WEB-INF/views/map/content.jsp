<%@ page contentType="text/html; charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <link rel="stylesheet" href="/css/map/map.css" />
    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css" />
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <jsp:include page="./partials/search.jsp" />

    <div id="map" class="map">
        <jsp:include page="./partials/guideline.jsp" />
        <jsp:include page="./partials/info_panel.jsp" />
    </div>

    <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>
    <script>
        let map, geoLayer, marker, currentCountryId;
        const countryMap = {};

        /** 지도 초기화 */
        function initMap() {
            map = L.map("map", {
                minZoom: 2,
                maxZoom: 10,
                maxBounds: [[30, 70], [45, 70]],
                maxBoundsViscosity: 1.0
            }).setView([37.5665, 126.9780], 2);

            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; OpenStreetMap contributors'
            }).addTo(map);

            marker = L.marker([37.5665, 126.9780]).addTo(map).openPopup();
        }

        /** 여행경보 색상 */
        function getColor(level) {
            const styles = getComputedStyle(document.documentElement);
            switch(level) {
                case "여행유의": return styles.getPropertyValue("--semantic-success").trim();
                case "여행자제": return styles.getPropertyValue("--semantic-caution").trim();
                case "철수권고": return styles.getPropertyValue("--semantic-error").trim();
                case "여행금지": return styles.getPropertyValue("--gray-900").trim();
                default: return styles.getPropertyValue("--gray-100").trim();
            }
        }

        /** GeoJSON + 여행경보 로드 */
        async function loadGeoData() {
            try {
                const alertRes = await fetch("/alerts/all");
                const alertData = await alertRes.json();
                const alertMap = {};
                alertData.forEach(d => {
                    const iso = (d.country.isoCode || "").trim().toUpperCase();
                    if (iso) alertMap[iso] = d.levelValue;
                });

                const geoRes = await fetch("/geojson/custom.geo.json");
                const geoData = await geoRes.json();

                geoLayer = L.geoJson(geoData, {
                    style: feature => {
                        const iso = (feature.properties.iso_a3 || "").trim().toUpperCase();
                        return {
                            fillColor: getColor(alertMap[iso]),
                            weight: 1,
                            color: "white",
                            fillOpacity: 0.7,
                            interactive: true
                        };
                    },
                    onEachFeature: (feature, layer) => {
                        const iso = (feature.properties.iso_a3 || "").trim().toUpperCase();
                        const level = alertMap[iso] || "정보 없음";
                        const countryName = feature.properties.admin || feature.properties.name || feature.properties.sovereignt || "국가명 없음";
                        const popupText = countryName + " : " + level;

                        layer.on("click", e => L.popup().setLatLng(e.latlng).setContent(popupText).openOn(map));
                        layer.on("mouseover", e => L.popup().setLatLng(e.latlng).setContent(popupText).openOn(map));
                    }
                }).addTo(map);
            } catch (err) {
                console.error("GeoJSON 로드 실패:", err);
            }
        }

        /** 카테고리 필터 초기화 */
        function initCategoryFilter() {
            const continentMap = {
                "전체": null,
                "미주": ["North America", "South America"],
                "유럽": ["Europe"],
                "아주": ["Asia"],
                "중동": "MiddleEast",
                "아프리카": ["Africa"]
            };

            const middleEastCountries = ["SAU","IRN","IRQ","ISR","JOR","SYR","LBN","TUR","ARE","QAT","KWT","OMN","YEM","BHR","EGY"];

            document.querySelectorAll(".category-container").forEach(el => {
                el.addEventListener("click", () => {
                    const category = el.querySelector(".category-title").innerText.trim();
                    const selected = continentMap[category];

                    geoLayer.eachLayer(layer => {
                        const cont = layer.feature.properties.continent;
                        const iso = (layer.feature.properties.iso_a3 || "").trim().toUpperCase();
                        let match = false;

                        if (!selected) match = true;
                        else if (selected === "MiddleEast") match = middleEastCountries.includes(iso);
                        else if (Array.isArray(selected)) match = selected.includes(cont);
                        else match = cont === selected;

                        layer.setStyle({ fillOpacity: match ? 0.7 : 0.1 });
                    });
                });
            });
        }

        /** 국가 검색 */
        function initSearch() {
            const searchInput = document.getElementById("country-search");
            searchInput.addEventListener("change", async function () {
                const country = this.value.trim();
                if (!country) return;

                try {
                    const res = await fetch("https://nominatim.openstreetmap.org/search?country=" + encodeURIComponent(country) + "&format=json");
                    const data = await res.json();

                    if (data.length > 0) {
                        const { lat, lon } = data[0];
                        map.setView([lat, lon], 2);
                        marker.setLatLng([lat, lon]).unbindPopup().bindPopup("여기는" + country + "입니다.").openPopup();

                        const countryRes = await fetch("/countries/search?name=" + encodeURIComponent(country));
                        const countryData = await countryRes.json();
                        openInfoPanel(countryData);
                    } else {
                        alert("국가를 찾을 수 없습니다.");
                    }
                } catch (err) {
                    console.error("검색 오류", err);
                }
            });
        }

        /** 패널 열기 */
        function openInfoPanel(countryData) {
            document.getElementById("country-info-panel").classList.add("show");
            currentCountryId = countryData.countryId;

            document.getElementById("country-flag").src = countryData.img || `/images/gallery.png`;
            document.getElementById("country-name").innerText = countryData.countryName;
            document.getElementById("country-continent").innerText = countryData.continent;

            setTimeout(() => {
                loadGdpCharts(countryData.countryName);
                loadCarbonChart(countryData.countryName);
            }, 0);
        }

        function closeInfoPanel() {
            document.getElementById("country-info-panel").classList.remove("show");
        }

        /** 실행 */
        (async function init() {
            initMap();
            await loadGeoData();
            initCategoryFilter();
            initSearch();
        })();
    </script>

    <jsp:include page="./partials/category.jsp" />
    <jsp:include page="./partials/top_posts.jsp" />
    <jsp:include page="./partials/popular_reviews.jsp" />

    <script src="/js/map/favorites.js"></script>
</body>
</html>