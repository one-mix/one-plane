// id가 1인 유저의 즐겨찾기 국가 목록  api 호출
fetch("/favorites/api/list/1")

  // json 형식으로 응답 반환
  .then(response => response.json())
  // data 반환
  .then(data => {
    // 즐겨찾기한 국가가 없을 경우
    if (!Array.isArray(data)) {
      return;
    }

    // 즐겨찾기한 국가가 있을 경우
    const list = document.getElementById("favorites-list");
    list.innerHTML = "";

    // 목록(ul)의 자식(li) 요소 추가해서 국가 이름 출력
    data.forEach(item => {
      const li = document.createElement("li");
      li.className = "tooltip-country";

      // 국가명
      const span = document.createElement("span");
      span.textContent = item.country.countryName;

      // X 버튼
      const removeBtn = document.createElement("button");
      removeBtn.textContent = "X";
      removeBtn.className = "remove-btn";

        removeBtn.onclick = () => {
          if (item.favoritesCountryId) {
            // 즐겨찾기 삭제
            fetch("/favorites/remove", {
              method: "POST",
              headers: { "Content-Type": "application/x-www-form-urlencoded" },
              body: `favoritesCountryId=${item.favoritesCountryId}&userId=${item.userId}`
            })
            .then(() => {
              li.remove();
              alert("즐겨찾기에서 삭제되었습니다.");
            });
          } else {
            // 추천 삭제 (soft delete)
            fetch("/api/recommend/remove", {
              method: "POST",
              headers: {
                "Content-Type": "application/x-www-form-urlencoded"
              },
              body: `recommendId=${item.recommendId}&userId=${item.userId}`
            })
            .then(() => {
              li.remove();
              alert("추천 국가에서 삭제되었습니다.");
            });
          }
        };

      // 자식 요소로 추가
      li.appendChild(span);
      li.appendChild(removeBtn);
      list.appendChild(li);
    });
  })

  // 그외의 경우 에러 로그 출력
  .catch(error => console.error("즐겨찾기 불러오기 실패:", error));
