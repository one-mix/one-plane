window.addEventListener("load", () => {
    const track = document.querySelector(".carousel-track");
    const prevBtn = document.querySelector(".carousel-btn.prev");
    const nextBtn = document.querySelector(".carousel-btn.next");
    const dots = document.querySelectorAll(".dot");
    const cards = track.querySelectorAll(".review-card");

    if (cards.length === 0) return;

    function getCardsPerPage() {
        if (window.innerWidth < 600) return 1;
        if (window.innerWidth < 900) return 2;
        return 3;
    }

    function getCardWidth() {
        const style = getComputedStyle(cards[0]);
        const marginRight = parseInt(style.marginRight) || 0;
        return cards[0].offsetWidth + marginRight;
    }

    let cardsPerPage = getCardsPerPage();
    let cardWidth = getCardWidth();
    let totalPages = Math.ceil(cards.length / cardsPerPage);
    let currentPage = 0;

    function updateCarousel() {
        const moveX = -(currentPage * cardWidth * cardsPerPage);
        track.style.transform = `translateX(${moveX}px)`;

        dots.forEach((dot, idx) => {
            dot.classList.toggle("active", idx === currentPage);
        });
    }

    nextBtn.addEventListener("click", () => {
        if (currentPage < totalPages - 1) {
            currentPage++;
            updateCarousel();
        }
    });

    prevBtn.addEventListener("click", () => {
        if (currentPage > 0) {
            currentPage--;
            updateCarousel();
        }
    });

    dots.forEach((dot, idx) => {
        dot.addEventListener("click", () => {
            currentPage = idx;
            updateCarousel();
        });
    });

    window.addEventListener("resize", () => {
        cardsPerPage = getCardsPerPage();
        cardWidth = getCardWidth();
        totalPages = Math.ceil(cards.length / cardsPerPage);
        if (currentPage >= totalPages) currentPage = totalPages - 1;
        updateCarousel();
    });

    updateCarousel(); // 초기 세팅
});