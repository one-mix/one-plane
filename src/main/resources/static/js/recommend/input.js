const steps = [
    {
        question: "이번 여행은 어떤 목적을 두고 계신가요?",
        options: ["🌴 휴식", "🏙️ 관광", "🏛️ 문화 체험", "💼 출장/업무", "✨ 모험/체험"]
    },
    {
        question: "이번 여행은 누구와 함께 가시나요?",
        options: ["👤 혼자서", "👨‍👩‍👧 가족과 함께", "🧑‍🤝‍🧑 친구와 함께", "💑 연인과 함께", "👥 단체로"]
    }
];

let currentStep = 0;

function renderStep(stepIndex) {
    const questionEl = document.getElementById("question");
    const optionsEl = document.getElementById("options");

    questionEl.textContent = steps[stepIndex].question;
    optionsEl.innerHTML = "";

    steps[stepIndex].options.forEach(opt => {
        const btn = document.createElement("button");
        btn.type = "button";
        btn.className = "option";
        btn.textContent = opt;
        btn.onclick = () => {
            if (stepIndex < steps.length - 1) {
                currentStep = stepIndex + 1;
                renderStep(currentStep);
            } else {
                window.location.href = "/recommend/loading";
            }
        };
        optionsEl.appendChild(btn);
    });
}

renderStep(currentStep);
