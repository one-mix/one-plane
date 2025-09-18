// 작성자: 김동현
package com.oneplane.admin.service;

import com.oneplane.post.dao.CommentDao;
import com.oneplane.post.dao.PostDao;
import com.oneplane.post.dao.PostLikeDao;
import com.oneplane.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostStatsService {

    private final PostDao postDao;
    private final CommentDao commentDao;
    private final PostLikeDao postLikeDao;


    /**
     * 게시물 통계 대시보드용 기본 통계 데이터 조회
     */
    public Map<String, Object> getBasicStats() {
        log.info("게시물 기본 통계 데이터 조회 시작");

        try {
            Map<String, Object> stats = new HashMap<>();

            // 기본 통계 수치들
            stats.put("totalPosts", postDao.countAllPosts());
            stats.put("totalComments", commentDao.countAllComments());
            stats.put("totalLikes", postLikeDao.countAllPostLikes());
            stats.put("todayPosts", postDao.countTodayPosts());

            log.info("게시물 기본 통계 데이터 조회 완료: {}", stats);
            return stats;

        } catch (Exception e) {
            log.error("게시물 기본 통계 데이터 조회 중 오류 발생", e);
            throw new RuntimeException("통계 데이터를 불러오는데 실패했습니다.", e);
        }
    }

    /**
     * 카테고리별 게시글 분포 통계
     */
    public List<Map<String, Object>> getCategoryStats() {
        log.info("카테고리별 게시글 분포 통계 조회");

        try {
            List<Map<String, Object>> categoryStats = postDao.getCategoryStats();

            // 카테고리명 한글 변환
            categoryStats.forEach(stat -> {
                String category = (String) stat.get("CATEGORY");
                stat.put("CATEGORY_NAME", getCategoryDisplayName(category));
            });

            return categoryStats;

        } catch (Exception e) {
            log.error("카테고리별 통계 조회 중 오류 발생", e);
            throw new RuntimeException("카테고리별 통계를 불러오는데 실패했습니다.", e);
        }
    }

    /**
     * 월별 게시글 작성 추이 (최근 6개월)
     */
    public List<Map<String, Object>> getMonthlyPostTrend() {
        log.info("월별 게시글 작성 추이 조회");

        try {
            return postDao.getMonthlyPostTrend();
        } catch (Exception e) {
            log.error("월별 추이 통계 조회 중 오류 발생", e);
            throw new RuntimeException("월별 추이 통계를 불러오는데 실패했습니다.", e);
        }
    }

    /**
     * 인기 게시글 TOP 10
     */
    public List<Post> getTop10PopularPosts() {
        log.info("인기 게시글 TOP 10 조회");

        try {
            List<Post> popularPosts = postDao.getTop10PopularPosts();

            // 게시글 데이터 후처리
            popularPosts.forEach(this::processPostForDisplay);

            return popularPosts;

        } catch (Exception e) {
            log.error("인기 게시글 TOP 10 조회 중 오류 발생", e);
            throw new RuntimeException("인기 게시글을 불러오는데 실패했습니다.", e);
        }
    }

    /**
     * 최근 7일간 활동 현황 (게시글 + 댓글)
     */
    public Map<String, Object> getDailyActivityStats() {
        log.info("최근 7일간 활동 현황 조회");

        try {
            Map<String, Object> activityStats = new HashMap<>();

            List<Map<String, Object>> dailyPosts = postDao.getDailyPostStats();
            List<Map<String, Object>> dailyComments = commentDao.getDailyCommentStats();

            activityStats.put("dailyPosts", dailyPosts);
            activityStats.put("dailyComments", dailyComments);

            return activityStats;

        } catch (Exception e) {
            log.error("일별 활동 통계 조회 중 오류 발생", e);
            throw new RuntimeException("일별 활동 통계를 불러오는데 실패했습니다.", e);
        }
    }

    /**
     * 전체 통계 데이터 조회 (대시보드용)
     */
    public Map<String, Object> getAllStats() {
        log.info("전체 게시물 통계 데이터 조회");

        try {
            Map<String, Object> allStats = new HashMap<>();

            // 기본 통계
            allStats.put("basic", getBasicStats());

            // 카테고리별 분포
            allStats.put("category", getCategoryStats());

            // 월별 추이
            allStats.put("monthly", getMonthlyPostTrend());

            // 인기 게시글
            allStats.put("popular", getTop10PopularPosts());

            // 일별 활동
            allStats.put("daily", getDailyActivityStats());

            log.info("전체 게시물 통계 데이터 조회 완료");
            return allStats;

        } catch (Exception e) {
            log.error("전체 통계 데이터 조회 중 오류 발생", e);
            throw new RuntimeException("통계 데이터를 불러오는데 실패했습니다.", e);
        }
    }

    /**
     * 카테고리 코드를 한글 표시명으로 변환
     */
    private String getCategoryDisplayName(String category) {
        if (category == null) return "기타";

        switch (category.toUpperCase()) {
            case "READY":
                return "출국준비";
            case "REVIEW":
                return "여행후기";
            case "ACCOMPANY":
                return "동행구함";
            case "FREE":
                return "자유게시판";
            default:
                return category;
        }
    }

    /**
     * 게시글 표시용 데이터 후처리
     */
    private void processPostForDisplay(Post post) {
        // 제목이 너무 길면 줄임
        if (post.getTitle() != null && post.getTitle().length() > 50) {
            post.setTitle(post.getTitle().substring(0, 50) + "...");
        }

        // null 값들 기본값 설정
        if (post.getViewCount() == null) post.setViewCount(0);
        if (post.getLikeCount() == null) post.setLikeCount(0);
        if (post.getCommentCount() == null) post.setCommentCount(0);

        // 카테고리 한글명 설정
        if (post.getCategory() != null) {
        }
    }
}