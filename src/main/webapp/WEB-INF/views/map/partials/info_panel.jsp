<%@ page contentType="text/html; charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <%-- CSS 연결 --%>
    <link rel="stylesheet" href="/css/map/map.css" />
</head>
<body>
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

        <!-- GDP 차트 -->
        <div class="chart-section">
            <span class="chart-section-title">국내 총생산</span>
            <div id="gdpChartWrapper">
                <div class="spinner"></div>
            </div>
        </div>

        <!-- 탄소 배출량 차트 -->
        <div class="chart-section">
            <span class="chart-section-title">탄소 배출량</span>
            <div id="carbonChartWrapper">
                <div class="spinner"></div>
            </div>
        </div>

        <!-- 저장하기 버튼 -->
        <button class="save-country" onclick="saveFavorite()">저장하기</button>

        <script>
        async function saveFavorite() {

            if (!currentCountryId) {
                alert("저장할 국가가 선택되지 않았습니다.");
                return;
            }

            const countryId = currentCountryId;
            const userId = 1;

            try {
                const res = await fetch("/favorites/add", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: new URLSearchParams({ countryId, userId })
                });

                const result = await res.text();
                if (result === "success") {
                    alert("즐겨찾기 국가로 저장되었습니다.");
                } else {
                    alert("저장 실패: " + result);
                }
            } catch (err) {
                console.error("즐겨찾기 국가 저장 실패:", err);
                alert("저장 중 오류가 발생했습니다.");
            }
        }
        </script>
    </div>
</div>

<script>
// 전역 변수
let gdpChartInstance = null;
let carbonChartInstance = null;

/** GDP 차트 로드 */
async function loadGdpCharts(countryId, countryName) {
    const wrapper = document.getElementById("gdpChartWrapper");
    wrapper.innerHTML = '<div class="spinner"></div><p class="loading-text">로딩중...</p>';

    try {
        const years = [2020, 2021, 2022, 2023, 2024];
        const gdpValues = [];

        for (let year of years) {
            const res = await fetch("/api/countries/gdp/" + year + "/" + encodeURIComponent(countryId));
            const data = await res.json();
            console.log("loadGdpCharts data: ", data);
            gdpValues.push(data?.gdpRaw ?? null);
        }

        wrapper.innerHTML = '<canvas id="gdpChart"></canvas>';
        const ctx = document.getElementById("gdpChart").getContext("2d");

        if (gdpChartInstance) gdpChartInstance.destroy();
        gdpChartInstance = new Chart(ctx, {
            type: "line",
            data: {
                labels: years,
                datasets: [{
                    label: "GDP",
                    data: gdpValues,
                    borderColor: "#4e73df",
                    fill: false
                }]
            },
        });
    } catch (err) {
        console.error("GDP 차트 로드 실패:", err);
        wrapper.innerHTML = "<p class='loading-text'>GDP 데이터 수집중 입니다.</p>";
    }
}

/** 탄소 배출량 차트 로드 */
async function loadCarbonChart(countryId, countryName) {
    const wrapper = document.getElementById("carbonChartWrapper");
    wrapper.innerHTML = '<div class="spinner"></div><p class="loading-text">로딩중...</p>';

    try {
        const years = [2020, 2021, 2022, 2023];
        const carbonValues = [];

        for (let year of years) {
            const res = await fetch("/countries/carbon/" + year + "/" + encodeURIComponent(countryId));
            const data = await res.json();
            console.log("loadCarbonChart data: ", data);
            const value = Array.isArray(data) && data.length > 0 ? data[0].VALUE : null;
            carbonValues.push(value);
        }

        wrapper.innerHTML = '<canvas id="carbonChart"></canvas>';
        const ctx = document.getElementById("carbonChart").getContext("2d");

        if (carbonChartInstance) carbonChartInstance.destroy();
        carbonChartInstance = new Chart(ctx, {
            type: "line",
            data: {
                labels: years,
                datasets: [{
                    label: "CO₂ Emissions",
                    data: carbonValues,
                    borderColor: "#e74a3b",
                    fill: false
                }]
            },
        });
    } catch (err) {
        console.error("탄소 배출량 차트 로드 실패:", err);
        wrapper.innerHTML = "<p class='loading-text'>탄소 데이터 수집중 입니다</p>";
    }
}

/** 통합 차트 렌더링 */
function renderCharts(countryData) {
    if (!countryData?.countryId) {
        console.warn("countryId 없음:", countryData);
        document.getElementById("gdpChartWrapper").innerHTML = "<p>GDP 데이터 없음</p>";
        document.getElementById("carbonChartWrapper").innerHTML = "<p>탄소 데이터 없음</p>";
        return;
    }

    loadGdpCharts(countryData.countryId, countryData.countryName);
    loadCarbonChart(countryData.countryId, countryData.countryName);
}

/** 패널 닫기 */
function closeInfoPanel() {
    document.getElementById("country-info-panel").classList.add("hidden");
}
</script>
</body>
</html>
