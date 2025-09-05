document.addEventListener("DOMContentLoaded", function() {
    const consentCheck = document.getElementById("consentCheck");
    const startBtn = document.getElementById("startBtn");

    if (consentCheck && startBtn) {
        consentCheck.addEventListener("change", function() {
            startBtn.disabled = !this.checked;
        });

        startBtn.addEventListener("click", function() {
            if (!startBtn.disabled) {
                window.location.href = "/recommend/input";
            }
        });
    }
});