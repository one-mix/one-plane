<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="/css/sidebar.css" />

<div class="sidebar">
    <!-- 프로필 섹션 -->
    <div class="profile-section">
        <div class="profile-avatar" id="profileAvatar" title="클릭하여 이미지 변경">
            <!-- 항상 하나의 <img>만 렌더링 -->
            <img id="avatarImg"
                 src="${not empty userProfile.profileImagePath ? userProfile.profileImagePath : '/images/default-avatar.png'}"
                 alt="프로필 아바타"
                 style="width:80px; height:80px; border-radius:50%; object-fit:cover;" />

            <!-- 숨겨진 파일 입력 -->
            <input type="file"
                   id="avatarInput"
                   name="profileImage"
                   accept="image/*"
                   style="display:none;"
                   onchange="uploadAvatar()" />

<%--            <div class="upload-overlay">📷</div>--%>
        </div>

        <div class="profile-info">
            <h4>${userProfile.nickname != null ? userProfile.nickname : '사용자'}</h4>
        </div>
    </div>

    <!-- 메뉴 섹션 -->
    <div class="menu-section">
        <h4>여행지</h4>
        <ul class="menu-list">
            <li><a href="/mypage/travelHistory" class="${activeMenu == 'travelHistory' ? 'active' : ''}">방문한</a></li>
            <li><a href="/section/recommend" class="${activeMenu == 'recommend' ? 'active' : ''}">추천받은</a></li>
        </ul>
    </div>

    <div class="menu-section">
        <h4>게시글</h4>
        <ul class="menu-list">
            <li><a href="/section/write" class="${activeMenu == 'write' ? 'active' : ''}">내가 작성한</a></li>
            <li><a href="/section/follower" class="${activeMenu == 'follower' ? 'active' : ''}">팔로워가 작성한</a></li>
        </ul>
    </div>

    <div class="menu-section">
        <h4>회원정보</h4>
        <ul class="menu-list">
            <li><a href="/mypage/profile/edit" class="${activeMenu == 'profile' ? 'active' : ''}">프로필 편집</a></li>
            <li class="danger"><a href="/mypage/profile/out">회원탈퇴</a></li>
        </ul>
    </div>
</div>

<script>
    // 프로필 아바타 클릭 시 파일 선택창 열기
    document.getElementById('profileAvatar').addEventListener('click', function() {
        document.getElementById('avatarInput').click();
    });

    function uploadAvatar() {
        const input = document.getElementById('avatarInput');
        const file = input.files[0];
        if (!file) return;

        // 파일 타입 확인
        if (!file.type.startsWith('image/')) {
            alert('이미지 파일만 업로드 가능합니다.');
            input.value = '';
            return;
        }
        // 파일 크기 확인 (5MB 제한)
        if (file.size > 5 * 1024 * 1024) {
            alert('파일 크기는 5MB 이하이어야 합니다.');
            input.value = '';
            return;
        }

        const formData = new FormData();
        formData.append('profileImage', file);

        // 업로드 진행
        fetch('/mypage/profile/uploadImage', {
            method: 'POST',
            body: formData
        })
            .then(response => response.text())
            .then(result => {
                if (result === 'success') {
                    // 즉시 미리보기
                    const reader = new FileReader();
                    reader.onload = e => {
                        document.getElementById('avatarImg').src = e.target.result;
                    };
                    reader.readAsDataURL(file);
                    alert('프로필 사진이 변경되었습니다.');
                } else {
                    alert('업로드에 실패했습니다.');
                }
            })
            .catch(() => {
                alert('업로드 중 오류가 발생했습니다.');
            })
            .finally(() => {
                input.value = '';
            });
    }
</script>
