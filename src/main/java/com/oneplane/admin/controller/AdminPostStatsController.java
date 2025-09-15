package com.oneplane.admin.controller;

import com.oneplane.admin.service.PostStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminPostStatsController {

    private final PostStatsService postStatsService;


    /**
     * 게시물 통계 대시보드 페이지
     */
    @GetMapping("/dashboard/post")
    public String postStats(Model model) {
        log.info("게시물 통계 대시보드 페이지 요청");

        try {
            // 기본 통계 데이터 조회 (페이지 로드시 표시할 데이터)
            Map<String, Object> basicStats = postStatsService.getBasicStats();
            model.addAttribute("stats", basicStats);

            // 페이지 정보
            model.addAttribute("contentPage", "postDashboard.jsp");
            model.addAttribute("activeMenu", "stats-post");

            log.info("게시물 통계 대시보드 페이지 로드 완료");
            return "admin/layout/adminLayout";

        } catch (Exception e) {
            log.error("게시물 통계 대시보드 로드 중 오류 발생", e);
            model.addAttribute("error", "통계 데이터를 불러오는데 실패했습니다.");
            return "admin/layout/adminLayout";
        }
    }

    /**
     * API: 카테고리별 게시글 분포 통계
     */
    @GetMapping("/api/posts/category-stats")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getCategoryStats() {
        log.info("카테고리별 게시글 분포 통계 API 요청");

        try {
            List<Map<String, Object>> categoryStats = postStatsService.getCategoryStats();
            return ResponseEntity.ok(categoryStats);

        } catch (Exception e) {
            log.error("카테고리별 통계 API 오류", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API: 월별 게시글 작성 추이
     */
    @GetMapping("/api/posts/monthly-trend")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getMonthlyTrend() {
        log.info("월별 게시글 작성 추이 API 요청");

        try {
            List<Map<String, Object>> monthlyTrend = postStatsService.getMonthlyPostTrend();
            return ResponseEntity.ok(monthlyTrend);

        } catch (Exception e) {
            log.error("월별 추이 통계 API 오류", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API: 인기 게시글 TOP 10
     */
    @GetMapping("/api/posts/popular-top10")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getPopularTop10() {
        log.info("인기 게시글 TOP 10 API 요청");

        try {
            Map<String, Object> result = Map.of(
                    "popularPosts", postStatsService.getTop10PopularPosts()
            );
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("인기 게시글 TOP 10 API 오류", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API: 최근 7일간 활동 현황
     */
    @GetMapping("/api/posts/daily-activity")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDailyActivity() {
        log.info("최근 7일간 활동 현황 API 요청");

        try {
            Map<String, Object> dailyActivity = postStatsService.getDailyActivityStats();
            return ResponseEntity.ok(dailyActivity);

        } catch (Exception e) {
            log.error("일별 활동 통계 API 오류", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API: 전체 통계 데이터 (한번에 모든 차트 데이터)
     */
    @GetMapping("/api/posts/all-stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllStats() {
        log.info("전체 게시물 통계 데이터 API 요청");

        try {
            Map<String, Object> allStats = postStatsService.getAllStats();
            return ResponseEntity.ok(allStats);

        } catch (Exception e) {
            log.error("전체 통계 데이터 API 오류", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API: 기본 통계 데이터만 (실시간 갱신용)
     */
    @GetMapping("/api/posts/basic-stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getBasicStats() {
        log.info("기본 통계 데이터 API 요청");

        try {
            Map<String, Object> basicStats = postStatsService.getBasicStats();
            return ResponseEntity.ok(basicStats);

        } catch (Exception e) {
            log.error("기본 통계 데이터 API 오류", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
