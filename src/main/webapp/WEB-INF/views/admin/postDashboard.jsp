<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="/css/admin/dashboard.css"/>
<div class="row mb-4">
    <!-- 전체 -->
    <div class="col-md-4">
        <div class="card shadow-sm p-3 d-flex flex-column justify-content-between">
            <div class="d-flex align-items-center">
                <div class="stat-line bg-primary me-2"></div>
                <h6 class="mb-0 text-muted">전체 게시글</h6>
            </div>
            <h2 id="totalCountries" class="fw-bold my-2"><fmt:formatNumber value="${empty stats.totalPosts ? 0 : stats.totalPosts}" /></h2>
        </div>
    </div>

    <!-- 안전 -->
    <div class="col-md-4">
        <div class="card shadow-sm p-3 d-flex flex-column justify-content-between">
            <div class="d-flex align-items-center">
                <div class="stat-line bg-success me-2"></div>
                <h6 class="mb-0 text-muted">오늘 게시글</h6>
            </div>
            <h2 id="safeCountries" class="fw-bold my-2"><fmt:formatNumber value="${empty stats.todayPosts ? 0 : stats.todayPosts}" /></h2>
        </div>
    </div>

    <!-- 여행 주의 대상 -->
    <div class="col-md-4">
        <div class="card shadow-sm p-3 d-flex flex-column justify-content-between">
            <div class="d-flex align-items-center">
                <div class="stat-line bg-danger me-2"></div>
                <h6 class="mb-0 text-muted">전체 댓글</h6>
            </div>
            <h2 id="bannedCountries" class="fw-bold my-2"><fmt:formatNumber value="${empty stats.totalComments ? 0 : stats.totalComments}" /></h2>
        </div>
    </div>
</div>

<!-- 차트 섹션 -->
<div class="content row">
    <!-- 카테고리별 분포 파이차트 -->
    <div class="col-md-6">
        <div class="chart-box equal-height">
            <h7>카테고리별 게시글 분포</h7>
            <canvas id="categoryPieChart"></canvas>
        </div>
    </div>

    <!-- 월별 작성 추이 라인차트 -->
    <div class="col-md-6">
        <div class="chart-box equal-height">
            <h7>월별 게시글 작성 추이 (최근 6개월)</h7>
            <canvas id="monthlyTrendChart"></canvas>
        </div>
    </div>
</div>

<div class="content row">
    <!-- 최근 7일간 활동 현황 -->
    <div class="col-md-12">
        <div class="chart-box">
            <h7>최근 7일간 활동 현황</h7>
            <canvas id="dailyActivityChart"></canvas>
        </div>
    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // 공통 차트 색상 팔레트
        const chartColors = {
            primary: ['#8CB6E9', '#5A90D2', '#30609D', '#0A2E5D', '#001530'],
            category: ['#5A90D2', '#FF6B9D', '#52C41A', '#F4B73F'],
            activity: {
                posts: '#5A90D2',
                comments: '#FF6B9D'
            }
        };

        // 카테고리별 게시글 분포 파이차트
        loadCategoryStats();

        // 월별 작성 추이 라인차트
        loadMonthlyTrend();

        // 최근 7일간 활동 현황
        loadDailyActivity();

        /**
         * 카테고리별 통계 로드
         */
        function loadCategoryStats() {
            fetch('/admin/api/posts/category-stats')
                .then(response => {
                    if (!response.ok) throw new Error('Network response was not ok');
                    return response.json();
                })
                .then(data => {
                    console.log('카테고리 통계 데이터:', data);

                    if (!data || data.length === 0) {
                        console.warn('카테고리 통계 데이터가 없습니다.');
                        return;
                    }

                    const labels = data.map(d => getCategoryDisplayName(d.CATEGORY || d.CATEGORY_NAME || d.category || '기타'));
                    const values = data.map(d => parseInt(d.COUNT || d.count) || 0);

                    new Chart(document.getElementById('categoryPieChart'), {
                        type: 'pie',
                        data: {
                            labels: labels,
                            datasets: [{
                                label: '게시글 수',
                                data: values,
                                backgroundColor: chartColors.category
                            }]
                        },
                        options: {
                            maintainAspectRatio: false,
                            responsive: true,
                            plugins: {
                                legend: {
                                    position: 'bottom',
                                    labels: {
                                        padding: 20,
                                        usePointStyle: true
                                    }
                                },
                                tooltip: {
                                    callbacks: {
                                        label: function(context) {
                                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                            const percentage = total > 0 ? Math.round((context.raw / total) * 100) : 0;
                                            return context.label + ': ' + context.raw + '개 (' + percentage + '%)';
                                        }
                                    }
                                }
                            }
                        }
                    });
                })
                .catch(error => {
                    console.error('카테고리 통계 로드 실패:', error);
                    createErrorChart('categoryPieChart', '카테고리 통계를 불러오지 못했습니다.');
                });
        }

        /**
         * 월별 추이 로드
         */
        function loadMonthlyTrend() {
            fetch('/admin/api/posts/monthly-trend')
                .then(response => {
                    if (!response.ok) throw new Error('Network response was not ok');
                    return response.json();
                })
                .then(data => {
                    console.log('월별 추이 데이터:', data);

                    if (!data || data.length === 0) {
                        console.warn('월별 추이 데이터가 없습니다.');
                        createEmptyTrendChart();
                        return;
                    }

                    const labels = data.map(d => {
                        if (d.MONTH && typeof d.MONTH === 'string' && d.MONTH.includes('-')) {
                            const [year, month] = d.MONTH.split('-');
                            return year + '년 ' + month + '월';
                        }
                        return '데이터 없음';
                    });
                    const values = data.map(d => parseInt(d.COUNT) || 0);

                    new Chart(document.getElementById('monthlyTrendChart'), {
                        type: 'line',
                        data: {
                            labels: labels,
                            datasets: [{
                                label: '게시글 수',
                                data: values,
                                borderColor: chartColors.primary[1],
                                backgroundColor: 'rgba(90, 144, 210, 0.1)',
                                fill: true,
                                tension: 0.4,
                                pointBackgroundColor: chartColors.primary[1],
                                pointBorderColor: '#fff',
                                pointBorderWidth: 2,
                                pointRadius: 5,
                                pointHoverRadius: 8
                            }]
                        },
                        options: {
                            maintainAspectRatio: false,
                            responsive: true,
                            plugins: {
                                legend: {
                                    position: 'bottom'
                                }
                            },
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    ticks: {
                                        stepSize: 1
                                    }
                                }
                            }
                        }
                    });
                })
                .catch(error => {
                    console.error('월별 추이 로드 실패:', error);
                    createErrorChart('monthlyTrendChart', '월별 추이를 불러오지 못했습니다.');
                });
        }

        /**
         * 일별 활동 현황 로드
         */
        function loadDailyActivity() {
            fetch('/admin/api/posts/daily-activity')
                .then(response => {
                    if (!response.ok) throw new Error('Network response was not ok');
                    return response.json();
                })
                .then(data => {
                    console.log('일별 활동 데이터:', data);

                    const dailyPosts = data.dailyPosts || [];
                    const dailyComments = data.dailyComments || [];

                    // 날짜 배열 생성 (최근 7일)
                    const dates = [];
                    for (let i = 6; i >= 0; i--) {
                        const date = new Date();
                        date.setDate(date.getDate() - i);
                        dates.push(date.toISOString().split('T')[0]);
                    }

                    const labels = dates.map(date => {
                        const d = new Date(date);
                        return (d.getMonth() + 1) + '/' + d.getDate();
                    });

                    // 데이터 매핑
                    const postCounts = dates.map(date => {
                        const found = dailyPosts.find(item => item.DAY === date);
                        return found ? parseInt(found.COUNT) || 0 : 0;
                    });

                    const commentCounts = dates.map(date => {
                        const found = dailyComments.find(item => item.DAY === date);
                        return found ? parseInt(found.COUNT) || 0 : 0;
                    });

                    new Chart(document.getElementById('dailyActivityChart'), {
                        type: 'bar',
                        data: {
                            labels: labels,
                            datasets: [{
                                label: '게시글',
                                data: postCounts,
                                backgroundColor: chartColors.activity.posts
                            }, {
                                label: '댓글',
                                data: commentCounts,
                                backgroundColor: chartColors.activity.comments
                            }]
                        },
                        options: {
                            responsive: true,
                            plugins: {
                                legend: {
                                    position: 'bottom'
                                }
                            },
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    ticks: {
                                        stepSize: 1
                                    }
                                }
                            }
                        }
                    });
                })
                .catch(error => {
                    console.error('일별 활동 로드 실패:', error);
                    createErrorChart('dailyActivityChart', '일별 활동 현황을 불러오지 못했습니다.');
                });
        }


        /**
         * 카테고리 표시명 반환 (JavaScript 함수)
         */
        function getCategoryDisplayName(category) {
            if (!category) return '기타';

            const categoryMap = {
                'READY': '출국준비',
                'REVIEW': '여행후기',
                'ACCOMPANY': '동행구함',
                'FREE': '자유게시판'
            };
            return categoryMap[category.toUpperCase()] || category;
        }

        /**
         * 날짜 포맷팅
         */
        function formatDate(dateString) {
            if (!dateString) return '-';
            const date = new Date(dateString);
            return date.getFullYear() + '-' +
                String(date.getMonth() + 1).padStart(2, '0') + '-' +
                String(date.getDate()).padStart(2, '0');
        }

        /**
         * 빈 트렌드 차트 생성
         */
        function createEmptyTrendChart() {
            new Chart(document.getElementById('monthlyTrendChart'), {
                type: 'line',
                data: {
                    labels: ['데이터 없음'],
                    datasets: [{
                        label: '게시글 수',
                        data: [0],
                        borderColor: chartColors.primary[1],
                        backgroundColor: 'rgba(90, 144, 210, 0.1)',
                        fill: true
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'bottom'
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        }

        /**
         * 에러 차트 생성
         */
        function createErrorChart(canvasId, message) {
            const canvas = document.getElementById(canvasId);
            const ctx = canvas.getContext('2d');
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            ctx.fillStyle = '#666';
            ctx.font = '14px Arial';
            ctx.textAlign = 'center';
            ctx.fillText(message, canvas.width / 2, canvas.height / 2);
        }
    });

    /**
     * 데이터 새로고침 함수
     */
    function refreshData() {
        location.reload();
    }
</script>

<style>
    /* 게시물 통계 전용 스타일 */

    /* 통계 카드 아이콘 색상 */
    .stat-card-icon.post {
        background: linear-gradient(135deg, #5A90D2, #30609D);
    }

    .stat-card-icon.comment {
        background: linear-gradient(135deg, #FF6B9D, #E91E63);
    }

    .stat-card-icon.like {
        background: linear-gradient(135deg, #FF4D4F, #F5222D);
    }

    .stat-card-icon.today {
        background: linear-gradient(135deg, #52C41A, #389E0D);
    }

    /* 인기 게시글 테이블 스타일 */
    .table th {
        background-color: var(--gray-100);
        border-bottom: 2px solid var(--gray-200);
        font-weight: 600;
        color: var(--gray-700);
    }

    .rank-badge {
        display: inline-block;
        width: 24px;
        height: 24px;
        border-radius: 50%;
        line-height: 24px;
        text-align: center;
        font-weight: bold;
        color: white;
        font-size: 12px;
    }

    .rank-1, .rank-2, .rank-3 {
        background: linear-gradient(135deg, #FFD700, #FFA500);
    }

    .rank-badge:not(.rank-1):not(.rank-2):not(.rank-3) {
        background: var(--gray-400);
    }

    .post-title-cell {
        max-width: 300px;
    }

    .post-title-link {
        color: var(--gray-700);
        text-decoration: none;
        display: block;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .post-title-link:hover {
        color: var(--main-600);
        text-decoration: underline;
    }

    .stat-line {
        width: 4px;
        height: 24px;
        border-radius: 2px;
    }

    .category-badge {
        display: inline-block;
        padding: 4px 8px;
        border-radius: 12px;
        font-size: 11px;
        font-weight: 500;
        background-color: var(--main-100);
        color: var(--main-700);
    }

    .stat-number {
        font-weight: 600;
        color: var(--gray-700);
    }

    .popularity-score {
        font-weight: bold;
        color: var(--main-600);
        font-size: 14px;
    }

    /* 테이블 호버 효과 */
    .table-hover tbody tr:hover {
        background-color: var(--gray-100);
    }

    /* 차트 컨테이너 높이 조정 */
    .equal-height {
        height: 35vh;
        min-height: 300px;
    }

    .chart-box {
        margin-top: 30px;
    }

    .chart-box canvas {
        height: 100% !important;
        /*width: 80% !important;*/
    }

    /* 일별 활동 차트 높이 */
    .chart-box:has(#dailyActivityChart) {
        height: auto;
        min-height: 400px;
    }

    #dailyActivityChart {
        height: 350px !important;
    }
</style>