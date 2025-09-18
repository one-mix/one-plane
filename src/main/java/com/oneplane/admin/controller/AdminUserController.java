// 작성자: 김동현
package com.oneplane.admin.controller;

import com.oneplane.admin.domain.AdminUser;
import com.oneplane.admin.service.AdminUserService;
import com.oneplane.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * 전체 사용자 목록 페이지
     */
    @GetMapping("/userList")
    public String userList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "grade", required = false) String grade,
            Model model) {

        log.info("전체 사용자 목록 조회 - page: {}, search: {}, role: {}, grade: {}", page, search, role, grade);

        try {
            // 페이지 크기 (한 페이지당 20개)
            int pageSize = 20;

            Map<String, Object> result = adminUserService.getUserList(page, pageSize, search);

            Map<String, Object> stats = new HashMap<>();
            try {
                stats = adminUserService.getUserStats();
            } catch (Exception e) {
                stats.put("totalUsers", result.get("totalCount") != null ? result.get("totalCount") : 0);
                stats.put("activeUsers", result.get("totalCount") != null ? result.get("totalCount") : 0);
                stats.put("monthlySignups", 0);
                stats.put("deletedUsers", 0);
            }

            // 모델에 데이터 추가
            model.addAttribute("users", result.get("users"));
            model.addAttribute("currentPage", result.get("currentPage"));
            model.addAttribute("totalPages", result.get("totalPages"));
            model.addAttribute("totalCount", result.get("totalCount"));
            model.addAttribute("hasNext", result.get("hasNext"));
            model.addAttribute("hasPrevious", result.get("hasPrevious"));
            model.addAttribute("startRow", result.get("startRow"));
            model.addAttribute("endRow", result.get("endRow"));
            model.addAttribute("search", search);
            model.addAttribute("role", role);
            model.addAttribute("grade", grade);
            model.addAttribute("stats", stats);

            // 검색 파라미터 문자열 생성 (페이징에 사용)
            StringBuilder searchParams = new StringBuilder();
            if (search != null && !search.trim().isEmpty()) {
                searchParams.append("&search=").append(search);
            }
            if (role != null && !role.trim().isEmpty()) {
                searchParams.append("&role=").append(role);
            }
            if (grade != null && !grade.trim().isEmpty()) {
                searchParams.append("&grade=").append(grade);
            }
            model.addAttribute("searchParams", searchParams.toString());

            // 페이지 정보
            model.addAttribute("contentPage", "userList.jsp");
            model.addAttribute("activeMenu", "userList");

        } catch (Exception e) {
            log.error("사용자 목록 조회 중 오류 발생", e);
            model.addAttribute("error", "사용자 목록을 불러오는데 실패했습니다: " + e.getMessage());

            // 오류 시에도 기본 데이터 설정
            model.addAttribute("users", List.of());
            model.addAttribute("totalCount", 0);
            model.addAttribute("stats", Map.of(
                    "totalUsers", 0,
                    "activeUsers", 0,
                    "todaySignups", 0,
                    "monthlySignups", 0
            ));
        }

        return "admin/layout/adminLayout";
    }

    /**
     * 사용자 상세보기 페이지
     */
    @GetMapping("/userDetail/{userId}")
    public String userDetail(@PathVariable Integer userId, Model model) {
            AdminUser user = adminUserService.getUserById(userId);

            model.addAttribute("user", user);
            model.addAttribute("contentPage", "userDetail.jsp");
            model.addAttribute("activeMenu", "userList");

        return "admin/layout/adminLayout";
    }

    /**
     * 사용자 삭제 (논리 삭제)
     */
    @PostMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable Integer userId) {
        log.info("사용자 삭제 - userId: {}", userId);
        adminUserService.deleteUser(userId);
        return "redirect:/admin/userList";
    }

    /**
     * 탈퇴 사용자 목록 페이지
     */
    @GetMapping("/userDeleted")
    public String deletedUsers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "search", required = false) String search,
            Model model) {
            int pageSize = 20;
            Map<String, Object> result = adminUserService.getDeletedUserList(page, pageSize, search);

            model.addAttribute("users", result.get("users"));
            model.addAttribute("currentPage", result.get("currentPage"));
            model.addAttribute("totalPages", result.get("totalPages"));
            model.addAttribute("totalCount", result.get("totalCount"));
            model.addAttribute("hasNext", result.get("hasNext"));
            model.addAttribute("hasPrevious", result.get("hasPrevious"));
            model.addAttribute("startRow", result.get("startRow"));
            model.addAttribute("endRow", result.get("endRow"));
            model.addAttribute("search", search);

            // 검색 파라미터 문자열 생성
            String searchParams = "";
            if (search != null && !search.trim().isEmpty()) {
                searchParams = "&search=" + search;
            }
            model.addAttribute("searchParams", searchParams);

            model.addAttribute("contentPage", "userDeleted.jsp");
            model.addAttribute("activeMenu", "user-del");

        return "admin/layout/adminLayout";
    }

    /**
     * 사용자 통계 대시보드 페이지
     */
    @GetMapping("/dashboard/user")
    public String userDashboard(Model model) {
        log.info("사용자 통계 대시보드 페이지 요청");

        try {
            // 기본 통계 데이터 조회
            Map<String, Object> stats = adminUserService.getUserStats();
            model.addAttribute("stats", stats);

            log.info("사용자 통계 대시보드 데이터 로드 완료");

        } catch (Exception e) {
            log.error("사용자 통계 대시보드 데이터 로드 실패", e);
            model.addAttribute("error", "통계 데이터를 불러오는데 실패했습니다.");
        }

        // 페이지 정보
        model.addAttribute("contentPage", "userDashboard.jsp");
        model.addAttribute("activeMenu", "user");

        return "admin/layout/adminLayout";
    }

    /**
     * 성별별 통계 API
     */
    @GetMapping("/api/users/gender-stats")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getGenderStats() {
        log.info("성별별 사용자 통계 API 호출");

        try {
            List<Map<String, Object>> stats = adminUserService.getGenderStats();

            // 디버깅을 위한 로그 추가
            log.info("성별 통계 결과: {}", stats);
            for (Map<String, Object> stat : stats) {
                log.info("Gender: {}, Count: {}", stat.get("gender"), stat.get("count"));
            }

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("성별별 통계 조회 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 연령대별 통계 API
     */
    @GetMapping("/api/users/age-stats")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAgeStats() {
        log.info("연령대별 사용자 통계 API 호출");

        try {
            List<Map<String, Object>> stats = adminUserService.getAgeStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("연령대별 통계 조회 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 등급별 통계 API
     */
    @GetMapping("/api/users/grade-stats")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getGradeStats() {
        log.info("등급별 사용자 통계 API 호출");

        try {
            List<Map<String, Object>> stats = adminUserService.getGradeStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("등급별 통계 조회 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 건강 정보별 통계 API
     */
    @GetMapping("/api/users/health-stats")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getHealthStats() {
        log.info("건강 정보별 사용자 통계 API 호출");

        try {
            List<Map<String, Object>> stats = adminUserService.getHealthStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("건강 정보별 통계 조회 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 월별 가입 추이 API
     */
    @GetMapping("/api/users/monthly-trend")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getMonthlyTrend() {
        log.info("월별 가입 추이 API 호출");

        try {
            List<Map<String, Object>> stats = adminUserService.getMonthlySignupTrend();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("월별 가입 추이 조회 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}