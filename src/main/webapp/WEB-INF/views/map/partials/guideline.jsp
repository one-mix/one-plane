<%@ page contentType="text/html; charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
</head>
<body>
                <div class="guideline-buttons">

                   <!-- 즐겨찾기 -->
                   <div class="tooltip-container">
                       <button class="save">
                           <span>즐겨찾기</span>
                       </button>
                       <div class="tooltip-content scrollable">
                           <ul id="favorites-list">
                               <!-- JS로 <li> 자동 생성 -->
                           </ul>
                       </div>
                   </div>

                   <%-- 행동지침 --%>
                   <div class="tooltip-container">
                       <button class="level-one">
                           <span class="level">1단계</span>
                           <span>여행유의</span>
                       </button>
                       <div class="tooltip-content">
                           <div class="tooltip-title">여행 예정자</div>
                           <ul>
                               <li class="tooltip-description">여행 시 신변안전에 유의</li>
                               <li class="tooltip-description">현지 상황 지속 확인</li>
                           </ul>
                           <div class="tooltip-title">체류자</div>
                           <ul>
                               <li class="tooltip-description">주의 깊게 일상 활동</li>
                               <li class="tooltip-description">비상 연락망 확보</li>
                           </ul>
                       </div>
                   </div>
                   <div class="tooltip-container">
                       <button class="level-two">
                           <span class="level">2단계</span>
                           <span>여행자제</span>
                       </button>
                       <div class="tooltip-content">
                           <div class="tooltip-title">여행 예정자</div>
                           <ul>
                               <li class="tooltip-description">불필요한 여행은 자제</li>
                               <li class="tooltip-description">부득이할 경우 철저한 준비</li>
                           </ul>
                           <div class="tooltip-title">체류자</div>
                           <ul>
                               <li class="tooltip-description">위험 지역 방문 자제</li>
                               <li class="tooltip-description">주변 상황 모니터링</li>
                           </ul>
                       </div>
                   </div>
                   <div class="tooltip-container">
                       <button class="level-three">
                           <span class="level">3단계</span>
                           <span>철수권고</span>
                       </button>
                       <div class="tooltip-content">
                           <div class="tooltip-title">여행 예정자</div>
                           <ul>
                               <li class="tooltip-description">여행 취소 권고</li>
                               <li class="tooltip-description">대체 목적지 고려</li>
                           </ul>
                           <div class="tooltip-title">체류자</div>
                           <ul>
                               <li class="tooltip-description">즉시 철수 준비</li>
                               <li class="tooltip-description">대사관 지침 준수</li>
                           </ul>
                       </div>
                   </div>
                   <div class="tooltip-container">
                       <button class="level-four">
                           <span class="level">4단계</span>
                           <span>여행금지</span>
                       </button>
                       <div class="tooltip-content">
                           <div class="tooltip-title">여행 예정자</div>
                           <ul>
                               <li class="tooltip-description">모든 여행 금지</li>
                               <li class="tooltip-description">대체 목적지 필수</li>
                           </ul>
                           <div class="tooltip-title">체류자</div>
                           <ul>
                               <li class="tooltip-description">즉시 철수</li>
                               <li class="tooltip-description">필수 시 대사관 보호 요청</li>
                           </ul>
                       </div>
                   </div>
                </div>
</body>
</html>