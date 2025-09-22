<!-- 작성자: 김동현 -->
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" href="/css/admin/dashboard.css"/>

<div class="row mb-4">
    <div class="col-md-4">
        <div class="card shadow-sm p-3 d-flex flex-column justify-content-between">
            <div class="d-flex align-items-center">
                <div class="stat-line bg-primary me-2"></div>
                <h6 class="mb-0 text-muted">전체 회원</h6>
            </div>
            <h2 id="totalCountries" class="fw-bold my-2"><fmt:formatNumber value="${stats.totalUsers != null ? stats.totalUsers : 0}" /></h2>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card shadow-sm p-3 d-flex flex-column justify-content-between">
            <div class="d-flex align-items-center">
                <div class="stat-line bg-success me-2"></div>
                <h6 class="mb-0 text-muted">월간 신규 가입</h6>
            </div>
            <h2 id="safeCountries" class="fw-bold my-2"><fmt:formatNumber value="${stats.monthlySignups != null ? stats.monthlySignups : 0}" /></h2>
        </div>
    </div>

    <div class="col-md-4">
        <div class="card shadow-sm p-3 d-flex flex-column justify-content-between">
            <div class="d-flex align-items-center">
                <div class="stat-line bg-danger me-2"></div>
                <h6 class="mb-0 text-muted">여행 주의 대상</h6>
            </div>
            <h2 id="bannedCountries" class="fw-bold my-2"><fmt:formatNumber value="${stats.travelCautionUsers != null ? stats.travelCautionUsers : 0}" /></h2>
        </div>
    </div>

</div>
<div class="content row">
    <div class="col-md-6">
        <div class="chart-box equal-height">
            <h7>성별 분포</h7>
            <canvas id="genderPieChart"></canvas>
        </div>
    </div>

    <div class="col-md-6">
        <div class="chart-box equal-height">
            <h7>연령대별 분포</h7>
            <canvas id="ageBarChart"></canvas>
        </div>
    </div>
</div>

<div class="content row">
    <div class="col-md-6">
        <div class="chart-box">
            <h7>회원 등급별 분포</h7>
            <canvas id="gradeBarChart"></canvas>
        </div>
    </div>
    <div class="col-md-6">
        <div class="chart-box">
            <h7>건강 정보 보유 현황</h7>
            <canvas id="healthBarChart"></canvas>
        </div>
    </div>
</div>

<div class="content row">
    <div class="col-12">
        <div class="chart-box">
            <h7>월별 가입 추이</h7>
            <canvas id="monthlyTrendChart"></canvas>
        </div>
    </div>
</div>

<script>
    // 페이지 로드 후 실행
    document.addEventListener('DOMContentLoaded', function() {
        // 공통 차트 색상 팔레트
        const chartColors = {
            primary: ['#8CB6E9', '#5A90D2', '#30609D', '#0A2E5D', '#001530'],
            gender: ['#5A90D2', '#FF6B9D', '#C4C4C4'],
            age: ['#8CB6E9', '#5A90D2', '#30609D', '#0A2E5D', '#001530', '#F4B73F', '#FF7979'],
            grade: ['#84A9FF','#81EE80', '#85E4F6', '#FFF681', '#FFAB87'],
            health: ['#FF4D4F', '#FAAD14', '#52C41A', '#C4C4C4']
        };

        // 성별 분포 파이차트
        fetch('/admin/api/users/gender-stats')
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                console.log('성별 통계 데이터:', data);

                if (!data || data.length === 0) {
                    console.warn('성별 통계 데이터가 없습니다.');
                    return;
                }

                // Oracle에서 대문자로 반환되는 컬럼명 처리
                const labels = data.map(d => d.GENDER || '미설정');
                const values = data.map(d => d.COUNT || 0);

                new Chart(document.getElementById('genderPieChart'), {
                    type: 'pie',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: '회원 수',
                            data: values,
                            backgroundColor: chartColors.gender
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
                                        return context.label + ': ' + context.raw + '명 (' + percentage + '%)';
                                    }
                                }
                            }
                        }
                    }
                });
            })
            .catch(error => {
                console.error('성별 통계 로드 실패:', error);
                createErrorChart('genderPieChart', '성별 통계를 불러오지 못했습니다.');
            });

        // 연령대별 분포 바차트
        fetch('/admin/api/users/age-stats')
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                console.log('연령대 통계 데이터:', data);

                if (!data || data.length === 0) {
                    console.warn('연령대 통계 데이터가 없습니다.');
                    return;
                }

                // Oracle에서 대문자로 반환되는 컬럼명 처리
                const labels = data.map(d => d.AGEGROUP || '미설정');
                const values = data.map(d => d.COUNT || 0);

                new Chart(document.getElementById('ageBarChart'), {
                    type: 'bar',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: '회원 수',
                            data: values,
                            backgroundColor: chartColors.age[0],
                            borderColor: chartColors.age[1],
                            borderWidth: 1
                        }]
                    },
                    options: {
                        maintainAspectRatio: false,
                        responsive: true,
                        plugins: {
                            legend: { display: false }
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
                console.error('연령대 통계 로드 실패:', error);
                createErrorChart('ageBarChart', '연령대 통계를 불러오지 못했습니다.');
            });

        // 등급별 분포 바차트
        fetch('/admin/api/users/grade-stats')
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                console.log('등급별 통계 데이터:', data);

                if (!data || data.length === 0) {
                    console.warn('등급별 통계 데이터가 없습니다.');
                    return;
                }

                // Oracle에서 대문자로 반환되는 컬럼명 처리
                const labels = data.map(d => d.GRADE || '미설정');
                const values = data.map(d => d.COUNT || 0);

                new Chart(document.getElementById('gradeBarChart'), {
                    type: 'bar',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: '회원 수',
                            data: values,
                            backgroundColor: chartColors.grade
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: { display: false }
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
                console.error('등급별 통계 로드 실패:', error);
                createErrorChart('gradeBarChart', '등급별 통계를 불러오지 못했습니다.');
            });

        // 건강 정보 바차트
        fetch('/admin/api/users/health-stats')
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                console.log('건강 정보 통계 데이터:', data);

                if (!data || data.length === 0) {
                    console.warn('건강 정보 통계 데이터가 없습니다.');
                    return;
                }

                // Oracle에서 대문자로 반환되는 컬럼명 처리
                const labels = data.map(d => d.CATEGORY || '미설정');
                const values = data.map(d => d.COUNT || 0);

                new Chart(document.getElementById('healthBarChart'), {
                    type: 'bar',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: '회원 수',
                            data: values,
                            backgroundColor: chartColors.health
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: { display: false }
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
                console.error('건강 정보 통계 로드 실패:', error);
                createErrorChart('healthBarChart', '건강 정보 통계를 불러오지 못했습니다.');
            });

        // 월별 가입 추이 라인차트
        fetch('/admin/api/users/monthly-trend')
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                console.log('월별 추이 데이터:', data);

                if (!data || data.length === 0) {
                    console.warn('월별 추이 데이터가 없습니다.');
                    // 빈 데이터로 차트 생성
                    createEmptyTrendChart();
                    return;
                }

                // Oracle에서 대문자로 반환되는 컬럼명 처리
                const labels = data.map(d => {
                    if (d.MONTH && typeof d.MONTH === 'string' && d.MONTH.includes('-')) {
                        const [year, month] = d.MONTH.split('-');
                        return year + '년 ' + month + '월';
                    } else {
                        return '데이터 없음';
                    }
                });
                const values = data.map(d => d.COUNT || 0);

                new Chart(document.getElementById('monthlyTrendChart'), {
                    type: 'line',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: '가입자 수',
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
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                position: 'bottom',
                                labels: {
                                    padding: 20,
                                    usePointStyle: true
                                }
                            },
                            tooltip: {
                                mode: 'index',
                                intersect: false,
                                backgroundColor: 'rgba(0, 0, 0, 0.8)',
                                titleColor: '#fff',
                                bodyColor: '#fff',
                                borderColor: chartColors.primary[1],
                                borderWidth: 1
                            }
                        },
                        scales: {
                            x: {
                                display: true,
                                grid: {
                                    display: false
                                },
                                ticks: {
                                    maxTicksLimit: 6
                                }
                            },
                            y: {
                                beginAtZero: true,
                                grid: {
                                    color: 'rgba(0, 0, 0, 0.1)'
                                },
                                ticks: {
                                    stepSize: 1
                                }
                            }
                        },
                        interaction: {
                            mode: 'nearest',
                            axis: 'x',
                            intersect: false
                        }
                    }
                });
            })
            .catch(error => {
                console.error('월별 추이 통계 로드 실패:', error);
                createErrorChart('monthlyTrendChart', '월별 추이 통계를 불러오지 못했습니다.');
            });

        // 빈 트렌드 차트 생성 함수
        function createEmptyTrendChart() {
            new Chart(document.getElementById('monthlyTrendChart'), {
                type: 'line',
                data: {
                    labels: ['데이터 없음'],
                    datasets: [{
                        label: '가입자 수',
                        data: [0],
                        borderColor: chartColors.primary[1],
                        backgroundColor: 'rgba(90, 144, 210, 0.1)',
                        fill: true
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
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

        // 에러 차트 생성 함수
        function createErrorChart(canvasId, message) {
            const canvas = document.getElementById(canvasId);
            const ctx = canvas.getContext('2d');

            // 캔버스 클리어
            ctx.clearRect(0, 0, canvas.width, canvas.height);

            // 에러 메시지 표시
            ctx.fillStyle = '#666';
            ctx.font = '14px Arial';
            ctx.textAlign = 'center';
            ctx.fillText(message, canvas.width / 2, canvas.height / 2);
        }
    });

    // 데이터 새로고침 함수
    function refreshData() {
        location.reload();
    }
</script>

<style>
    .chart-box {
        width: 100%;
        margin: 0.5rem auto;
        background: #fff;
        border-radius: 12px;
        padding: 1.5rem;
        /*box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);*/
        /*transition: all 0.3s ease;*/
    }

    /*.chart-box:hover {*/
    /*    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);*/
    /*    transform: translateY(-2px);*/
    /*}*/

    .equal-height {
        height: 35vh;
    }

    .equal-height canvas {
        height: 100% !important;
    }

    .chart-box h7 {
        display: block;
        font-weight: 600;
        font-size: 1.1rem;
        color: var(--gray-700);
        padding-bottom: 1rem;
        margin-bottom: 1rem;
        border-bottom: 2px solid var(--gray-100);
    }

    .stat-line {
        width: 4px;
        height: 24px;
        border-radius: 2px;
    }

    .card {
        transition: all 0.3s ease;
    }

    .card:hover {
        transform: translateY(-3px);
        /*box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);*/
    }

    .card h2 {
        font-size: 2rem;
        font-weight: 700;
    }

    .chart-loading {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 200px;
        color: var(--gray-500);
    }

    .chart-error {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 200px;
        color: var(--semantic-error);
        flex-direction: column;
    }

    .chart-error i {
        font-size: 2rem;
        margin-bottom: 0.5rem;
    }

    .chart-box canvas {
        opacity: 0;
        animation: fadeIn 0.6s ease-in-out forwards;
    }

    @keyframes fadeIn {
        from {
            opacity: 0;
            transform: translateY(20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    /* 통계 수치 강조 */
    .stat-card-value {
        background: linear-gradient(135deg, var(--main-600), var(--main-700));
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
    }

    /* 월별 추이 차트만 높이 조정 */
    #monthlyTrendChart {
        height: 300px !important;
    }

    .chart-box:has(#monthlyTrendChart) {
        height: auto;
    }

    /* 또는 월별 추이 차트 컨테이너에 별도 클래스 적용 */
    .monthly-trend-chart {
        height: 350px;
    }

    .monthly-trend-chart canvas {
        height: 100% !important;
    }
</style>