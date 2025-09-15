<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<link href="/css/post/postWrite.css" rel="stylesheet">
<!-- Summernote CSS -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.css" rel="stylesheet">

<div class="post-write-container">
    <h2 style="margin-bottom: 30px; color: var(--gray-700); font-weight: var(--font-semibold);">게시글 수정</h2>

    <form id="postEditForm" action="/post/edit/${post.postId}" method="post">
        <sec:csrfInput />
        <input type="hidden" name="postId" value="${post.postId}" />

        <!-- 유형 및 국가 -->
        <div class="form-row">
            <label class="form-label">유형</label>
            <select name="category" class="form-select" required>
                <option value="" disabled>게시판의 유형을 선택하세요.</option>
                <option value="READY" ${post.category == 'READY' ? 'selected' : ''}>여행준비</option>
                <option value="REVIEW" ${post.category == 'REVIEW' ? 'selected' : ''}>여행후기</option>
                <option value="ACCOMPANY" ${post.category == 'ACCOMPANY' ? 'selected' : ''}>동행구하기</option>
                <option value="FREE" ${post.category == 'FREE' ? 'selected' : ''}>자유게시판</option>
            </select>

            <label class="form-label">국가</label>
            <select name="countryId" class="form-select" required>
                <option value="" disabled>국가를 선택하세요.</option>
                <c:forEach var="country" items="${countries}">
                    <option value="${country.countryId}"
                        ${post.countryId == country.countryId ? 'selected' : ''}>
                            ${country.countryName}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- 제목 -->
        <div class="form-row">
            <label class="form-label">제목</label>
            <input type="text" name="title" class="form-control" placeholder="제목을 입력하세요"
                   value="${post.title}" required>
        </div>

        <!-- 내용 -->
        <div class="form-row">
            <label class="form-label">내용</label>
            <textarea id="content" name="content" class="content-editor" required>${post.content}</textarea>
        </div>

        <!-- 버튼 컨테이너 -->
        <div class="button-container">
            <a href="/post/detail/${post.postId}" class="btn-cancel"
               style="background: white; color: var(--gray-700); border: 1px solid var(--gray-200); padding: 12px var(--spacing-lg);
                      border-radius: 6px; font-size: 16px; font-weight: var(--font-medium); font-family: var(--font-main);
                      cursor: pointer; text-decoration: none; display: inline-block; margin-right: 10px;">
                취소
            </a>
            <button type="submit" class="btn-submit">수정완료</button>
        </div>
    </form>
</div>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- Summernote JS -->
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/lang/summernote-ko-KR.min.js"></script>

<script>
    $(document).ready(function() {
        // Summernote 초기화
        $('#content').summernote({
            height: 400,
            width: 1000,
            lang: 'ko-KR',
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'strikethrough']],
                ['font', ['fontsize', 'color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['table', ['table']],
                ['insert', ['link', 'picture']],
                ['view', ['codeview']]
            ],
            fontSizes: ['8', '9', '10', '11', '12', '14', '16', '18', '20', '22', '24', '28', '30', '36', '50', '72'],
            callbacks: {
                onImageUpload: function(files) {
                    uploadImage(files[0]);
                }
            }
        });

        // 이미지 업로드 함수
        function uploadImage(file) {
            if (file.size > 5 * 1024 * 1024) {
                alert('이미지 파일 크기는 5MB를 초과할 수 없습니다.');
                return;
            }

            if (!file.type.startsWith('image/')) {
                alert('이미지 파일만 업로드할 수 있습니다.');
                return;
            }

            const formData = new FormData();
            formData.append('image', file);

            $.ajax({
                url: '/api/upload/image',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                beforeSend: function(xhr) {
                    // CSRF 토큰 설정
                    const token = $('input[name="_csrf"]').val();
                    if (token) {
                        xhr.setRequestHeader('X-CSRF-TOKEN', token);
                    }
                },
                success: function(response) {
                    if (response.success) {
                        // Summernote에 이미지 삽입
                        $('#content').summernote('insertImage', response.imageUrl);
                        console.log('이미지 업로드 성공:', response.imageUrl);
                    } else {
                        alert('이미지 업로드에 실패했습니다: ' + response.message);
                    }
                },
                error: function(xhr, status, error) {
                    console.error('이미지 업로드 오류:', error);
                    alert('이미지 업로드 중 오류가 발생했습니다.');
                }
            });
        }

        // select 요소 변경 감지하여 색상 변경
        $('.form-select').each(function() {
            if ($(this).val()) {
                $(this).css('color', '#374151');
            }
        });

        $('.form-select').on('change', function() {
            if ($(this).val()) {
                $(this).css('color', '#374151');
            } else {
                $(this).css('color', '#9ca3af');
            }
        });

        // 폼 제출 처리
        $('#postEditForm').on('submit', function(e) {
            e.preventDefault();

            const title = $('input[name="title"]').val().trim();
            const content = $('#content').summernote('code').trim();
            const category = $('select[name="category"]').val();
            const countryId = $('select[name="countryId"]').val();

            if (!title) {
                alert('제목을 입력해주세요.');
                $('input[name="title"]').focus();
                return;
            }

            if (!content || content === '<p><br></p>' || content === '<br>') {
                alert('내용을 입력해주세요.');
                $('#content').summernote('focus');
                return;
            }

            if (!category) {
                alert('게시판 유형을 선택해주세요.');
                $('select[name="category"]').focus();
                return;
            }

            if (!countryId) {
                alert('국가를 선택해주세요.');
                $('select[name="countryId"]').focus();
                return;
            }

            // 수정 확인
            if (!confirm('게시글을 수정하시겠습니까?')) {
                return;
            }

            // 로딩 표시
            const $submitBtn = $('.btn-submit');
            const originalText = $submitBtn.text();
            $submitBtn.prop('disabled', true).text('수정 중...');

            // AJAX로 폼 제출
            $.ajax({
                url: $(this).attr('action'),
                type: 'POST',
                data: $(this).serialize(),
                success: function(response) {
                    if (response.success) {
                        alert('게시글이 성공적으로 수정되었습니다!');
                        window.location.href = '/post/detail/${post.postId}';
                    } else {
                        alert(response.message || '게시글 수정에 실패했습니다.');
                        $submitBtn.prop('disabled', false).text(originalText);
                    }
                },
                error: function(xhr) {
                    let errorMsg = '게시글 수정 중 오류가 발생했습니다.';
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMsg = xhr.responseJSON.message;
                    }
                    alert(errorMsg);
                    $submitBtn.prop('disabled', false).text(originalText);
                }
            });
        });

        // 페이지 이탈 경고
        let formChanged = false;

        $('input[name="title"], select').on('input change', function() {
            formChanged = true;
        });

        $('#content').on('summernote.change', function() {
            formChanged = true;
        });

        $(window).on('beforeunload', function() {
            if (formChanged) {
                return '수정 중인 내용이 있습니다. 정말로 페이지를 떠나시겠습니까?';
            }
        });

        $('#postEditForm').on('submit', function() {
            formChanged = false;
        });

        $('.btn-cancel').on('click', function(e) {
            if (formChanged) {
                if (!confirm('수정 중인 내용이 있습니다. 정말로 취소하시겠습니까?')) {
                    e.preventDefault();
                }
            }
        });
    });
</script>