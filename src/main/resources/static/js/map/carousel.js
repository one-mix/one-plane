// 작성자: 오수경
// 리뷰 카드 캐러셀 초기화 및 이벤트 핸들링

window.addEventListener("load", () => {
    // === 요소 선택 ===
    const track = document.querySelector(".carousel-track");      // 캐러셀 이동 트랙
    const prevBtn = document.querySelector(".carousel-btn.prev"); // 이전 버튼
    const nextBtn = document.querySelector(".carousel-btn.next"); // 다음 버튼
    const dots = document.querySelectorAll(".dot");               // 하단 인디케이터 점
    const cards = track.querySelectorAll(".review-card");         // 개별 카드 요소

    // 카드가 없으면 즉시 종료
    if (cards.length === 0) return;

    /**
     * 현재 화면 폭 기준으로 한 페이지에 표시할 카드 개수 반환
     * - 600px 미만 : 1장
     * - 900px 미만 : 2장
     * - 그 이상   : 3장
     */
    function getCardsPerPage() {
        if (window.innerWidth < 600) return 1;
        if (window.innerWidth < 900) return 2;
        return 3;
    }

    /**
     * 카드 1장의 실제 너비 + margin-right 계산
     * → translateX 계산 시 필요
     */
    function getCardWidth() {
        const style = getComputedStyle(cards[0]);
        const marginRight = parseInt(style.marginRight) || 0;
        return cards[0].offsetWidth + marginRight;
    }

    // === 상태 값 ===
    let cardsPerPage = getCardsPerPage();
    let cardWidth = getCardWidth();
    let totalPages = Math.ceil(cards.length / cardsPerPage); // 전체 페이지 수
    let currentPage = 0;                                     // 현재 페이지 index

    /**
     * 캐러셀 위치 및 인디케이터 갱신
     */
    function updateCarousel() {
        // 페이지 * 카드폭 * 페이지당 카드수 만큼 X축 이동
        const moveX = -(currentPage * cardWidth * cardsPerPage);
        track.style.transform = `translateX(${moveX}px)`;

        // 인디케이터 활성화 갱신
        dots.forEach((dot, idx) => {
            dot.classList.toggle("active", idx === currentPage);
        });
    }

    // === 이벤트 등록 ===
    // 다음 버튼: 마지막 페이지 전까지 이동
    nextBtn.addEventListener("click", () => {
        if (currentPage < totalPages - 1) {
            currentPage++;
            updateCarousel();
        }
    });

    // 이전 버튼: 첫 페이지 전까지 이동
    prevBtn.addEventListener("click", () => {
        if (currentPage > 0) {
            currentPage--;
            updateCarousel();
        }
    });

    // 인디케이터 점 클릭 시 해당 페이지로 이동
    dots.forEach((dot, idx) => {
        dot.addEventListener("click", () => {
            currentPage = idx;
            updateCarousel();
        });
    });

    // 화면 리사이즈 시 카드 개수·너비·총 페이지 재계산
    window.addEventListener("resize", () => {
        cardsPerPage = getCardsPerPage();
        cardWidth = getCardWidth();
        totalPages = Math.ceil(cards.length / cardsPerPage);
        if (currentPage >= totalPages) currentPage = totalPages - 1;
        updateCarousel();
    });

    // 초기 캐러셀 세팅
    updateCarousel();
});