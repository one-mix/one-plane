<%@ page contentType="text/html; charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
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
                <h3>GDP</h3>
                <canvas id="travelChart"></canvas>
                <p id="gdp-message" style="color: gray; font-size: 14px; margin-top: 8px;"></p>
                <script>
                    async function loadGdpCharts(countryName) {
                        const currentYear = new Date().getFullYear();
                        const startYear = 2020;
                        const endYear = currentYear - 1; // 올해 전 해까지만

                        const gdpData = {};
                        for (let y = startYear; y <= endYear; y++) {
                            const response = await fetch("/api/countries/gdp/" + y + "/" + encodeURIComponent(countryName));
                            const data = await response.json();
                            if (data[countryName]) {
                                gdpData[y] = data[countryName];
                            }
                        }

                        const labels = Object.keys(gdpData);
                        const values = Object.values(gdpData);

                        if (labels.length === 0) {
                            document.getElementById("gdp-message").innerText = "GDP 수집중입니다.";
                            return;
                        } else {
                            document.getElementById("gdp-message").innerText = "";
                        }

                        const ctx = document.getElementById("travelChart").getContext("2d");
                        if (travelChartInstance) {
                            travelChartInstance.destroy();
                        }
                        travelChartInstance = new Chart(ctx, {
                            type: "line",
                            data: {
                                labels: labels,
                                datasets: [{
                                    label: countryName + " GDP",
                                    data: values,
                                    borderColor: "#36A2EB",
                                    fill: false
                                }]
                            },
                            options: {
                                plugins: {
                                    title: {
                                        display: true,
                                        text: `${countryName} GDP (2020 ~ ${endYear})`
                                    }
                                }
                            }
                        });
                    }
                </script>

                <h3>방문객</h3>
                <canvas id="visitChart"></canvas>

                <h3>환율</h3>
                <canvas id="currencyChart"></canvas>
                <script>
                    async function loadCurrencyChart(countryId) {
                        const response = await fetch("/fx/" + countryId);
                        const data = await response.json();

                        console.log("환율 api 응답:", data)

                        if (!Array.isArray(data) || data.length === 0) {
                            console.warn("환율 데이터 없음");
                            return;
                        }

                        const labels = data.map(r => r.baseDate);
                        const values = data.map(r => r.dealBasR);

                        // 이미 차트가 있으면 제거
                        if (currencyChartInstance) {
                            currencyChartInstance.destroy();
                        }

                        const ctx = document.getElementById("currencyChart").getContext("2d");
                        currencyChartInstance = new Chart(ctx, {
                            type: "line",
                            data: {
                                labels: labels,
                                datasets: [{
                                    label: "환율",
                                    data: values,
                                    borderColor: "#30609D",
                                    fill: false,
                                    tension: 0.1
                                }]
                            }
                        });
                    }
                </script>
            </div>
        </div>
</body>
</html>