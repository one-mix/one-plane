document.addEventListener("DOMContentLoaded", function() {
    const consentCheck = document.getElementById("consentCheck");
    const startBtn = document.getElementById("startBtn");

    fetch("/api/recommend/agreement", { method: "GET" })
        .then(res => res.text())
        .then(agreement => {
            if (agreement === "Y") {
                // 이미 동의한 사용자 → 바로 input 페이지로 이동
                window.location.href = "/recommend/input";
            }
        })
        .catch(err => console.error("동의 여부 확인 실패:", err));

    if (consentCheck && startBtn) {
        // 체크박스 동의 여부에 따라 버튼 활성/비활성
        consentCheck.addEventListener("change", function() {
            startBtn.disabled = !this.checked;
        });

        // 시작하기 버튼 클릭
        startBtn.addEventListener("click", function() {
            if (!startBtn.disabled) {
                fetch("/api/recommend/agreement", {
                    method: "POST"
                })
                    .then(res => res.text())
                    .then(msg => {
                        console.log(msg);
                        window.location.href = "/recommend/input";
                    });
            }
        });
    }
});
