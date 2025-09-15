package com.oneplane.favorites.controller;

import com.oneplane.config.SecurityUtil;
import com.oneplane.favorites.domain.FavoritesCountry;
import com.oneplane.favorites.service.FavoritesCountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/favorites")
public class FavoritesCountryController {

    private final FavoritesCountryService favoritesCountryService;

    public FavoritesCountryController(FavoritesCountryService favoritesCountryService) {
        this.favoritesCountryService = favoritesCountryService;
    }

    @PostMapping("/add")
    @ResponseBody
    public String addFavorite(@RequestParam Long countryId) {
        // 로그인한 사용자 ID 가져오기
        Long userId = SecurityUtil.getCurrentUserId().longValue();

        // 현재 세션 유저 ID 로깅
        log.info("현재 로그인한 사용자 ID: {}", userId);
        log.info("즐겨찾기 추가 요청 - userId: {}, countryId: {}", userId, countryId);


        favoritesCountryService.addFavorite(userId, countryId);
        return "success";
    }

    @PostMapping("/remove")
    public String removeFavorite(@RequestParam Long favoritesCountryId, @RequestParam Long userId) {
        favoritesCountryService.removeFavorite(favoritesCountryId);
        return "redirect:/favorites/list?userId=" + userId;
    }

    @GetMapping("/list/{userId}")
    public String listFavorites(@PathVariable Long userId, Model model) {
        List<FavoritesCountry> favorites = favoritesCountryService.getFavoritesByUser(userId);
        model.addAttribute("favorites", favorites);
        model.addAttribute("contentPage", "favorites/list.jsp");
        return "layout/layout";
    }
}