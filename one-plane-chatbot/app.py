from flask import Flask, request, jsonify
from recommend2 import recommend_for_user

app = Flask(__name__)

@app.route("/recommend", methods=["POST"])
def recommend():
    data = request.json
    user_id = data.get("user", {}).get("user_id")
    purpose = data.get("input", {}).get("travelPurpose")
    companion = data.get("input", {}).get("companion")

    if not user_id or not purpose or not companion:
        return jsonify({"error": "필수값(user_id, purpose, companion) 누락"}), 400

    try:
        recs = recommend_for_user(user_id=user_id, purpose=purpose, companion=companion, topk=3)
        recs = recs.rename(columns={
            "country_name_ko": "countryNameKo",
            "country_iso3": "countryIso3"
        })
        
        return jsonify(recs.to_dict(orient="records"))
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001, debug=True)
