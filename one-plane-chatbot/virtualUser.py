import pandas as pd
import random

# 연령대별 비율 설정 (주어진 통계 기준)
age_distribution = [("~19", 14.56), ("20~29", 11.87), ("30~39", 13.5), ("40~49", 14.93),
                    ("50~59", 16.76), ("60~", 28.38)]

# 성별 비율
gender_distribution = [("M", 0.50), ("F", 0.50)]
# 장애 여부 비율
disability_distribution = [("N", 0.95), ("Y", 0.05)]
# 질병 비율
disease_distribution = [("N", 0.70), ("Y", 0.30)]
# 약물 복용 여부 비율
medication_distribution = [("N", 0.70), ("Y", 0.30)]

# 사용자 수
num_users = 15000

# 사용자 데이터 생성
user_data = {
    "user_id": [i for i in range(1, num_users + 1)],
    "age": [],
    "age_bucket": [],
    "gender": [random.choices([g[0] for g in gender_distribution],
                              [g[1] for g in gender_distribution])[0] for _ in range(num_users)],
    "disability": [random.choices([d[0] for d in disability_distribution],
                                  [d[1] for d in disability_distribution])[0] for _ in range(num_users)],
    "disease": [random.choices([d[0] for d in disease_distribution],
                               [d[1] for d in disease_distribution])[0] for _ in range(num_users)],
    "medication": [random.choices([m[0] for m in medication_distribution],
                                  [m[1] for m in medication_distribution])[0] for _ in range(num_users)]
}

# 나이/연령대 채우기
for _ in range(num_users):
    age_bucket = random.choices([a[0] for a in age_distribution],
                                [a[1] for a in age_distribution])[0]
    user_data["age_bucket"].append(age_bucket)

    if age_bucket == "~19":
        user_data["age"].append(random.randint(1, 19))
    elif age_bucket == "20~29":
        user_data["age"].append(random.randint(20, 29))
    elif age_bucket == "30~39":
        user_data["age"].append(random.randint(30, 39))
    elif age_bucket == "40~49":
        user_data["age"].append(random.randint(40, 49))
    elif age_bucket == "50~59":
        user_data["age"].append(random.randint(50, 59))
    elif age_bucket == "60~":
        user_data["age"].append(random.randint(60, 80))

# DataFrame 생성
users_df = pd.DataFrame(user_data)[
    ["user_id", "age", "age_bucket", "gender", "disability", "disease", "medication"]
]

# CSV 저장
csv_path = "/Users/bangdaehyeog/Desktop/one-plane/one-plane-chatbot/users_test2.csv"
users_df.to_csv(csv_path, index=False)
print(f"사용자 데이터가 성공적으로 저장되었습니다! ({csv_path})")

# ✅ 생성된 데이터 통계 확인
print("\n생성된 사용자 데이터 통계:")
print(f"총 사용자 수: {len(users_df)}")
print("\n연령대 분포:")
print(users_df["age_bucket"].value_counts(normalize=True).sort_index().round(3))

print("\n성별 분포:")
print(users_df["gender"].value_counts(normalize=True).round(3))

print("\n장애 여부 분포:")
print(users_df["disability"].value_counts(normalize=True).round(3))

print("\n질병 여부 분포:")
print(users_df["disease"].value_counts(normalize=True).round(3))

print("\n약물 복용 여부 분포:")
print(users_df["medication"].value_counts(normalize=True).round(3))

print("\n연령 요약 통계:")
print(users_df["age"].describe())
