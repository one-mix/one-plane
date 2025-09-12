<%@ page contentType="text/html; charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Country Info Panel</title>
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
        <div class="info-body">

            <!-- GDP 차트 -->
            <div class="chart-section">
                <h3>GDP</h3>
                <canvas id="gdpChart"></canvas>
            </div>

            <!-- 탄소 배출량 차트 -->
            <div class="chart-section">
                <h3>탄소 배출량</h3>
                <canvas id="carbonChart"></canvas>
            </div>
        </div>
    </div>
</div>

<script>
// 전역 변수
let gdpChartInstance = null;
let currencyChartInstance = null;
let carbonChartInstance = null;

/** GDP 차트 로드 */
async function loadGdpCharts(countryId, countryName) {
    try {
        const years = [2020, 2021, 2022, 2023, 2024];
        const gdpValues = [];

        for (let year of years) {
            const res = await fetch("/api/countries/gdp/" + year + "/" + encodeURIComponent(countryId));
            const data = await res.json();
            console.log("loadGdpCharts data: ", data);
            gdpValues.push(data?.gdpRaw ?? null);
        }

        if (gdpChartInstance) gdpChartInstance.destroy();

        const ctx = document.getElementById("gdpChart").getContext("2d");
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
        document.getElementById("gdpChart").outerHTML = "<p>GDP 수집중입니다</p>";
    }
}

/** 탄소 배출량 차트 로드 */
async function loadCarbonChart(countryId) {
    if (!countryId) {
        document.getElementById("carbonChart").innerHTML = "<p>탄소 배출량 데이터 없음</p>";
        return;
    }

    try {
        const res = await fetch("/api/countries/carbon/" + encodeURIComponent(countryId));
        if (!res.ok) throw new Error("탄소 배출량 데이터 없음!!");
        const data = await res.json();

        if (!data || data.length === 0) {
            document.getElementById("carbonChart").innerHTML = "<p>탄소 데이터 없음 ㅠㅠ </p>";
            return;
        }

        const labels = data.map(d => d.year);
        const values = data.map(d => d.emission);

        if (carbonChartInstance) carbonChartInstance.destroy();

        const ctx = document.getElementById("carbonChart").getContext("2d");
        carbonChartInstance = new Chart(ctx, {
            type: "bar",
            data: {
                labels,
                datasets: [{
                    label: "탄소 배출량 (kt CO₂)",
                    data: values,
                    backgroundColor: "#1cc88a"
                }]
            },
            options: { responsive: true }
        });
    } catch (err) {
        console.error(err);
        document.getElementById("carbonChart").innerHTML = "<p>탄소 배출량 차트 로드 실패</p>";
    }
}


/** 통합 차트 렌더링 */
function renderCharts(countryData) {
    if (!countryData?.countryId) {
        console.warn("countryId 없음:", countryData);
        document.getElementById("gdpChart").outerHTML = "<p>GDP 데이터 없음</p>";
        document.getElementById("carbonChart").outerHTML = "<p>탄소 데이터 없음</p>";
        return;
    }

    loadGdpCharts(countryData.countryId, countryData.countryName);
    loadCarbonChart(countryData.countryId);
}

/** 패널 닫기 */
function closeInfoPanel() {
    document.getElementById("country-info-panel").classList.add("hidden");
}
</script>
</body>
</html>
