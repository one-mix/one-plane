// 작성자: 오수경
// 즐겨찾기 국가 목록을 불러와 화면에 표시하고, 개별 삭제 기능을 제공하는 스크립트

// 1. 사용자의 즐겨찾기 목록 API 호출
fetch("/favorites/api/list/1")

  // 2. 서버 응답을 JSON으로 변환
  .then(response => response.json())

  // 3. 변환된 데이터 처리
  .then(data => {
    // 응답이 배열이 아니면(즐겨찾기가 없거나 오류 시) 종료
    if (!Array.isArray(data)) return;

    // 즐겨찾기 목록을 표시할 <ul id="favorites-list"> 선택 후 초기화
    const list = document.getElementById("favorites-list");
    list.innerHTML = "";

    // 4. 배열의 각 즐겨찾기 아이템을 <li>로 생성하여 DOM에 추가
    data.forEach(item => {
      const li = document.createElement("li");
      li.className = "tooltip-country"; // 스타일 클래스

      // 국가 이름 출력용 <span>
      const span = document.createElement("span");
      span.textContent = item.country.countryName;

      // 삭제 버튼(X)
      const removeBtn = document.createElement("button");
      removeBtn.textContent = "X";
      removeBtn.className = "remove-btn";

      // 5. 삭제 버튼 클릭 시 실행될 로직
      removeBtn.onclick = () => {
        if (item.favoritesCountryId) {
          // (1) 즐겨찾기 테이블에 존재하는 경우 → 즐겨찾기 삭제
          fetch("/favorites/remove", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `favoritesCountryId=${item.favoritesCountryId}&userId=${item.userId}`
          })
          .then(() => {
            li.remove(); // DOM에서 즉시 제거
            alert("즐겨찾기에서 삭제되었습니다.");
          });
        } else {
          // (2) 추천 국가(soft delete) 삭제
          fetch("/api/recommend/remove", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `recommendId=${item.recommendId}&userId=${item.userId}`
          })
          .then(() => {
            li.remove();
            alert("추천 국가에서 삭제되었습니다.");
          });
        }
      };

      // 6. li에 span(국가명)과 버튼을 자식으로 추가 후 목록에 삽입
      li.appendChild(span);
      li.appendChild(removeBtn);
      list.appendChild(li);
    });
  })

  // 7. 네트워크/파싱 오류 발생 시 콘솔에 에러 출력
  .catch(error => console.error("즐겨찾기 불러오기 실패:", error));
