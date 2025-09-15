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
     *
     * @param userId 조회할 회원 ID
     * @return UsersProfile 도메인 객체
     */
    @Transactional(readOnly = true)
    public UsersProfile getUserProfile(Long userId) {
        log.info("회원 프로필 조회 요청 - userId: {}", userId);
        return repository.findById(userId);
    }

    /**
     * 프로필 업데이트:
     * 이메일, 닉네임, disease, disability, medication 포함
     * 닉네임 중복 검사 수행
     *
     * @param profile 수정할 UsersProfile 객체
     */
    @Transactional
    public void updateUserProfile(UsersProfile profile) {
        log.info("회원 프로필 수정 요청 - {}", profile);

        // 닉네임 중복 검사 (자기 자신 제외)
        int dupCount = repository.countByNickname(profile.getNickname(), profile.getUserId());
        if (dupCount > 0) {
            log.warn("중복된 닉네임 입력 - userId: {}, nickname: {}", profile.getUserId(), profile.getNickname());
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        int updated = repository.update(profile);
        if (updated <= 0) {
            log.warn("프로필 업데이트 실패 - userId: {}", profile.getUserId());
            throw new IllegalStateException("프로필 업데이트에 실패했습니다.");
        }

        log.info("프로필 업데이트 완료 - userId: {}", profile.getUserId());
    }

    /**
     * 회원 탈퇴 처리 (Soft Delete)
     *
     * @param userId 회원 ID
     */
    @Transactional
    public void withdrawUser(Long userId) {
        log.info("회원 탈퇴 요청 - userId: {}", userId);
        Date today = Date.valueOf(LocalDate.now());
        int deleted = repository.softDelete(userId, today);
        if (deleted <= 0) {
            log.warn("이미 탈퇴되었거나 처리 실패 - userId: {}", userId);
            throw new IllegalStateException("탈퇴 처리에 실패했습니다.");
        }
        log.info("탈퇴 처리 완료 - userId: {}", userId);
    }
}
