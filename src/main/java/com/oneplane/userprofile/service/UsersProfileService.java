package com.oneplane.userprofile.service;

import com.oneplane.userprofile.domain.UsersProfile;
import com.oneplane.userprofile.repository.UsersProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.time.LocalDate;

/**
 * 사용자 프로필 비즈니스 로직 처리 서비스
 * 프로필 조회, 수정, 탈퇴(Soft Delete) 기능 제공
 *
 * @author 허겸
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsersProfileService {

    private final UsersProfileRepository repository;  // JPA Repository 의존성

    /**
     * 특정 사용자 프로필 조회
     * 읽기 전용 트랜잭션으로 최적화
     *
     * @param userId 조회할 사용자 ID
     * @return UsersProfile 도메인 객체
     */
    @Transactional(readOnly = true)
    public UsersProfile getUserProfile(Long userId) {
        log.info("회원 프로필 조회 요청 - userId: {}", userId);
        return repository.findById(userId);
    }

    /**
     * 사용자 프로필 업데이트 처리
     * 이메일, 닉네임, 기저질환, 장애, 복용약 정보 수정
     * 닉네임 중복 검사 수행 후 업데이트
     *
     * @param profile 수정할 UsersProfile 객체
     * @throws IllegalArgumentException 닉네임 중복 시
     * @throws IllegalStateException    업데이트 실패 시
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

        // 실제 업데이트 수행
        int updated = repository.update(profile);
        if (updated <= 0) {
            log.warn("프로필 업데이트 실패 - userId: {}", profile.getUserId());
            throw new IllegalStateException("프로필 업데이트에 실패했습니다.");
        }
        log.info("프로필 업데이트 완료 - userId: {}", profile.getUserId());
    }

    /**
     * 회원 탈퇴(Soft Delete) 처리
     * 탈퇴 일자로 현재 날짜 저장
     *
     * @param userId 탈퇴할 사용자 ID
     * @throws IllegalStateException 이미 탈퇴된 경우 또는 처리 실패 시
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