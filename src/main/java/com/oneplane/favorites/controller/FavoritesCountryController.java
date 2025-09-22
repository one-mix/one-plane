// 작성자: 오수경
package com.oneplane.favorites.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.favorites.domain.FavoritesCountry;
import com.oneplane.favorites.service.FavoritesCountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 즐겨찾기 국가 컨트롤러 (뷰 렌더링 + 일부 JSON)
 * - 사용자 즐겨찾기 등록/삭제/조회 담당
 */
@Slf4j
@Controller
@RequestMapping("/favorites")
public class FavoritesCountryController {

    private final FavoritesCountryService favoritesCountryService;

    public FavoritesCountryController(FavoritesCountryService favoritesCountryService) {
        this.favoritesCountryService = favoritesCountryService;
    }

    /**
     * 즐겨찾기 추가
     * 현재 로그인한 사용자 ID를 SecurityUtil에서 추출
     * @param countryId 추가할 국가 PK
     * @return "success" 문자열(JSON)
     */
    @PostMapping("/add")
    @ResponseBody
    public String addFavorite(@RequestParam Long countryId) {
        Long userId = SecurityUtil.getCurrentUserId().longValue();
        log.info("현재 로그인한 사용자 ID: {}", userId);
        log.info("즐겨찾기 추가 요청 - userId: {}, countryId: {}", userId, countryId);
        favoritesCountryService.addFavorite(userId, countryId);
        return "success";
    }

    /**
     * 즐겨찾기 삭제 후 목록 페이지로 리다이렉트
     * @param favoritesCountryId 즐겨찾기 레코드 PK
     * @param userId 사용자 ID
     * @return redirect URL
     */
    @PostMapping("/remove")
    public String removeFavorite(@RequestParam Long favoritesCountryId,
                                 @RequestParam Long userId) {
        favoritesCountryService.removeFavorite(favoritesCountryId);
        return "redirect:/favorites/list?userId=" + userId;
    }

    /**
     * 특정 사용자 즐겨찾기 목록을 JSP 뷰로 렌더링
     * @param userId 사용자 ID
     * @param model  뷰 모델
     * @return 레이아웃 JSP 경로
     */
    @GetMapping("/list/{userId}")
    public String listFavorites(@PathVariable Long userId, Model model) {
        List<FavoritesCountry> favorites = favoritesCountryService.getFavoritesByUser(userId);
        model.addAttribute("favorites", favorites);
        model.addAttribute("contentPage", "favorites/list.jsp");
        return "layout/layout";
    }
}
