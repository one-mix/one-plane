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
                // 파일을 미리보기로 읽기
                const reader = new FileReader();
                reader.onload = e => {
                    // 사이드바 이미지 업데이트
                    document.getElementById('avatarImg').src = e.target.result;

                    // 헤더의 프로필 이미지도 업데이트
                    const headerProfileImg = document.querySelector('.header .profile-img');
                    if (headerProfileImg) {
                        headerProfileImg.src = e.target.result;
                    }
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