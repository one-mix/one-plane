// 작성자: 김동현
package com.oneplane.friend.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.friend.domain.Friend;
import com.oneplane.friend.service.FriendService;
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
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    /**
     * 친구 목록 페이지
     * 작성자 : 김동현
     */
    @GetMapping
    public String friendsPage(@RequestParam(defaultValue = "following") String tab,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(required = false) String search,
                              Model model) {

        Integer currentUserId = SecurityUtil.getCurrentUserId();
        int size = 16; // 페이지당 16명

        // 페이지네이션 계산용 변수들
        int totalCount = 0;
        int totalPages = 0;
        int startPage = 1;
        int endPage = 1;

        if (search != null && !search.trim().isEmpty()) {
            // 검색 모드
            try {
                List<Friend> searchResults = friendService.searchUsers(search, currentUserId, page, size);
                totalCount = friendService.getSearchResultCount(search, currentUserId);
                totalPages = (int) Math.ceil((double) totalCount / size);

                model.addAttribute("searchResults", searchResults);
                model.addAttribute("search", search);
                model.addAttribute("tab", "search");

            } catch (Exception e) {
                log.error("사용자 검색 중 오류 발생", e);
                model.addAttribute("searchResults", List.of());
                model.addAttribute("search", search);
                model.addAttribute("tab", "search");
                model.addAttribute("errorMessage", "검색 중 오류가 발생했습니다.");
            }

        } else {
            // 일반 친구 목록
            try {
                List<Friend> friendList;

                if ("followers".equals(tab)) {
                    friendList = friendService.getFollowerList(currentUserId, page, size);
                    totalCount = friendService.getFollowerCount(currentUserId);
                } else {
                    // 기본값은 following
                    tab = "following";
                    friendList = friendService.getFollowingList(currentUserId, page, size);
                    totalCount = friendService.getFollowingCount(currentUserId);
                }

                totalPages = (int) Math.ceil((double) totalCount / size);

                model.addAttribute("friendList", friendList);
                model.addAttribute("tab", tab);

            } catch (Exception e) {
                log.error("친구 목록 조회 중 오류 발생", e);
                model.addAttribute("friendList", List.of());
                model.addAttribute("tab", tab);
                model.addAttribute("errorMessage", "친구 목록을 불러오는 중 오류가 발생했습니다.");
            }
        }

        // 페이지네이션 정보 계산
        if (totalPages > 0) {
            startPage = Math.max(1, page - 2);
            endPage = Math.min(totalPages, page + 2);
        }

        // 공통 모델 속성
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("contentPage", "friend/friends.jsp");
        model.addAttribute("activeMenu", "following");
        model.addAttribute("showSidebar", true);

        // 통계 정보 (사이드바용)
        try {
            int followingCount = friendService.getFollowingCount(currentUserId);
            int followerCount = friendService.getFollowerCount(currentUserId);

            model.addAttribute("followingCount", followingCount);
            model.addAttribute("followerCount", followerCount);

        } catch (Exception e) {
            log.error("팔로우 통계 조회 중 오류 발생", e);
            model.addAttribute("followingCount", 0);
            model.addAttribute("followerCount", 0);
        }

        return "layout/layout";
    }

    /**
     * 팔로우/언팔로우 처리 (AJAX)
     * 작성자 : 김동현
     */
    @PostMapping("/toggle")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleFollow(@RequestParam Integer targetUserId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Integer currentUserId = SecurityUtil.getCurrentUserId();

            // 자기 자신 팔로우 방지
            if (currentUserId.equals(targetUserId)) {
                response.put("success", false);
                response.put("message", "자기 자신을 팔로우할 수 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            boolean isFollowing = friendService.isFollowing(currentUserId, targetUserId);

            if (isFollowing) {
                friendService.unfollow(currentUserId, targetUserId);
                response.put("action", "unfollowed");
                response.put("message", "언팔로우했습니다.");
            } else {
                friendService.follow(currentUserId, targetUserId);
                response.put("action", "followed");
                response.put("message", "팔로우했습니다.");
            }

            response.put("success", true);
            response.put("isFollowing", !isFollowing);

        } catch (IllegalArgumentException e) {
            log.warn("팔로우/언팔로우 처리 실패 - 잘못된 요청: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("팔로우/언팔로우 처리 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "팔로우 처리 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 팔로우 상태 확인 (AJAX)
     * 작성자 : 김동현
     */
    @GetMapping("/status/{targetUserId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getFollowStatus(@PathVariable Integer targetUserId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Integer currentUserId = SecurityUtil.getCurrentUserId();
            boolean isFollowing = friendService.isFollowing(currentUserId, targetUserId);

            response.put("success", true);
            response.put("isFollowing", isFollowing);

        } catch (Exception e) {
            log.error("팔로우 상태 확인 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "상태 확인에 실패했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 팔로우 통계 정보 조회 (AJAX)
     * 작성자 : 김동현
     */
    @GetMapping("/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getFollowStats() {
        Map<String, Object> response = new HashMap<>();

        try {
            Integer currentUserId = SecurityUtil.getCurrentUserId();

            int followingCount = friendService.getFollowingCount(currentUserId);
            int followerCount = friendService.getFollowerCount(currentUserId);

            response.put("success", true);
            response.put("followingCount", followingCount);
            response.put("followerCount", followerCount);

        } catch (Exception e) {
            log.error("팔로우 통계 조회 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "통계 조회에 실패했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 검색 자동완성 (AJAX)
     * 작성자 : 김동현
     */
    @GetMapping("/search/autocomplete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchAutocomplete(@RequestParam String query) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (query == null || query.trim().length() < 2) {
                response.put("success", true);
                response.put("suggestions", List.of());
                return ResponseEntity.ok(response);
            }

            Integer currentUserId = SecurityUtil.getCurrentUserId();

            List<Friend> suggestions = friendService.searchUsers(query.trim(), currentUserId, 1, 5);

            response.put("success", true);
            response.put("suggestions", suggestions);

        } catch (Exception e) {
            log.error("자동완성 검색 중 오류 발생", e);
            response.put("success", false);
            response.put("message", "검색 중 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok(response);
    }
}