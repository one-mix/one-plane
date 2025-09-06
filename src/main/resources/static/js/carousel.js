document.addEventListener("DOMContentLoaded", () => {
    const track = document.querySelector(".carousel-track");
    const prevBtn = document.querySelector(".carousel-btn.prev");
    const nextBtn = document.querySelector(".carousel-btn.next");
    const dots = document.querySelectorAll(".dot");

    /*
    * rack.querySelector(".review-card"): .review-card 첫 번째 요소 선택
    * .offsetWidth: 해당 카드의 실제 렌더링된 너비 (padding 포함, margin 제외)
    * + 24: 카드와 카드 사이 간격 더함
    */
    const cardWidth = track.querySelector(".review-card").offsetWidth + 24; // 카드 너비 + gap

    let cardsPerPage = getCardsPerPage();
    let currentIndex = 0;

    function getCardsPerPage() {
        if (window.innerWidth < 600) return 1;
        if (window.innerWidth < 900) return 2;
        return 3;
    }

    function updateCarousel() {
        const moveX = -(currentIndex * cardWidth * cardsPerPage);
        track.style.transform = `translateX(${moveX}px)`;

        dots.forEach((dot, idx) => {
            dot.classList.toggle("active", idx === currentIndex);
        });
    }

    nextBtn.addEventListener("click", () => {
        if (currentIndex < dots.length - 1) {
            currentIndex++;
            updateCarousel();
        }
    });

    prevBtn.addEventListener("click", () => {
        if (currentIndex > 0) {
            currentIndex--;
            updateCarousel();
        }
    });

    dots.forEach((dot, idx) => {
        dot.addEventListener("click", () => {
            currentIndex = idx;
            updateCarousel();
        });
    });

    window.addEventListener("resize", () => {
        cardsPerPage = getCardsPerPage();
        updateCarousel();
    });

    updateCarousel(); // 초기 세팅
});
