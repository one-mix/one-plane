document.addEventListener("DOMContentLoaded", () => {
    const track = document.querySelector(".carousel-track");

    fetch("/api/countries/all")
        .then(res => res.json())
        .then(data => {
            track.innerHTML = ""; // 기존 카드 초기화

            data.forEach(country => {
                const card = document.createElement("div");
                card.classList.add("review-card");

                card.innerHTML = `
                    <img src="${country.img}" alt="${country.countryName}" class="country-img"/>
                    <h3>${country.countryName}</h3>
                    <p>${country.continent}</p>
                `;

                track.appendChild(card);
            });

            initCarousel(); // 데이터 로딩 후 캐러셀 초기화
        })
        .catch(err => console.error("국가 데이터 불러오기 실패:", err));

    function initCarousel() {
        const prevBtn = document.querySelector(".carousel-btn.prev");
        const nextBtn = document.querySelector(".carousel-btn.next");
        const dots = document.querySelectorAll(".dot");
        const cards = track.querySelectorAll(".review-card");

        const cardWidth = cards[0].offsetWidth + 40;
        let cardsPerPage = getCardsPerPage();
        let totalPages = Math.ceil(cards.length / cardsPerPage);
        let currentPage = 0;

        function getCardsPerPage() {
            if (window.innerWidth < 600) return 1;
            if (window.innerWidth < 900) return 2;
            return 3;
        }

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
            totalPages = Math.ceil(cards.length / cardsPerPage);
            if (currentPage >= totalPages) currentPage = totalPages - 1;
            updateCarousel();
        });

        updateCarousel();
    }
});
