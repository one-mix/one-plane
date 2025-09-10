import pandas as pd
import random

# 연령대별 비율 설정 (주어진 통계 기준)
age_distribution = [("~19", 14.56), ("20~29", 11.87), ("30~39", 13.5), ("40~49", 14.93), 
                    ("50~59", 16.76), ("60~", 28.38)]

# 성별 비율 설정
gender_distribution = [("M", 0.50), ("F", 0.50)]  # 남자 비율 50%, 여자 비율 50%
# 장애 여부 비율 설정
disability_distribution = [("N", 0.95), ("Y", 0.05)]  # 장애 없음이 95%, 장애 있음이 5%
# 질병 비율 설정
disease_distribution = [("N", 0.70), ("Y", 0.30)]  # 질병 없음이 70%, 질병 있음이 30%
# 약물 복용 여부 비율 설정
medication_distribution = [("N", 0.70), ("Y", 0.30)]  # 약물 복용 안 함이 70%, 약물 복용이 30%

# 사용자 데이터 생성
num_users = 15000
user_data = {
    "user_id": [i for i in range(1, num_users + 1)],
    "age": [],  # 나이 리스트
    "age_bucket": [],  # 나이 구간 리스트
    "gender": [random.choices([gender[0] for gender in gender_distribution], [gender[1] for gender in gender_distribution])[0] for _ in range(num_users)],
    "disability": [random.choices([disability[0] for disability in disability_distribution], [disability[1] for disability in disability_distribution])[0] for _ in range(num_users)],
    "disease": [random.choices([disease[0] for disease in disease_distribution], [disease[1] for disease in disease_distribution])[0] for _ in range(num_users)],
    "medication": [random.choices([medication[0] for medication in medication_distribution], [medication[1] for medication in medication_distribution])[0] for _ in range(num_users)]
}

# 나이 구간을 그대로 두고, ~19, 20~29 형식으로 추가하고, 실제 나이는 구간 내에서 랜덤으로 할당
for _ in range(num_users):
    age_bucket = random.choices([age[0] for age in age_distribution], [age[1] for age in age_distribution])[0]
    user_data["age_bucket"].append(age_bucket)
    
    # 나이대별 나이 랜덤 설정
    if age_bucket == "~19":
        user_data["age"].append(random.randint(1, 19))  # ~19세
    elif age_bucket == "20~29":
        user_data["age"].append(random.randint(20, 29))  # 20~29세
    elif age_bucket == "30~39":
        user_data["age"].append(random.randint(30, 39))  # 30~39세
    elif age_bucket == "40~49":
        user_data["age"].append(random.randint(40, 49))  # 40~49세
    elif age_bucket == "50~59":
        user_data["age"].append(random.randint(50, 59))  # 50~59세
    elif age_bucket == "60~":
        user_data["age"].append(random.randint(60, 80))  # 60대 이상

# DataFrame 생성 (원하는 순서로 컬럼 정렬)
users_df = pd.DataFrame(user_data)[["user_id", "age", "age_bucket", "gender", "disability", "disease", "medication"]]

# CSV 저장
users_df.to_csv("/Users/bangdaehyeog/Desktop/one-plane/one-plane-chatbot/users_test2.csv", index=False)