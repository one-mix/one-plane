// id가 1인 유저의 즐겨찾기 국가 목록  api 호출
fetch("/favorites/api/list/1")

  // json 형식으로 응답 반환
  .then(response => response.json())
  // data 반환
  .then(data => {
        console.log("API 응답:", data);
    // 즐겨찾기한 국가가 없을 경우
    if (!Array.isArray(data)) {
      console.error("즐겨찾기 API 응답이 배열이 아님:", data);
      return;
    }

    // 즐겨찾기한 국가가 있을 경우
    const list = document.getElementById("favorites-list");
    list.innerHTML = "";

    // 목록(ul)의 자식(li) 요소 추가해서 국가 이름 출력
    data.forEach(item => {
      const li = document.createElement("li");
      li.className = "tooltip-country";
      li.textContent = item.country.countryName;
      list.appendChild(li);
    });
  })

  // 그외의 경우 에러 로그 출력
  .catch(error => console.error("즐겨찾기 불러오기 실패:", error));
