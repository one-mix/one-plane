<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>여행 목적 선택</title>
    <style>
        body {
            font-family: Pretendard, sans-serif;
            background: linear-gradient(to right, #eaf4ff 0%, #fffbe6 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .input {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            align-items: center;
            text-align: center;
            min-height: 100vh;
            padding: 3rem 2rem;
            box-sizing: border-box;
        }

        .input .top {
            margin-top: 3rem;
        }

        .plane-icon {
            width: 70px;
            margin-bottom: 1rem;
        }

        .input .top h2 {
            font-size: 1.6rem;
            font-weight: 600;
            margin: 0;
        }

        .input .bottom {
            margin-bottom: 6rem;
        }

        .question {
            margin-bottom: 1.8rem;
            font-size: 1.2rem;
            font-weight: 500;
            color: #444;
        }

        .options {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 1rem;
            max-width: 700px;
            margin: 0 auto;
        }

        .option {
            flex: 1 1 calc(33% - 1rem);
            min-width: 160px;
            max-width: 200px;
            padding: 1rem 2.5rem;
            border-radius: 12px;
            border: 1px solid #ddd;
            background: #fefefe;
            font-size: 1.05rem;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.25s ease;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .option:hover {
            background: #5a90d2;
            color: #fff;
            transform: scale(1.05);
            box-shadow: 0 6px 16px rgba(48, 96, 157, 0.25);
        }
    </style>
</head>
<body>
<main class="input">
    <div class="top">
        <img src="<c:url value='/images/airplane.png'/>" alt="AI 이미지" class="plane-icon">
        <h2>AI와 대화중</h2>
    </div>

    <div class="bottom">
        <p id="question" class="question"></p>
        <div id="options" class="options"></div>
    </div>
</main>

<script>
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
</script>
</body>
</html>
