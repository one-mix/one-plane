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
                fetch("/api/recommend/check-login", { method: "GET" })
                    .then(res => res.json())
                    .then(data => {
                        if (!data.loggedIn) {
                            // 로그인되지 않으면 로그인 페이지로 리다이렉트
                            alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
                            window.location.href = "/oauth2/authorization/kakao";
                        } else {
                            // 동의 정보 서버에 전송
                            fetch("/api/recommend/agreement", {
                                method: "POST"
                            })
                                .then(res => res.text())
                                .then(msg => {
                                    console.log(msg);  // 서버 응답 메시지 출력
                                    window.location.href = "/recommend/input";  // 입력 페이지로 이동
                                })
                                .catch(err => console.error("동의 처리 실패:", err));
                        }
                    })
                    .catch(err => console.error("로그인 여부 확인 실패:", err));
            }
        });
    }
});