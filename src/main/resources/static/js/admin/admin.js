$(document).ready(function() {
    // 사이드바 네비게이션 초기화
    initializeSidebar();

});

/**
 * 사이드바 네비게이션 초기화
 */
function initializeSidebar() {
    // 네비게이션 섹션 클릭 이벤트
    $('.nav-header').click(function() {
        const $section = $(this).parent('.nav-section');
        const $submenu = $section.find('.nav-submenu');

        // 현재 섹션 토글
        $section.toggleClass('expanded');

        // 다른 섹션들 닫기 (아코디언 효과)
        $('.nav-section').not($section).removeClass('expanded');
    });

    // 현재 활성 메뉴의 부모 섹션 열기
    const $activeMenu = $('.nav-submenu a.active');
    if ($activeMenu.length > 0) {
        $activeMenu.closest('.nav-section').addClass('expanded');
    }

    // 모바일에서 사이드바 토글
    $(document).on('click', '.mobile-menu-btn', function() {
        $('.admin-sidebar').toggleClass('open');
    });

    // 모바일에서 외부 클릭 시 사이드바 닫기
    $(document).on('click', function(e) {
        if ($(window).width() <= 768) {
            if (!$(e.target).closest('.admin-sidebar, .mobile-menu-btn').length) {
                $('.admin-sidebar').removeClass('open');
            }
        }
    });
}
/**
 * 데이터 테이블 초기화
 */
function initializeDataTable(tableSelector, options = {}) {
    const defaultOptions = {
        pageLength: 25,
        responsive: true,
        language: {
            url: '//cdn.datatables.net/plug-ins/1.13.4/i18n/ko.json'
        },
        dom: '<"row"<"col-sm-12 col-md-6"l><"col-sm-12 col-md-6"f>>' +
            '<"row"<"col-sm-12"tr>>' +
            '<"row"<"col-sm-12 col-md-5"i><"col-sm-12 col-md-7"p>>',
        ...options
    };

    return $(tableSelector).DataTable(defaultOptions);
}

/**
 * AJAX 요청 공통 함수
 */
function makeAjaxRequest(url, method = 'GET', data = null, options = {}) {
    const defaultOptions = {
        url: url,
        method: method,
        data: data,
        dataType: 'json',
        beforeSend: function(xhr) {
            // CSRF 토큰 추가
            const token = $('meta[name="_csrf"]').attr('content');
            const header = $('meta[name="_csrf_header"]').attr('content');
            if (token && header) {
                xhr.setRequestHeader(header, token);
            }
        },
        success: function(response) {
            if (options.onSuccess) {
                options.onSuccess(response);
            }
        },
        error: function(xhr, status, error) {
            console.error('AJAX Error:', error);
            if (options.onError) {
                options.onError(xhr, status, error);
            } else {
                showErrorAlert('요청 처리 중 오류가 발생했습니다.');
            }
        }
    };

    return $.ajax({ ...defaultOptions, ...options });
}

/**
 * 차트 색상 팔레트
 */
const chartColors = {
    primary: '#215BAF',
    secondary: '#6c757d',
    success: '#10b981',
    warning: '#f59e0b',
    danger: '#ef4444',
    info: '#3b82f6',
    light: '#f8f9fa',
    dark: '#212529'
};

/**
 * 숫자 포맷팅
 */
function formatNumber(num) {
    return num.toLocaleString();
}

/**
 * 날짜 포맷팅
 */
function formatDate(date, format = 'YYYY-MM-DD') {
    if (typeof date === 'string') {
        date = new Date(date);
    }

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return format
        .replace('YYYY', year)
        .replace('MM', month)
        .replace('DD', day);
}