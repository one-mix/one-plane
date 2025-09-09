package com.oneplane.user.service;

import com.oneplane.user.dao.UserDao;
import com.oneplane.user.domain.User;
import com.oneplane.user.dto.ProfileCompleteRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserDao userDao;

    /**
     * 사용자 ID로 조회
     */
    @Transactional(readOnly = true)
    public User findById(Integer userId) {
        log.debug("사용자 ID로 조회: {}", userId);
        return userDao.findById(userId);
    }

    /**
     * 이메일로 사용자 조회
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        log.debug("이메일로 사용자 조회: {}", email);
        return userDao.findByEmail(email);
    }

    /**
     * 프로필 완성 처리 (카카오 로그인 후 추가 정보 입력)
     */
    public User completeProfile(Integer userId, ProfileCompleteRequestDto profileRequest) {
        log.info("프로필 완성 처리 시작 - 사용자 ID: {}", userId);

        // 기존 사용자 조회
        User existingUser = userDao.findById(userId);
        if (existingUser == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        // 이미 프로필이 완성된 사용자인지 확인
        if (existingUser.isProfileComplete()) {
            log.warn("이미 프로필이 완성된 사용자: {}", userId);
            return existingUser;
        }

        // 닉네임 중복 확인
        if (!profileRequest.getNickname().equals(existingUser.getNickname())) {
            if (isNicknameDuplicate(profileRequest.getNickname())) {
                throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
            }
        }

        // 사용자 정보 업데이트
        existingUser.setName(profileRequest.getName());
        existingUser.setNickname(profileRequest.getNickname());
        existingUser.setAge(profileRequest.getAge());
        existingUser.setGender(profileRequest.getGender());
        existingUser.setDisease(profileRequest.getDisease().toString());
        existingUser.setDisability(profileRequest.getDisability().toString());
        existingUser.setMedication(profileRequest.getMedication().toString());
        existingUser.setUpdatedAt(LocalDateTime.now());

        // 데이터베이스 업데이트
        int result = userDao.updateUser(existingUser);
        if (result <= 0) {
            throw new RuntimeException("프로필 완성 처리에 실패했습니다.");
        }

        // 업데이트된 사용자 정보 조회 및 반환
        User updatedUser = userDao.findById(userId);
        log.info("프로필 완성 완료 - 사용자 ID: {}, 이름: {}, 닉네임: {}",
                userId, updatedUser.getName(), updatedUser.getNickname());

        return updatedUser;
    }

    /**
     * 닉네임 중복 확인
     */
    @Transactional(readOnly = true)
    public boolean isNicknameDuplicate(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            return false;
        }
        return userDao.existsByNickname(nickname.trim());
    }

    /**
     * 사용자 탈퇴 (논리 삭제)
     */
    public void deleteUser(Integer userId) {
        log.info("사용자 탈퇴 처리 - 사용자 ID: {}", userId);

        User user = userDao.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        int result = userDao.deleteUser(userId);
        if (result <= 0) {
            throw new RuntimeException("사용자 탈퇴 처리에 실패했습니다.");
        }

        log.info("사용자 탈퇴 완료 - 이메일: {}", user.getEmail());
    }

    /**
     * 사용자 프로필 완성 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean isProfileComplete(Integer userId) {
        User user = userDao.findById(userId);
        return user != null && user.isProfileComplete();
    }

    /**
     * 사용자 정보 업데이트
     */
    public User updateUser(User user) {
        log.info("사용자 정보 업데이트 - 사용자 ID: {}", user.getUser_id());

        user.setUpdatedAt(LocalDateTime.now());

        int result = userDao.updateUser(user);
        if (result <= 0) {
            throw new RuntimeException("사용자 정보 업데이트에 실패했습니다.");
        }

        return userDao.findById(user.getUser_id());
    }

    /**
     * 건강 정보 보유 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean hasHealthInfo(Integer userId) {
        User user = userDao.findById(userId);
        return user != null && user.hasHealthInfo();
    }

    /**
     * 여행 시 주의가 필요한 사용자인지 확인
     */
    @Transactional(readOnly = true)
    public boolean needsTravelCaution(Integer userId) {
        User user = userDao.findById(userId);
        if (user == null) {
            return false;
        }

        return user.hasHealthInfo() || (user.getAge() != null && user.getAge() >= 65);
    }
}