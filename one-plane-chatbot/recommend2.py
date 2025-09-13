import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, roc_auc_score, classification_report
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import OneHotEncoder
from sklearn.ensemble import RandomForestClassifier
from sklearn.pipeline import Pipeline
from sklearn.impute import SimpleImputer
import joblib

USERS_CSV = "/Users/osuky/Downloads/TodoList/one-plane/one-plane-chatbot/users_test2.csv"
HIST_CSV  = "/Users/osuky/Downloads/TodoList/one-plane/one-plane-chatbot/travel_history_test2.csv"

users = pd.read_csv(USERS_CSV)
hist  = pd.read_csv(HIST_CSV)

# --- 타깃 ---
hist["target"] = (hist["rating"] >= 4).astype(int)

# --- 파생 특징(인기/선호) ---
# 1) 글로벌 인기
pos = hist[hist["rating"] >= 4]
pop_country = pos.groupby("country_iso3").size().rename("pop_country")
pop_ctx = pos.groupby(["country_iso3","purpose","companion"]).size().rename("pop_ctx")

# 2) 사용자 선호 (횟수 기준)
u_purpose = hist.groupby(["user_id","purpose"]).size().rename("u_purpose")
u_ctx = hist.groupby(["user_id","purpose","companion"]).size().rename("u_ctx")

# ✅ 3) 사용자 긍정 경험 (새로 추가)
u_purpose_pos = pos.groupby(["user_id","purpose"]).size().rename("u_purpose_pos")
u_ctx_pos     = pos.groupby(["user_id","purpose","companion"]).size().rename("u_ctx_pos")

# ✅ 긍정률 (전체 대비)
u_purpose_all = hist.groupby(["user_id","purpose"]).size().rename("u_purpose_all")
u_ctx_all     = hist.groupby(["user_id","purpose","companion"]).size().rename("u_ctx_all")

u_purpose_ratio = (u_purpose_pos / u_purpose_all).rename("u_purpose_ratio")
u_ctx_ratio     = (u_ctx_pos / u_ctx_all).rename("u_ctx_ratio")

# --- 사용자 특성 조인 ---
user_cols = ["user_id","age","age_bucket","gender","disability","disease","medication"]
use_cols  = [c for c in user_cols if c in users.columns]
feat = hist.merge(users[use_cols], on="user_id", how="left")

# --- 파생 특징 조인 ---
feat = feat.merge(pop_country, on="country_iso3", how="left")
feat = feat.merge(pop_ctx, on=["country_iso3","purpose","companion"], how="left")
feat = feat.merge(u_purpose, on=["user_id","purpose"], how="left")
feat = feat.merge(u_ctx, on=["user_id","purpose","companion"], how="left")

# ✅ 긍정 경험 조인
feat = feat.merge(u_purpose_pos, on=["user_id","purpose"], how="left")
feat = feat.merge(u_ctx_pos, on=["user_id","purpose","companion"], how="left")
feat = feat.merge(u_purpose_ratio, on=["user_id","purpose"], how="left")
feat = feat.merge(u_ctx_ratio, on=["user_id","purpose","companion"], how="left")

# 결측치 처리
for c in ["pop_country","pop_ctx","u_purpose","u_ctx","u_purpose_pos","u_ctx_pos","u_purpose_ratio","u_ctx_ratio"]:
    if c not in feat.columns: feat[c] = 0
feat = feat.fillna({c:0 for c in ["pop_country","pop_ctx","u_purpose","u_ctx","u_purpose_pos","u_ctx_pos","u_purpose_ratio","u_ctx_ratio"]})

# 로그 변환
feat["pop_country_log"]   = np.log1p(feat["pop_country"])
feat["pop_ctx_log"]       = np.log1p(feat["pop_ctx"])
feat["u_purpose_log"]     = np.log1p(feat["u_purpose"])
feat["u_ctx_log"]         = np.log1p(feat["u_ctx"])
feat["u_purpose_pos_log"] = np.log1p(feat["u_purpose_pos"])   # ✅ 추가
feat["u_ctx_pos_log"]     = np.log1p(feat["u_ctx_pos"])       # ✅ 추가

# --- 피처 목록 ---
cat_cols, num_cols = [], []
for c in ["age_bucket","gender","disability","disease","medication","purpose","companion","country_iso3"]:
    if c in feat.columns: cat_cols.append(c)
if "age" in feat.columns: num_cols.append("age")

# ✅ 새로 만든 수치 피처 추가
num_cols += ["pop_country_log","pop_ctx_log",
             "u_purpose_log","u_ctx_log",
             "u_purpose_pos_log","u_ctx_pos_log",
             "u_purpose_ratio","u_ctx_ratio"]

X = feat[cat_cols + num_cols].copy()
y = feat["target"].copy()

# --- 파이프라인 ---
cat_pipe = Pipeline([
    ("impute", SimpleImputer(strategy="constant", fill_value="미상")),
    ("ohe", OneHotEncoder(handle_unknown="ignore"))
])
num_pipe = Pipeline([
    ("impute", SimpleImputer(strategy="median"))
])
pre = ColumnTransformer([
    ("cat", cat_pipe, cat_cols),
    ("num", num_pipe, num_cols),
])

clf = RandomForestClassifier(
    n_estimators=400,
    max_depth=14,
    min_samples_leaf=10,
    class_weight=None,
    n_jobs=-1,
    random_state=42
)
pipe = Pipeline([("pre", pre), ("clf", clf)])

# --- 학습/평가 ---
X_train, X_val, y_train, y_val = train_test_split(
    X, y, test_size=0.25, random_state=42, stratify=y
)
pipe.fit(X_train, y_train)

val_proba = pipe.predict_proba(X_val)[:,1]
ths = np.linspace(0.2, 0.8, 61)
accs = [accuracy_score(y_val, (val_proba >= t).astype(int)) for t in ths]
best_t = ths[int(np.argmax(accs))]

y_pred_default = (val_proba >= 0.5).astype(int)
y_pred_best    = (val_proba >= best_t).astype(int)

print("ROC-AUC :", round(roc_auc_score(y_val, val_proba),4))
print("Acc@0.5:", round(accuracy_score(y_val, y_pred_default),4))
print(f"Acc@best_t({best_t:.3f}):", round(accuracy_score(y_val, y_pred_best),4))
print("\nReport@best_t:\n", classification_report(y_val, y_pred_best, digits=3))

# 저장
joblib.dump({"model": pipe, "threshold": float(best_t)}, "reco_tree.pkl")
print("Saved model -> reco_tree.pkl (with best threshold)")

# --- 추천 함수 ---
def recommend_for_user(user_id:int, purpose:str, companion:str, topk:int=10, exclude_visited:bool=True):
    u = users[users["user_id"]==user_id]
    if u.empty:
        raise ValueError(f"user_id {user_id} not found")
    urow = u.iloc[0:1].copy()

    # 후보군 생성
    cc = hist[["country_iso3","country_name_ko","city"]].drop_duplicates().copy()
    cc = cc.assign(purpose=purpose, companion=companion)

    for c in ["age","age_bucket","gender","disability","disease","medication"]:
        cc[c] = urow.iloc[0][c] if c in urow.columns else np.nan

    # 인기 피처
    pos = hist[hist["rating"] >= 4]
    pop_country = pos.groupby("country_iso3").size().rename("pop_country")
    pop_ctx     = pos.groupby(["country_iso3","purpose","companion"]).size().rename("pop_ctx")
    cc = cc.merge(pop_country, on="country_iso3", how="left")
    cc = cc.merge(pop_ctx, on=["country_iso3","purpose","companion"], how="left")
    cc[["pop_country","pop_ctx"]] = cc[["pop_country","pop_ctx"]].fillna(0)
    cc["pop_country_log"] = np.log1p(cc["pop_country"])
    cc["pop_ctx_log"]     = np.log1p(cc["pop_ctx"])

    # 개인 선호 (횟수)
    cnt_up = hist[(hist["user_id"]==user_id) & (hist["purpose"]==purpose)].shape[0]
    cnt_uc = hist[(hist["user_id"]==user_id) & (hist["purpose"]==purpose) & (hist["companion"]==companion)].shape[0]

    # ✅ 개인 긍정 경험
    cnt_up_pos = hist[(hist["user_id"]==user_id) & (hist["purpose"]==purpose) & (hist["rating"]>=4)].shape[0]
    cnt_uc_pos = hist[(hist["user_id"]==user_id) & (hist["purpose"]==purpose) & (hist["companion"]==companion) & (hist["rating"]>=4)].shape[0]

    ratio_up = cnt_up_pos / cnt_up if cnt_up>0 else 0
    ratio_uc = cnt_uc_pos / cnt_uc if cnt_uc>0 else 0

    cc["u_purpose_log"]     = np.log1p(cnt_up)
    cc["u_ctx_log"]         = np.log1p(cnt_uc)
    cc["u_purpose_pos_log"] = np.log1p(cnt_up_pos)  # ✅ 추가
    cc["u_ctx_pos_log"]     = np.log1p(cnt_uc_pos)  # ✅ 추가
    cc["u_purpose_ratio"]   = ratio_up              # ✅ 추가
    cc["u_ctx_ratio"]       = ratio_uc              # ✅ 추가

    # 후보 중 기방문지 제거
    if exclude_visited:
        visited = set(hist[(hist["user_id"]==user_id) &
                           (hist["purpose"]==purpose) &
                           (hist["companion"]==companion)][["country_iso3","city"]]
                      .itertuples(index=False, name=None))
        cc = cc[~cc.set_index(["country_iso3","city"]).index.isin(visited)].copy()

    # 모델 로드
    saved = joblib.load("reco_tree.pkl")
    model = saved["model"] if isinstance(saved, dict) else saved

    # 입력 피처 순서 동일하게 맞추기
    Xcand = cc[
        [c for c in ["age_bucket","gender","disability","disease","medication","purpose","companion","country_iso3"] if c in cc.columns]
        + [c for c in ["age","pop_country_log","pop_ctx_log",
                       "u_purpose_log","u_ctx_log",
                       "u_purpose_pos_log","u_ctx_pos_log",
                       "u_purpose_ratio","u_ctx_ratio"]]
    ]

    proba = model.predict_proba(Xcand)[:,1]

    # 인기 prior 보정
    ctx = hist[(hist["purpose"]==purpose) & (hist["companion"]==companion) & (hist["rating"]>=4)]
    pop2 = ctx.groupby(["country_iso3","city"]).size().rename("pop2")
    cc = cc.merge(pop2, on=["country_iso3","city"], how="left").fillna({"pop2":0})
    cc["pop2_norm"] = cc["pop2"] / (cc["pop2"].max() if cc["pop2"].max() else 1)
    alpha, beta = 0.85, 0.15
    cc["score"] = alpha * proba + beta * cc["pop2_norm"]

    ranked = cc.sort_values("score", ascending=False).reset_index(drop=True)

    # 국가 다양화
    from collections import defaultdict
    def diversify_by_country(df, topk=10, per_country=2):
        picked, counts = [], defaultdict(int)
        for _, r in df.iterrows():
            if counts[r["country_iso3"]] < per_country:
                picked.append(r)
                counts[r["country_iso3"]] += 1
            if len(picked) == topk: break
        return pd.DataFrame(picked)

    recs = diversify_by_country(ranked, topk=topk, per_country=2)
    return recs[["country_name_ko","country_iso3","city","score"]]
