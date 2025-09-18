# 작성자: 방대혁
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, roc_auc_score, classification_report
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import OneHotEncoder
from sklearn.tree import DecisionTreeClassifier
from sklearn.pipeline import Pipeline
import joblib

USERS_CSV = "/Users/bangdaehyeog/Desktop/one-plane/one-plane-chatbot/users_test.csv"
HIST_CSV  = "/Users/bangdaehyeog/Desktop/one-plane/one-plane-chatbot/travel_history_test.csv"

users = pd.read_csv(USERS_CSV)
hist  = pd.read_csv(HIST_CSV)

# 1) 타깃: rating >= 4 → 1
hist["target"] = (hist["rating"] >= 4).astype(int)

# 2) 사용자 특성 붙이기
user_cols = ["user_id","age","age_bucket","gender","disability","disease","medication"]
use_cols  = [c for c in user_cols if c in users.columns]
feat = hist.merge(users[use_cols], on="user_id", how="left")

# 3) 피처 목록
cat_cols, num_cols = [], []
for c in ["age_bucket","gender","disability","disease","medication","purpose","companion","country_iso3","city"]:
    if c in feat.columns: cat_cols.append(c)
if "age" in feat.columns: num_cols.append("age")

X = feat[cat_cols + num_cols].copy()
y = feat["target"].copy()

# 4) 파이프라인(원핫 + 트리)
pre = ColumnTransformer([
    ("cat", OneHotEncoder(handle_unknown="ignore"), cat_cols),
    ("num", "passthrough", num_cols),
])
clf = DecisionTreeClassifier(random_state=42)
pipe = Pipeline([("pre", pre), ("clf", clf)])

# 5) 학습/평가
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.25, random_state=42, stratify=y
)
pipe.fit(X_train, y_train)
y_pred  = pipe.predict(X_test)
y_proba = pipe.predict_proba(X_test)[:,1]
print("Accuracy:", round(accuracy_score(y_test, y_pred),4))
print("ROC-AUC :", round(roc_auc_score(y_test, y_proba),4))
print("\nReport:\n", classification_report(y_test, y_pred, digits=3))

joblib.dump(pipe, "reco_tree.pkl")
print("Saved model -> reco_tree.pkl")

# 6) 추천 함수 (히스토리 기반 후보)
def recommend_for_user(user_id:int, purpose:str, companion:str, topk:int=10, exclude_visited:bool=True):
    u = users[users["user_id"]==user_id]
    if u.empty:
        raise ValueError(f"user_id {user_id} not found")
    urow = u.iloc[0:1].copy()

    # 후보: 히스토리에 등장한 (국가-도시)만
    cc = hist[["country_iso3","country_name_ko","city"]].drop_duplicates().copy()
    cc = cc.assign(purpose=purpose, companion=companion)

    # 유저 특성 브로드캐스트
    for c in ["age","age_bucket","gender","disability","disease","medication"]:
        if c in urow.columns:
            cc[c] = urow.iloc[0][c]

    # 같은 컨텍스트로 이미 방문한 곳 제외(옵션)
    if exclude_visited:
        visited = set(hist[(hist["user_id"]==user_id) &
                           (hist["purpose"]==purpose) &
                           (hist["companion"]==companion)][["country_iso3","city"]]
                      .itertuples(index=False, name=None))
        cc = cc[~cc.set_index(["country_iso3","city"]).index.isin(visited)].copy()
        if cc.empty:
            # 후보가 비면 제외옵션 해제하고 전체로 재시도
            cc = hist[["country_iso3","country_name_ko","city"]].drop_duplicates().copy()
            cc = cc.assign(purpose=purpose, companion=companion)
            for c in ["age","age_bucket","gender","disability","disease","medication"]:
                if c in urow.columns:
                    cc[c] = urow.iloc[0][c]

    model = joblib.load("reco_tree.pkl")
    Xcand = cc[cat_cols + num_cols]
    proba = model.predict_proba(Xcand)[:,1]
    cc = cc.assign(score=proba)
    recs = cc.sort_values("score", ascending=False).head(topk).reset_index(drop=True)
    return recs[["country_name_ko","country_iso3","city","score"]]
