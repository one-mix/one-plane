document.addEventListener("DOMContentLoaded", function () {
    const feedbackModal = document.getElementById("feedbackModal");
    const closeModal = document.getElementById("closeModal");
    const submitFeedback = document.getElementById("submitFeedback");
    const stars = document.querySelectorAll(".star");
    const comment = document.getElementById("comment");
    const toast = document.getElementById("toast");
    const saveBtn = document.querySelector(".save");
    const cancelBtn = document.querySelector(".cancel");

    let selectedRating = 0;
    let selectedCountry = null;
    let savedRecommendId = null;

    // 카드 선택 시 저장 버튼 활성화
    document.querySelectorAll(".card").forEach(card => {
        card.addEventListener("click", () => {
            document.querySelectorAll(".card").forEach(c => c.classList.remove("selected"));
            card.classList.add("selected");
            selectedCountry = card.dataset.country; // countryIso3 값
            saveBtn.disabled = false;
        });
    });

    // 취소 → 메인으로 복귀
    cancelBtn.addEventListener("click", () => {
        window.location.href = "/recommend";
    });

    // 저장 버튼 → 국가 저장 후 모달 열기
    saveBtn.addEventListener("click", () => {
        if (!selectedCountry) {
            alert("저장할 국가를 선택하세요.");
            return;
        }
        fetch("/api/recommend/saveCountry", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                countryIso3: selectedCountry
            })
        })
            .then(res => res.json())
            .then(data => {
                console.log("국가 저장 완료:", data);
                savedRecommendId = data.recommendId; // 저장된 recommendId 보관

                // 2. 저장 성공 후 피드백 모달 열기
                feedbackModal.style.display = "flex";
            })
            .catch(err => {
                console.error("국가 저장 실패:", err);
                alert("국가 저장에 실패했습니다.");
            });
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

    // 피드백 제출
    submitFeedback.addEventListener("click", () => {
        if (!selectedCountry) {
            alert("저장할 국가를 선택하세요!");
            return;
        }
        if (!selectedRating) {
            alert("별점을 선택해주세요!");
            return;
        }

        const feedbackText = comment.value;

        fetch("/api/recommend/updateFeedback", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                recommendId: savedRecommendId,
                recommendRating: selectedRating,
                ratingContent: feedbackText
            })
        })
            .then(res => res.text())
            .then(msg => {
                console.log("피드백 저장:", msg);

                feedbackModal.style.display = "none";

                // 토스트 메시지 표시
                toast.textContent = "피드백이 제출되었습니다!";
                toast.classList.add("show");

                setTimeout(() => {
                    toast.classList.remove("show");
                }, 2000);

                // 토스트 닫힌 후 메인으로 이동
                setTimeout(() => {
                    window.location.href = "/recommend";
                }, 2500);
            })
            .catch(err => console.error("피드백 저장 실패:", err));
    });
});