package com.oneplane.userprofile.service;

import com.oneplane.userprofile.domain.UsersProfile;
import com.oneplane.userprofile.repository.UsersProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersProfileService {

    private final UsersProfileRepository repository;

    /**
     * 회원 프로필 조회
     * @param userId 조회할 회원 ID
     * @return UserProfile 프로필 정보
     */
    @Transactional(readOnly = true)
    public UsersProfile getUserProfile(Long userId) {
        log.info("회원 프로필 조회 요청 - userId: {}", userId);
        return repository.findById(userId);
    }

    /**
     * 이메일 및 닉네임 업데이트
     * @param userId   회원 ID
     * @param email    변경할 이메일
     * @param nickname 변경할 닉네임
     * @return true 업데이트 성공 여부
     */
    @Transactional
    public boolean updateUserProfile(Long userId, String email, String nickname) {
        log.info("회원 프로필 수정 요청 - userId: {}, email: {}, nickname: {}", userId, email, nickname);
        UsersProfile profile = repository.findById(userId);
        if (profile == null) {
            log.warn("회원 프로필이 존재하지 않습니다 - userId: {}", userId);
            return false;
        }
        profile.setEmail(email);
        profile.setNickname(nickname);
        int updated = repository.save(profile);
        boolean success = updated > 0;
        if (success) {
            log.info("회원 프로필 수정 완료 - userId: {}", userId);
        } else {
            log.warn("회원 프로필 수정 실패 - userId: {}", userId);
        }
        return success;
    }

    /**
     * 회원 탈퇴 처리 (Soft Delete)
     * @param userId 회원 ID
     * @return true: 탈퇴 처리 성공, false: 이미 탈퇴했거나 실패
     */
    @Transactional
    public boolean withdrawUser(Long userId) {
        log.info("회원 탈퇴 요청 - userId: {}", userId);
        Date today = Date.valueOf(LocalDate.now());
        int updated = repository.softDelete(userId, today);
        boolean success = updated > 0;
        if (success) {
            log.info("회원 탈퇴 처리 완료 - userId: {}", userId);
        } else {
            log.warn("회원 탈퇴 처리 실패 또는 이미 탈퇴된 회원 - userId: {}", userId);
        }
        return success;
    }
}
