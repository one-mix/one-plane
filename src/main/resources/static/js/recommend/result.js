document.addEventListener("DOMContentLoaded", function () {
    const feedbackModal = document.getElementById("feedbackModal");
    const closeModal = document.getElementById("closeModal");
    const submitFeedback = document.getElementById("submitFeedback");
    const stars = document.querySelectorAll(".star");
    const comment = document.getElementById("comment");
    const toast = document.getElementById("toast");
    const saveBtn = document.querySelector(".save");

    let selectedRating = 0;

    // 카드 선택 시 저장 버튼 활성화
    document.querySelectorAll(".card").forEach(card => {
        card.addEventListener("click", () => {
            document.querySelectorAll(".card").forEach(c => c.classList.remove("selected"));
            card.classList.add("selected");
            saveBtn.disabled = false;
        });
    });

    // 취소/저장 버튼 → 모달 열기
    document.querySelector(".cancel").addEventListener("click", () => {
        feedbackModal.style.display = "flex";
    });
    saveBtn.addEventListener("click", () => {
        feedbackModal.style.display = "flex";
    });

    // 닫기 버튼
    closeModal.addEventListener("click", () => {
        feedbackModal.style.display = "none";
    });

    // 별점 선택
    stars.forEach((star, index) => {
        star.addEventListener("click", () => {
            selectedRating = index + 1;
            stars.forEach(s => s.classList.remove("selected"));
            for (let i = 0; i < selectedRating; i++) {
                stars[i].classList.add("selected");
            }
        });
    });

    // 제출 버튼
    submitFeedback.addEventListener("click", () => {
        const feedbackText = comment.value;
        console.log(`⭐ 평점: ${selectedRating}\n📝 코멘트: ${feedbackText}`);

        feedbackModal.style.display = "none";

        // 토스트 메시지 표시
        toast.textContent = "피드백이 제출되었습니다!";
        toast.classList.add("show");

        setTimeout(() => {
            toast.classList.remove("show");
        }, 2000);

        // 토스트 닫힌 후 페이지 이동
        setTimeout(() => {
            window.location.href = "/recommend";
        }, 2500);
    });
});
