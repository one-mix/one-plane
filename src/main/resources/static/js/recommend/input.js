// 작성자: 방대혁

const steps = [
    {
        key: "travelPurpose",
        question: "이번 여행은 어떤 목적을 두고 계신가요?",
        options: [
            {label: "🌴 휴식", value: "휴식"},
            {label: "✨ 모험/체험", value: "모험"},
            {label: "🏛️ 문화 체험", value: "문화"},
            {label: "💼 출장/업무", value: "출장"},
            {label: "🏙️ 관광", value: "관광"}
        ]
    },
    {
        key: "companion",
        question: "이번 여행은 누구와 함께 가시나요?",
        options: [
            {label: "👤 혼자서", value: "혼자"},
            {label: "💑 연인과 함께", value: "연인"},
            {label: "🧑‍🤝‍🧑 친구와 함께", value: "친구"},
            {label: "👨‍👩‍👧 가족과 함께", value: "가족"},
            {label: "👥 단체로", value: "단체"}
        ]
    }
];

let currentStep = 0;
let answers = {};

function renderStep(stepIndex) {
    const questionEl = document.getElementById("question");
    const optionsEl = document.getElementById("options");

    questionEl.textContent = steps[stepIndex].question;
    optionsEl.innerHTML = "";

    steps[stepIndex].options.forEach(opt => {
        const btn = document.createElement("button");
        btn.type = "button";
        btn.className = "option";
        btn.textContent = opt.label;
        btn.onclick = () => {
            answers[steps[stepIndex].key] = opt.value;

            if (stepIndex < steps.length - 1) {
                currentStep = stepIndex + 1;
                renderStep(currentStep);
            } else {
                fetch("/api/recommend/input", {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify(answers)
                })
                    .then(res => res.text())
                    .then(msg => {
                        console.log("저장 완료:", msg);
                        window.location.href = "/recommend/loading";
                    })
                    .catch(err => console.error("저장 실패:", err));
            }
        };
        optionsEl.appendChild(btn);
    });
}

renderStep(currentStep);
