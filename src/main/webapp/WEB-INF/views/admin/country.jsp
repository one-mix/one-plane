<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="row mb-4">
    <!-- 전체 -->
    <div class="col-md-4">
        <div class="card shadow-sm p-3 d-flex flex-column justify-content-between">
            <div class="d-flex align-items-center">
                <div class="stat-line bg-primary me-2"></div>
                <h6 class="mb-0 text-muted">전체</h6>
            </div>
            <h2 id="totalCountries" class="fw-bold my-2">N</h2>
        </div>
    </div>

    <!-- 안전 -->
    <div class="col-md-4">
        <div class="card shadow-sm p-3 d-flex flex-column justify-content-between">
            <div class="d-flex align-items-center">
                <div class="stat-line bg-success me-2"></div>
                <h6 class="mb-0 text-muted">안전</h6>
            </div>
            <h2 id="safeCountries" class="fw-bold my-2">N</h2>
        </div>
    </div>

    <!-- 금지 -->
    <div class="col-md-4">
        <div class="card shadow-sm p-3 d-flex flex-column justify-content-between">
            <div class="d-flex align-items-center">
                <div class="stat-line bg-danger me-2"></div>
                <h6 class="mb-0 text-muted">금지</h6>
            </div>
            <h2 id="bannedCountries" class="fw-bold my-2">N</h2>
        </div>
    </div>
</div>

<div class="content row">
    <!-- 파이차트 -->
    <div class="col-md-6">
        <div class="chart-box equal-height">
            <h7>대륙별 여행 비중</h7>
            <canvas id="continentPie"></canvas>
        </div>
    </div>

    <!-- 라인차트 -->
    <div class="col-md-6">
        <div class="chart-box equal-height">
            <h7>월별 여행 추이</h7>
            <canvas id="monthlyLine"></canvas>
        </div>
    </div>
</div>

<div class="content row">
    <div class="col-md-6">
        <div class="chart-box">
            <h7>추천 TOP 10 국가</h7>
            <canvas id="topRecommendBar"></canvas>
        </div>
    </div>
    <div class="col-md-6">
        <div class="chart-box">
            <h7>즐겨찾기 TOP 10 국가</h7>
            <canvas id="topFavoriteBar"></canvas>
        </div>
    </div>
</div>

<script>
    // ✅ 대륙별 파이차트
    fetch("/admin/continent")
        .then(response => response.json())
        .then(data => {
            const labels = data.map(d => d.continentName);
            const values = data.map(d => d.travelCount);

            new Chart(document.getElementById("continentPie"), {
                type: "pie",
                data: {
                    labels: labels,
                    datasets: [{
                        label: "여행 횟수",
                        data: values,
                        backgroundColor: [
                            "#36A2EB", "#FF6384", "#FFCE56",
                            "#4BC0C0", "#9966FF", "#FF9F40"
                        ]
                    }]
                },
                options: {
                    maintainAspectRatio: false, // ✅ 높이 강제 적용 가능하게
                    responsive: true,
                    layout: {
                        padding: {
                            bottom: 20, // ✅ 라벨이 잘리지 않게 아래쪽 여백 확보
                            top: 10
                        }
                    },
                    plugins: {
                        legend: {position: "bottom"}
                    }
                }
            });
        });

    // ✅ 월별 라인차트
    fetch("/admin/monthly")
        .then(response => response.json())
        .then(data => {
            const labels = data.map(d => d.month);
            const values = data.map(d => d.travelCount);

            new Chart(document.getElementById("monthlyLine"), {
                type: "line",
                data: {
                    labels: labels,
                    datasets: [{
                        label: "여행 횟수",
                        data: values,
                        borderColor: "#36A2EB",
                        backgroundColor: "rgba(54,162,235,0.2)",
                        fill: true,
                        tension: 0.3
                    }]
                },
                options: {
                    maintainAspectRatio: false, // ✅ 높이 강제 적용 가능하게
                    responsive: true,
                    layout: {
                        padding: {
                            bottom: 20, // ✅ 라벨이 잘리지 않게 아래쪽 여백 확보
                            top: 10
                        }
                    },
                    plugins: {
                        legend: {position: "bottom"}
                    },
                    scales: {
                        y: {beginAtZero: true}
                    }
                }
            });
        });

    fetch("/admin/top-recommend")
        .then(response => response.json())
        .then(data => {
            const labels = data.map(d => d.countryName);
            const values = data.map(d => d.count);

            new Chart(document.getElementById("topRecommendBar"), {
                type: "bar",
                data: {
                    labels: labels,
                    datasets: [{
                        label: "추천 수",
                        data: values,
                        backgroundColor: "#36A2EB"
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {display: false}
                    },
                    scales: {
                        y: {beginAtZero: true}
                    }
                }
            });
        });

    // 즐겨찾기 TOP 10
    fetch("/admin/top-favorite")
        .then(response => response.json())
        .then(data => {
            const labels = data.map(d => d.countryName);
            const values = data.map(d => d.count);

            new Chart(document.getElementById("topFavoriteBar"), {
                type: "bar",
                data: {
                    labels: labels,
                    datasets: [{
                        label: "즐겨찾기 수",
                        data: values,
                        backgroundColor: "#FF6384"
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {display: false}
                    },
                    scales: {
                        y: {beginAtZero: true}
                    }
                }
            });
        });

    fetch("/admin/country-summary")
        .then(res => res.json())
        .then(data => {
            document.getElementById("totalCountries").textContent = data.totalCountries;
            document.getElementById("safeCountries").textContent = data.safeCountries;
            document.getElementById("bannedCountries").textContent = data.bannedCountries;
        });
</script>

<style>
    .chart-box {
        width: 100%;
        margin: 0.5rem auto;
        background: #fff;
        border-radius: 12px;
        padding: 1rem;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
    }

    .equal-height {
        height: 40vh; /* 원하는 높이 */
    }

    .equal-height canvas {
        height: 100% !important;
    }

    .chart-box h7 {
        display: block; /* 블록요소로 변경 */
        font-weight: 600; /* 굵게 */
        padding-bottom: 20px; /* 제목과 차트 사이 간격 */
    }

    .stat-line {
        width: 4px;
        height: 24px;
        border-radius: 2px;
    }
    .card h2 {
        font-size: 1.8rem;
    }
</style>
