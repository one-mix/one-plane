# 작성자: 방대혁
import pandas as pd
import random
import numpy as np
from datetime import datetime, timedelta

# 사용자 데이터 로드
users_df = pd.read_csv("/Users/bangdaehyeog/Desktop/one-plane/one-plane-chatbot/users_test2.csv")

# 국가 및 도시 데이터 정의 (제공받은 리스트 기반)
countries_data = [
    # 아시아git
    {"country_id": 1, "country_iso3": "JPN", "country_name_ko": "일본", "cities": ["도쿄", "오사카", "교토", "후쿠오카"]},
    {"country_id": 2, "country_iso3": "CHN", "country_name_ko": "중국", "cities": ["베이징", "상하이", "광저우", "선전"]},
    {"country_id": 3, "country_iso3": "HKG", "country_name_ko": "홍콩", "cities": ["홍콩"]},
    {"country_id": 4, "country_iso3": "TWN", "country_name_ko": "대만", "cities": ["타이베이", "타이중", "가오슝", "타이난"]},
    {"country_id": 5, "country_iso3": "SGP", "country_name_ko": "싱가포르", "cities": ["싱가포르"]},
    {"country_id": 6, "country_iso3": "MYS", "country_name_ko": "말레이시아", "cities": ["쿠알라룸푸르", "조지타운(페낭)", "코타키나발루", "말라카"]},
    {"country_id": 7, "country_iso3": "THA", "country_name_ko": "태국", "cities": ["방콕", "치앙마이", "푸켓", "파타야"]},
    {"country_id": 8, "country_iso3": "VNM", "country_name_ko": "베트남", "cities": ["하노이", "호찌민시", "다낭", "나트랑"]},
    {"country_id": 9, "country_iso3": "IDN", "country_name_ko": "인도네시아", "cities": ["자카르타", "덴파사르(발리)", "욕야카르타", "수라바야"]},
    {"country_id": 10, "country_iso3": "PHL", "country_name_ko": "필리핀", "cities": ["마닐라", "세부", "다바오", "보홀(타그빌라란)"]},
    {"country_id": 11, "country_iso3": "IND", "country_name_ko": "인도", "cities": ["델리", "뭄바이", "벵갈루루", "자이푸르"]},
    {"country_id": 12, "country_iso3": "LKA", "country_name_ko": "스리랑카", "cities": ["콜롬보", "캔디", "갈레", "누와라엘리야"]},
    {"country_id": 13, "country_iso3": "KHM", "country_name_ko": "캄보디아", "cities": ["씨엠립", "프놈펜", "시아누크빌", "바탐방"]},
    {"country_id": 14, "country_iso3": "LAO", "country_name_ko": "라오스", "cities": ["비엔티안", "루앙프라방", "방비엥", "팍세"]},
    {"country_id": 15, "country_iso3": "BRN", "country_name_ko": "브루나이", "cities": ["반다르스리브가완"]},
    {"country_id": 16, "country_iso3": "MMR", "country_name_ko": "미얀마", "cities": ["양곤", "만달레이", "바간", "네피도"]},
    
    # 오세아니아
    {"country_id": 17, "country_iso3": "AUS", "country_name_ko": "호주", "cities": ["시드니", "멜버른", "브리즈번", "퍼스"]},
    {"country_id": 18, "country_iso3": "NZL", "country_name_ko": "뉴질랜드", "cities": ["오클랜드", "웰링턴", "퀸스타운", "크라이스트처치"]},
    {"country_id": 19, "country_iso3": "FJI", "country_name_ko": "피지", "cities": ["난디", "수바", "라우토카"]},
    
    # 유럽
    {"country_id": 20, "country_iso3": "FRA", "country_name_ko": "프랑스", "cities": ["파리", "니스", "리옹", "스트라스부르"]},
    {"country_id": 21, "country_iso3": "ESP", "country_name_ko": "스페인", "cities": ["바르셀로나", "마드리드", "세비야", "발렌시아"]},
    {"country_id": 22, "country_iso3": "ITA", "country_name_ko": "이탈리아", "cities": ["로마", "밀라노", "피렌체", "베네치아"]},
    {"country_id": 23, "country_iso3": "DEU", "country_name_ko": "독일", "cities": ["베를린", "뮌헨", "프랑크푸르트", "함부르크"]},
    {"country_id": 24, "country_iso3": "CHE", "country_name_ko": "스위스", "cities": ["취리히", "루체른", "인터라켄", "제네바"]},
    {"country_id": 25, "country_iso3": "GBR", "country_name_ko": "영국", "cities": ["런던", "에든버러", "맨체스터", "바스"]},
    {"country_id": 26, "country_iso3": "NLD", "country_name_ko": "네덜란드", "cities": ["암스테르담", "로테르담", "헤이그(덴하흐)", "위트레흐트"]},
    {"country_id": 27, "country_iso3": "AUT", "country_name_ko": "오스트리아", "cities": ["빈", "잘츠부르크", "인스브루크", "할슈타트"]},
    {"country_id": 28, "country_iso3": "CZE", "country_name_ko": "체코", "cities": ["프라하", "체스키크룸로프", "브르노", "카를로비바리"]},
    {"country_id": 29, "country_iso3": "GRC", "country_name_ko": "그리스", "cities": ["아테네", "산토리니(티라)", "미코노스", "테살로니키"]},
    {"country_id": 30, "country_iso3": "PRT", "country_name_ko": "포르투갈", "cities": ["리스본", "포르투", "라고스", "코임브라"]},
    {"country_id": 31, "country_iso3": "IRL", "country_name_ko": "아일랜드", "cities": ["더블린", "골웨이", "코크", "킬라니"]},
    {"country_id": 32, "country_iso3": "BEL", "country_name_ko": "벨기에", "cities": ["브뤼셀", "브뤼헤", "겐트", "안트베르펜"]},
    {"country_id": 33, "country_iso3": "DNK", "country_name_ko": "덴마크", "cities": ["코펜하겐", "오르후스", "오덴세", "올보르"]},
    {"country_id": 34, "country_iso3": "NOR", "country_name_ko": "노르웨이", "cities": ["오슬로", "베르겐", "트롬쇠", "스타방에르"]},
    {"country_id": 35, "country_iso3": "SWE", "country_name_ko": "스웨덴", "cities": ["스톡홀름", "예테보리", "말뫼", "웁살라"]},
    {"country_id": 36, "country_iso3": "FIN", "country_name_ko": "핀란드", "cities": ["헬싱키", "로바니에미", "투르쿠", "탐페레"]},
    {"country_id": 37, "country_iso3": "POL", "country_name_ko": "폴란드", "cities": ["바르샤바", "크라쿠프", "그단스크", "브로츠와프"]},
    {"country_id": 38, "country_iso3": "HUN", "country_name_ko": "헝가리", "cities": ["부다페스트", "에거", "데브레첸", "세게드"]},
    {"country_id": 39, "country_iso3": "HRV", "country_name_ko": "크로아티아", "cities": ["자그레브", "두브로브니크", "스플리트", "자다르"]},
    {"country_id": 40, "country_iso3": "ISL", "country_name_ko": "아이슬란드", "cities": ["레이캬비크", "아쿠레이리", "비크", "케플라비크"]},
    {"country_id": 41, "country_iso3": "TUR", "country_name_ko": "튀르키예", "cities": ["이스탄불", "안탈리아", "카파도키아(괴레메)", "이즈미르"]},
    
    # 중동
    {"country_id": 42, "country_iso3": "ARE", "country_name_ko": "아랍에미리트", "cities": ["두바이", "아부다비", "샤르자", "라스알카이마"]},
    {"country_id": 43, "country_iso3": "QAT", "country_name_ko": "카타르", "cities": ["도하", "알와크라", "알코르"]},
    {"country_id": 44, "country_iso3": "SAU", "country_name_ko": "사우디아라비아", "cities": ["리야드", "제다", "메디나", "알코바르"]},
    {"country_id": 45, "country_iso3": "OMN", "country_name_ko": "오만", "cities": ["무스카트", "살랄라", "니즈와", "소하르"]},
    {"country_id": 46, "country_iso3": "JOR", "country_name_ko": "요르단", "cities": ["암만", "아카바", "이르비드", "와디무사(페트라)"]},
    {"country_id": 47, "country_iso3": "BHR", "country_name_ko": "바레인", "cities": ["마나마", "무하라크", "리파"]},
    {"country_id": 48, "country_iso3": "ISR", "country_name_ko": "이스라엘", "cities": ["텔아비브", "예루살렘", "하이파", "에일라트"]},
    {"country_id": 49, "country_iso3": "EGY", "country_name_ko": "이집트", "cities": ["카이로", "룩소르", "아스완", "샤름엘셰이크"]},
    {"country_id": 50, "country_iso3": "MAR", "country_name_ko": "모로코", "cities": ["마라케시", "카사블랑카", "페스", "라바트"]},
    {"country_id": 51, "country_iso3": "TUN", "country_name_ko": "튀니지", "cities": ["튀니스", "수스", "함마메트", "스팍스"]},
    
    # 북미
    {"country_id": 52, "country_iso3": "USA", "country_name_ko": "미국", "cities": ["뉴욕", "로스앤젤레스", "샌프란시스코", "라스베이거스"]},
    {"country_id": 53, "country_iso3": "CAN", "country_name_ko": "캐나다", "cities": ["밴쿠버", "토론토", "몬트리올", "캘거리"]},
    {"country_id": 54, "country_iso3": "MEX", "country_name_ko": "멕시코", "cities": ["칸쿤", "멕시코시티", "과달라하라", "로스카보스"]},
    {"country_id": 55, "country_iso3": "DOM", "country_name_ko": "도미니카공화국", "cities": ["푼타카나", "산토도밍고", "산티아고"]},
    {"country_id": 56, "country_iso3": "JAM", "country_name_ko": "자메이카", "cities": ["몬테고베이", "오초리오스", "킹스턴", "네그릴"]},
    {"country_id": 57, "country_iso3": "BHS", "country_name_ko": "바하마", "cities": ["나소", "프리포트", "조지타운(엑수마)"]},
    {"country_id": 58, "country_iso3": "CRI", "country_name_ko": "코스타리카", "cities": ["산호세", "리베리아", "라포르투나", "타마린도"]},
    
    # 남미
    {"country_id": 59, "country_iso3": "BRA", "country_name_ko": "브라질", "cities": ["리우데자네이루", "상파울루", "이구아수", "살바도르"]},
    {"country_id": 60, "country_iso3": "ARG", "country_name_ko": "아르헨티나", "cities": ["부에노스아이레스", "우수아이아", "멘도사", "바릴로체"]},
    {"country_id": 61, "country_iso3": "CHL", "country_name_ko": "칠레", "cities": ["산티아고", "산페드로데아타카마", "발파라이소", "푸에르토바라스"]},
    {"country_id": 62, "country_iso3": "PER", "country_name_ko": "페루", "cities": ["쿠스코", "리마", "아레키파", "푸노"]},
    {"country_id": 63, "country_iso3": "COL", "country_name_ko": "콜롬비아", "cities": ["보고타", "메데인", "카르타헤나", "칼리"]},
    
    # 아프리카
    {"country_id": 64, "country_iso3": "ZAF", "country_name_ko": "남아프리카공화국", "cities": ["케이프타운", "요하네스버그", "더반", "프리토리아"]},
    {"country_id": 65, "country_iso3": "KEN", "country_name_ko": "케냐", "cities": ["나이로비", "몸바사", "나쿠루", "키수무"]},
    {"country_id": 66, "country_iso3": "TZA", "country_name_ko": "탄자니아", "cities": ["다르에스살람", "아루샤", "잔지바르시티", "모시"]},
    {"country_id": 67, "country_iso3": "MUS", "country_name_ko": "모리셔스", "cities": ["포트루이스", "그랑바", "플리컨플락", "마에부르"]}
]

# 여행 목적 정의
purposes = ["휴식", "모험/체험", "문화체험", "출장/업무", "관광"]
# 동반자 정의
companions = ["혼자", "연인", "친구들", "가족", "단체"]

# 연령대별 선호도 패턴 정의
age_preferences = {
    "~19": {
        "purposes": {"휴식": 0.15, "모험/체험": 0.40, "문화체험": 0.18, "출장/업무": 0.02, "관광": 0.25},
        "companions": {"혼자": 0.05, "연인": 0.10, "친구들": 0.45, "가족": 0.35, "단체": 0.05},
        "rating_bias": 0.1
    },
    "20~29": {
        "purposes": {"휴식": 0.20, "모험/체험": 0.32, "문화체험": 0.23, "출장/업무": 0.05, "관광": 0.20},
        "companions": {"혼자": 0.15, "연인": 0.25, "친구들": 0.35, "가족": 0.15, "단체": 0.10},
        "rating_bias": 0.05
    },
    "30~39": {
        "purposes": {"휴식": 0.25, "모험/체험": 0.18, "문화체험": 0.24, "출장/업무": 0.15, "관광": 0.18},
        "companions": {"혼자": 0.10, "연인": 0.20, "친구들": 0.20, "가족": 0.40, "단체": 0.10},
        "rating_bias": -0.05
    },
    "40~49": {
        "purposes": {"휴식": 0.30, "모험/체험": 0.11, "문화체험": 0.19, "출장/업무": 0.20, "관광": 0.20},
        "companions": {"혼자": 0.08, "연인": 0.15, "친구들": 0.15, "가족": 0.50, "단체": 0.12},
        "rating_bias": -0.1
    },
    "50~59": {
        "purposes": {"휴식": 0.35, "모험/체험": 0.08, "문화체험": 0.22, "출장/업무": 0.10, "관광": 0.25},
        "companions": {"혼자": 0.05, "연인": 0.25, "친구들": 0.20, "가족": 0.45, "단체": 0.05},
        "rating_bias": -0.15
    },
    "60~": {
        "purposes": {"휴식": 0.40, "모험/체험": 0.04, "문화체험": 0.24, "출장/업무": 0.02, "관광": 0.30},
        "companions": {"혼자": 0.03, "연인": 0.35, "친구들": 0.25, "가족": 0.35, "단체": 0.02},
        "rating_bias": -0.2
    }
}

# 성별별 선호도 차이
gender_preferences = {
    "M": {
        "purposes": {"출장/업무": 1.3, "모험/체험": 1.3},
        "companions": {"혼자": 1.2, "단체": 1.3}
    },
    "F": {
        "purposes": {"모험/체험": 1.4, "문화체험": 1.2, "휴식": 1.1},
        "companions": {"친구들": 1.1, "가족": 1.1}
    }
}

# 장애/질병/약물 복용자의 여행 패턴
health_preferences = {
    "disability_Y": {
        "purposes": {"휴식": 1.5, "관광": 0.8, "모험/체험": 0.3},
        "companions": {"가족": 1.3, "혼자": 0.7},
        "rating_bias": -0.3,
        "travel_frequency": 0.6
    },
    "disease_Y": {
        "purposes": {"휴식": 1.3, "모험/체험": 0.6, "출장/업무": 0.8},
        "companions": {"가족": 1.2},
        "rating_bias": -0.2,
        "travel_frequency": 0.7
    },
    "medication_Y": {
        "purposes": {"휴식": 1.2, "모험/체험": 0.7},
        "rating_bias": -0.15,
        "travel_frequency": 0.8
    }
}

# 국가별 인기도 (한국인 여행 패턴 반영)
country_popularity = {
    # 아시아 (높은 인기)
    "JPN": 0.20,    # 일본 (가장 인기)
    "THA": 0.10,    # 태국
    "VNM": 0.08,    # 베트남
    "CHN": 0.05,    # 중국
    "TWN": 0.04,    # 대만
    "SGP": 0.04,    # 싱가포르
    "HKG": 0.03,    # 홍콩
    "MYS": 0.03,    # 말레이시아
    "IDN": 0.03,    # 인도네시아
    "PHL": 0.03,    # 필리핀
    "IND": 0.02,    # 인도
    "KHM": 0.015,   # 캄보디아
    "LAO": 0.01,    # 라오스
    "LKA": 0.01,    # 스리랑카
    "MMR": 0.005,   # 미얀마
    "BRN": 0.002,   # 브루나이
    
    # 오세아니아
    "AUS": 0.035,   # 호주
    "NZL": 0.015,   # 뉴질랜드
    "FJI": 0.005,   # 피지
    
    # 유럽 (중간 인기)
    "FRA": 0.04,    # 프랑스
    "ITA": 0.035,   # 이탈리아
    "ESP": 0.03,    # 스페인
    "GBR": 0.025,   # 영국
    "DEU": 0.02,    # 독일
    "CHE": 0.02,    # 스위스
    "AUT": 0.015,   # 오스트리아
    "CZE": 0.015,   # 체코
    "TUR": 0.02,    # 터키
    "GRC": 0.015,   # 그리스
    "NLD": 0.012,   # 네덜란드
    "PRT": 0.01,    # 포르투갈
    "BEL": 0.008,   # 벨기에
    "NOR": 0.01,    # 노르웨이
    "SWE": 0.008,   # 스웨덴
    "DNK": 0.008,   # 덴마크
    "FIN": 0.006,   # 핀란드
    "ISL": 0.008,   # 아이슬란드
    "IRL": 0.005,   # 아일랜드
    "POL": 0.005,   # 폴란드
    "HUN": 0.005,   # 헝가리
    "HRV": 0.008,   # 크로아티아
    
    # 중동
    "ARE": 0.025,   # 아랍에미리트
    "EGY": 0.012,   # 이집트
    "ISR": 0.005,   # 이스라엘
    "JOR": 0.008,   # 요르단
    "MAR": 0.006,   # 모로코
    "QAT": 0.005,   # 카타르
    "OMN": 0.003,   # 오만
    "SAU": 0.003,   # 사우디아라비아
    "BHR": 0.002,   # 바레인
    "TUN": 0.003,   # 튀니지
    
    # 북미 (중간 인기)
    "USA": 0.05,    # 미국
    "CAN": 0.025,   # 캐나다
    "MEX": 0.015,   # 멕시코
    "DOM": 0.005,   # 도미니카공화국
    "JAM": 0.003,   # 자메이카
    "BHS": 0.002,   # 바하마
    "CRI": 0.003,   # 코스타리카
    
    # 남미 (낮은 인기)
    "BRA": 0.008,   # 브라질
    "ARG": 0.005,   # 아르헨티나
    "PER": 0.005,   # 페루
    "CHL": 0.003,   # 칠레
    "COL": 0.003,   # 콜롬비아
    
    # 아프리카 (낮은 인기)
    "ZAF": 0.005,   # 남아프리카공화국
    "KEN": 0.003,   # 케냐
    "TZA": 0.003,   # 탄자니아
    "MUS": 0.003,   # 모리셔스
}

def generate_travel_history():
    travel_records = []
    record_id = 1
    
    for _, user in users_df.iterrows():
        user_id = user['user_id']
        age_bucket = user['age_bucket']
        gender = user['gender']
        disability = user['disability']
        disease = user['disease']
        medication = user['medication']
        
        # 사용자별 여행 빈도 결정 (건강 상태 고려)
        base_trips = random.randint(2, 15)
        
        # 건강 상태에 따른 여행 빈도 조정
        frequency_modifier = 1.0
        if disability == 'Y':
            frequency_modifier *= health_preferences["disability_Y"]["travel_frequency"]
        if disease == 'Y':
            frequency_modifier *= health_preferences["disease_Y"]["travel_frequency"]
        if medication == 'Y':
            frequency_modifier *= health_preferences["medication_Y"]["travel_frequency"]
        
        num_trips = max(1, int(base_trips * frequency_modifier))
        
        # 사용자의 선호도 패턴 가져오기
        age_pref = age_preferences[age_bucket]
        
        for trip in range(num_trips):
            # 날짜 생성 (최근 3년간)
            start_date = datetime.now() - timedelta(days=1095)
            random_days = random.randint(0, 1095)
            created_at = start_date + timedelta(days=random_days)
            
            # 국가 선택 (인기도 기반)
            country_choices = list(country_popularity.keys())
            country_weights = list(country_popularity.values())
            selected_country_iso3 = random.choices(country_choices, country_weights)[0]
            
            # 선택된 국가 정보 가져오기
            country_info = next(c for c in countries_data if c["country_iso3"] == selected_country_iso3)
            city = random.choice(country_info["cities"])
            
            # 목적 선택 (연령대, 성별, 건강상태 고려)
            purpose_probs = age_pref["purposes"].copy()
            
            # 성별 조정
            if gender in gender_preferences:
                for purpose, modifier in gender_preferences[gender]["purposes"].items():
                    if purpose in purpose_probs:
                        purpose_probs[purpose] *= modifier
            
            # 건강상태 조정
            if disability == 'Y':
                for purpose, modifier in health_preferences["disability_Y"]["purposes"].items():
                    if purpose in purpose_probs:
                        purpose_probs[purpose] *= modifier
            if disease == 'Y':
                for purpose, modifier in health_preferences["disease_Y"]["purposes"].items():
                    if purpose in purpose_probs:
                        purpose_probs[purpose] *= modifier
            if medication == 'Y':
                for purpose, modifier in health_preferences["medication_Y"]["purposes"].items():
                    if purpose in purpose_probs:
                        purpose_probs[purpose] *= modifier
            
            # 정규화
            total = sum(purpose_probs.values())
            purpose_probs = {k: v/total for k, v in purpose_probs.items()}
            
            selected_purpose = random.choices(list(purpose_probs.keys()), list(purpose_probs.values()))[0]
            
            # 동반자 선택 (연령대, 성별, 건강상태 고려)
            companion_probs = age_pref["companions"].copy()
            
            # 성별 조정
            if gender in gender_preferences:
                for companion, modifier in gender_preferences[gender]["companions"].items():
                    if companion in companion_probs:
                        companion_probs[companion] *= modifier
            
            # 건강상태 조정
            if disability == 'Y':
                for companion, modifier in health_preferences["disability_Y"]["companions"].items():
                    if companion in companion_probs:
                        companion_probs[companion] *= modifier
            if disease == 'Y':
                for companion, modifier in health_preferences["disease_Y"]["companions"].items():
                    if companion in companion_probs:
                        companion_probs[companion] *= modifier
            
            # 정규화
            total = sum(companion_probs.values())
            companion_probs = {k: v/total for k, v in companion_probs.items()}
            
            selected_companion = random.choices(list(companion_probs.keys()), list(companion_probs.values()))[0]
            
            # 평점 생성 (다양한 요소 고려)
            base_rating = 3.5  # 기본 평점
            
            # 연령대별 평점 경향
            base_rating += age_pref["rating_bias"]
            
            # 건강상태별 평점 경향
            if disability == 'Y':
                base_rating += health_preferences["disability_Y"]["rating_bias"]
            if disease == 'Y':
                base_rating += health_preferences["disease_Y"]["rating_bias"]
            if medication == 'Y':
                base_rating += health_preferences["medication_Y"]["rating_bias"]
            
            # 목적과 동반자 조합에 따른 만족도 조정
            purpose_companion_bonus = {
                ("휴식", "연인"): 0.3, ("휴식", "가족"): 0.2,
                ("관광", "친구들"): 0.2, ("관광", "가족"): 0.15,
                ("출장/업무", "혼자"): 0.1, ("출장/업무", "단체"): 0.1,
                ("모험/체험", "친구들"): 0.25, ("모험/체험", "연인"): 0.2,
                ("문화체험", "혼자"): 0.1, ("문화체험", "연인"): 0.15
            }
            
            if (selected_purpose, selected_companion) in purpose_companion_bonus:
                base_rating += purpose_companion_bonus[(selected_purpose, selected_companion)]
            
            # 국가별 만족도 조정 (인기 국가는 약간의 보너스)
            if country_popularity[selected_country_iso3] > 0.1:
                base_rating += 0.1
            
            # 랜덤 노이즈 추가
            base_rating += random.normalvariate(0, 0.4)
            
            # 1~5 범위로 제한하고 반올림
            rating = max(1, min(5, round(base_rating)))
            
            # 레코드 생성
            travel_records.append({
                "user_id": user_id,
                "country_id": country_info["country_id"],
                "country_iso3": selected_country_iso3,
                "country_name_ko": country_info["country_name_ko"],
                "city": city,
                "purpose": selected_purpose,
                "companion": selected_companion,
                "rating": rating,
                "created_at": created_at.strftime("%Y-%m-%d %H:%M:%S")
            })
            record_id += 1
    
    return travel_records

# 데이터 생성
print("여행 데이터 생성 중...")
travel_data = generate_travel_history()

# DataFrame 변환
travel_df = pd.DataFrame(travel_data)

# 생성된 데이터 통계 확인
print(f"\n생성된 여행 기록 수: {len(travel_df)}")
print(f"사용자당 평균 여행 횟수: {len(travel_df) / len(users_df):.1f}")
print(f"\n평점 분포:")
print(travel_df['rating'].value_counts().sort_index())
print(f"\n목적별 분포:")
print(travel_df['purpose'].value_counts())
print(f"\n동반자별 분포:")
print(travel_df['companion'].value_counts())
print(f"\n인기 국가 TOP 10:")
print(travel_df['country_name_ko'].value_counts().head(10))

# 지역별 분포 확인
asia_countries = ["일본", "중국", "홍콩", "대만", "싱가포르", "말레이시아", "태국", "베트남", "인도네시아", "필리핀", "인도", "스리랑카", "캄보디아", "라오스", "브루나이", "미얀마"]
europe_countries = ["프랑스", "스페인", "이탈리아", "독일", "스위스", "영국", "네덜란드", "오스트리아", "체코", "그리스", "포르투갈", "아일랜드", "벨기에", "덴마크", "노르웨이", "스웨덴", "핀란드", "폴란드", "헝가리", "크로아티아", "아이슬란드", "튀르키예"]

asia_count = len(travel_df[travel_df['country_name_ko'].isin(asia_countries)])
europe_count = len(travel_df[travel_df['country_name_ko'].isin(europe_countries)])

print(f"\n지역별 분포:")
print(f"아시아: {asia_count} ({asia_count/len(travel_df)*100:.1f}%)")
print(f"유럽: {europe_count} ({europe_count/len(travel_df)*100:.1f}%)")

# CSV 저장
travel_df.to_csv("/Users/bangdaehyeog/Desktop/one-plane/one-plane-chatbot/travel_history_test2.csv", index=False)
print(f"\n여행 데이터가 성공적으로 저장되었습니다!")

# 데이터 검증
print(f"\n데이터 검증:")
print(f"총 국가 수: {travel_df['country_name_ko'].nunique()}")
print(f"총 도시 수: {travel_df['city'].nunique()}")
print(f"날짜 범위: {travel_df['created_at'].min()} ~ {travel_df['created_at'].max()}")

# 연령대별 여행 패턴 확인
print(f"\n연령대별 평균 여행 횟수:")
age_travel_stats = travel_df.merge(users_df[['user_id', 'age_bucket']], on='user_id').groupby('age_bucket').size()
age_user_counts = users_df['age_bucket'].value_counts()
for age in age_travel_stats.index:
    avg_trips = age_travel_stats[age] / age_user_counts[age]
    print(f"{age}: {avg_trips:.1f}회")

print(f"\n건강 상태별 평균 여행 횟수:")
for health_col in ['disability', 'disease', 'medication']:
    health_travel_stats = travel_df.merge(users_df[['user_id', health_col]], on='user_id').groupby(health_col).size()
    health_user_counts = users_df[health_col].value_counts()
    for status in health_travel_stats.index:
        avg_trips = health_travel_stats[status] / health_user_counts[status]
        print(f"{health_col}_{status}: {avg_trips:.1f}회")