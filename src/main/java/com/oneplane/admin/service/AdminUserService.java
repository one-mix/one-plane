package com.oneplane.admin.service;

import com.oneplane.admin.dao.AdminUserDao;
import com.oneplane.admin.domain.AdminUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminUserService {

    private final AdminUserDao adminUserDao;


    // 전체 사용자 목록 조회 (페이징, 검색)
    @Transactional(readOnly = true)
    public Map<String, Object> getUserList(int page, int pageSize, String search) {
        log.debug("사용자 목록 조회 - page: {}, pageSize: {}, search: {}", page, pageSize, search);

        // 페이지 검증 및 기본값 설정
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 20;

        // offset 계산
        int offset = (page - 1) * pageSize;

        // 검색 조건 정리 (공백 제거, null 처리)
        String searchKeyword = (search != null && !search.trim().isEmpty()) ? search.trim() : null;

        // 사용자 목록 조회
        List<AdminUser> users = adminUserDao.findUserList(offset, pageSize, searchKeyword);

        // 전체 개수 조회 (검색 조건 포함)
        int totalCount = adminUserDao.getTotalUserCount(searchKeyword);

        // 페이지 정보 계산
        int totalPages = (totalCount == 0) ? 1 : (int) Math.ceil((double) totalCount / pageSize);
        boolean hasNext = page < totalPages;
        boolean hasPrevious = page > 1;

        // 결과 Map 생성
        Map<String, Object> result = new HashMap<>();
        result.put("users", users);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);
        result.put("totalCount", totalCount);
        result.put("pageSize", pageSize);
        result.put("hasNext", hasNext);
        result.put("hasPrevious", hasPrevious);
        result.put("startRow", (page - 1) * pageSize + 1);
        result.put("endRow", Math.min(page * pageSize, totalCount));

        log.debug("사용자 목록 조회 완료 - 총 {}명, {}페이지/{} (검색어: {})",
                totalCount, page, totalPages, searchKeyword);

        return result;
    }

    /**
     * 탈퇴 사용자 목록 조회 (페이징, 검색)
     * @param page 페이지 번호 (1부터 시작)
     * @param pageSize 페이지 크기
     * @param search 검색어 (닉네임, 이름)
     * @return 탈퇴 사용자 목록과 페이징 정보
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getDeletedUserList(int page, int pageSize, String search) {
        log.debug("탈퇴 사용자 목록 조회 - page: {}, pageSize: {}, search: {}", page, pageSize, search);

        // 페이지 검증 및 기본값 설정
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 20;

        // offset 계산
        int offset = (page - 1) * pageSize;

        // 검색 조건 정리
        String searchKeyword = (search != null && !search.trim().isEmpty()) ? search.trim() : null;

        // 탈퇴 사용자 목록 조회
        List<AdminUser> users = adminUserDao.findDeletedUserList(offset, pageSize, searchKeyword);

        // 전체 개수 조회
        int totalCount = adminUserDao.getTotalDeletedUserCount(searchKeyword);

        // 페이지 정보 계산
        int totalPages = (totalCount == 0) ? 1 : (int) Math.ceil((double) totalCount / pageSize);
        boolean hasNext = page < totalPages;
        boolean hasPrevious = page > 1;

        // 결과 Map 생성
        Map<String, Object> result = new HashMap<>();
        result.put("users", users);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);
        result.put("totalCount", totalCount);
        result.put("pageSize", pageSize);
        result.put("hasNext", hasNext);
        result.put("hasPrevious", hasPrevious);
        result.put("startRow", (page - 1) * pageSize + 1);
        result.put("endRow", Math.min(page * pageSize, totalCount));

        log.debug("탈퇴 사용자 목록 조회 완료 - 총 {}명, {}페이지/{}", totalCount, page, totalPages);

        return result;
    }

    /**
     * 사용자 ID로 단일 사용자 조회
     *
     * @param userId 사용자 ID
     * @return AdminUser 객체 (없으면 null)
     */
    @Transactional(readOnly = true)
    public AdminUser getUserById(Integer userId) {
        log.debug("사용자 ID로 조회 - userId: {}", userId);

        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID가 필요합니다.");
        }

        if (userId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다: " + userId);
        }

        AdminUser user = adminUserDao.findUserById(userId);

        if (user == null) {
            log.warn("사용자를 찾을 수 없습니다 - userId: {}", userId);
        } else {
            log.debug("사용자 조회 완료 - userId: {}, email: {}, nickname: {}",
                    userId, user.getEmail(), user.getNickname());
        }

        return user;
    }

    /**
     * 사용자 정보 수정
     * @param user 수정할 사용자 정보
     * @return 수정된 사용자 정보
     */
    public AdminUser updateUser(AdminUser user) {
        log.info("사용자 정보 수정 시작 - userId: {}", user.getUser_id());

        // 입력값 검증
        if (user == null) {
            throw new IllegalArgumentException("사용자 정보가 필요합니다.");
        }

        if (user.getUser_id() == null || user.getUser_id() <= 0) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }

        // 기존 사용자 존재 여부 확인
        AdminUser existingUser = adminUserDao.findUserById(user.getUser_id());
        if (existingUser == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + user.getUser_id());
        }

        // 이미 삭제된 사용자인지 확인
        if (existingUser.isDeleted()) {
            throw new IllegalStateException("삭제된 사용자는 수정할 수 없습니다.");
        }

        // 닉네임 중복 확인 (본인 제외)
        if (user.getNickname() != null && !user.getNickname().equals(existingUser.getNickname())) {
            if (isNicknameDuplicate(user.getNickname(), user.getUser_id())) {
                throw new IllegalArgumentException("이미 사용중인 닉네임입니다: " + user.getNickname());
            }
        }

        // 수정 시간 설정
        user.setUpdatedAt(LocalDateTime.now());

        // 데이터베이스 업데이트
        int result = adminUserDao.updateUser(user);
        if (result <= 0) {
            throw new RuntimeException("사용자 정보 수정에 실패했습니다.");
        }

        // 수정된 사용자 정보 반환
        AdminUser updatedUser = adminUserDao.findUserById(user.getUser_id());

        log.info("사용자 정보 수정 완료 - userId: {}, name: {}, nickname: {}",
                user.getUser_id(), updatedUser.getName(), updatedUser.getNickname());

        return updatedUser;
    }

    /**
     * 사용자 논리 삭제
     * @param userId 삭제할 사용자 ID
     */
    public void deleteUser(Integer userId) {
        log.info("사용자 삭제 시작 - userId: {}", userId);

        // 입력값 검증
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID가 필요합니다.");
        }

        if (userId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다: " + userId);
        }

        // 사용자 존재 여부 확인
        AdminUser user = adminUserDao.findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + userId);
        }

        // 이미 삭제된 사용자인지 확인
        if (user.isDeleted()) {
            throw new IllegalStateException("이미 삭제된 사용자입니다: " + userId);
        }

        // 관리자는 삭제할 수 없도록 보호
        if (user.isAdmin()) {
            throw new IllegalStateException("관리자 계정은 삭제할 수 없습니다.");
        }

        // 논리 삭제 실행
        int result = adminUserDao.deleteUser(userId);
        if (result <= 0) {
            throw new RuntimeException("사용자 삭제에 실패했습니다.");
        }

        log.info("사용자 삭제 완료 - userId: {}, email: {}, nickname: {}",
                userId, user.getEmail(), user.getNickname());
    }

    /**
     * 닉네임 중복 확인 (특정 사용자 제외)
     * @return 중복 여부 (true: 중복됨, false: 사용 가능)
     */
    @Transactional(readOnly = true)
    public boolean isNicknameDuplicate(String nickname, Integer excludeUserId) {
        log.debug("닉네임 중복 확인 - nickname: {}, excludeUserId: {}", nickname, excludeUserId);

        if (nickname == null || nickname.trim().isEmpty()) {
            return false;
        }

        // 닉네임 길이 검증
        String trimmedNickname = nickname.trim();
        if (trimmedNickname.length() < 2 || trimmedNickname.length() > 10) {
            throw new IllegalArgumentException("닉네임은 2~10글자로 입력해주세요.");
        }

        boolean isDuplicate = adminUserDao.existsByNickname(trimmedNickname, excludeUserId);

        log.debug("닉네임 중복 확인 결과 - nickname: {}, isDuplicate: {}", trimmedNickname, isDuplicate);

        return isDuplicate;
    }

    /**
     * 사용자 통계 정보 조회
     * @return 사용자 관련 통계 데이터
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getUserStats() {
        log.debug("사용자 통계 정보 조회 시작");

        try {
            Map<String, Object> stats = new HashMap<>();

            // 전체 사용자 수 (삭제되지 않은)
            int totalUsers = adminUserDao.getTotalUserCount(null);

            // 탈퇴 사용자 수
            int deletedUsers = adminUserDao.getTotalDeletedUserCount(null);

            // 활성 사용자 수 계산
            int activeUsers = Math.max(0, totalUsers);

            // 오늘 가입한 사용자 수
            int todaySignups = adminUserDao.getTodaySignupCount();

            // 이번달 가입한 사용자 수
            int monthlySignups = adminUserDao.getMonthlySignupCount();

            // 역할별 통계
            List<Map<String, Object>> roleStats = adminUserDao.getUserRoleStats();

            // 등급별 통계
            List<Map<String, Object>> gradeStats = adminUserDao.getUserGradeStats();

            // 결과 설정
            stats.put("totalUsers", totalUsers);
            stats.put("deletedUsers", deletedUsers);
            stats.put("activeUsers", activeUsers);
            stats.put("todaySignups", todaySignups);
            stats.put("monthlySignups", monthlySignups);
            stats.put("roleStats", roleStats);
            stats.put("gradeStats", gradeStats);

            log.debug("사용자 통계 조회 완료 - 전체: {}, 활성: {}, 탈퇴: {}, 오늘가입: {}, 월간가입: {}",
                    totalUsers, activeUsers, deletedUsers, todaySignups, monthlySignups);

            return stats;

        } catch (Exception e) {
            log.error("사용자 통계 조회 중 오류 발생", e);
            throw new RuntimeException("통계 정보를 불러오는데 실패했습니다.", e);
        }
    }

    /**
     * 사용자 검색 (닉네임 또는 이름으로)
     */
    @Transactional(readOnly = true)
    public List<AdminUser> searchUsers(String keyword, int limit) {
        log.debug("사용자 검색 - keyword: {}, limit: {}", keyword, limit);

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("검색 키워드가 필요합니다.");
        }

        if (limit <= 0) {
            limit = 10;
        }

        // 검색 실행 (페이징을 이용해서 limit 적용)
        Map<String, Object> result = getUserList(1, limit, keyword.trim());

        @SuppressWarnings("unchecked")
        List<AdminUser> users = (List<AdminUser>) result.get("users");

        log.debug("사용자 검색 완료 - keyword: {}, 결과 수: {}", keyword, users.size());

        return users;
    }
}