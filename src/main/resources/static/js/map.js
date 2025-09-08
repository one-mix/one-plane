// 지도 초기화
const map = L.map('map').setView([20, 0], 2); // [위도, 경도], 줌 레벨

// 타일맵 (배경 지도)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

// 여행경보 단계 → 색상 매핑 함수
function getColor(level) {
  switch(level) {
    case "여행유의": return "#FEE33C"; // 노랑
    case "여행자제": return "#FAAD14"; // 주황
    case "철수권고": return "#FF4D4F"; // 빨강
    case "여행금지": return "#000000"; // 검정
    default: return "#D9D9D9";          // 정보 없음
  }
}

// 여행경보 데이터 가져오기
fetch("/alerts/all")  // 백엔드 API (ISO 코드, levelValue 내려줌)
  .then(res => res.json())
  .then(alertData => {
    const alertMap = {};
    alertData.forEach(d => {
      alertMap[d.country.isoCode] = d.levelValue;
    });

    // GeoJSON 파일 불러오기
    fetch("/geojson/custom.geo.json")
      .then(res => res.json())
      .then(geoData => {
        L.geoJson(geoData, {
          style: feature => ({
            fillColor: getColor(alertMap[feature.properties.iso_a3]),
            weight: 1,
            color: "white",
            fillOpacity: 0.7
          }),
          onEachFeature: (feature, layer) => {
            const iso = feature.properties.iso_a3;
            const level = alertMap[iso] || "정보 없음";
            layer.bindPopup(`${feature.properties.admin} : ${level}`);
          }
        }).addTo(map);
      });
  });
