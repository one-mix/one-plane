// 작성자: 오수경
package com.oneplane.favorites.controller;

import com.oneplane.favorites.domain.FavoritesCountry;
import com.oneplane.favorites.service.FavoritesCountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 즐겨찾기 국가 API 컨트롤러
 * - REST(JSON) 방식으로 즐겨찾기 목록을 제공
 */
@RestController
@RequestMapping("/favorites/api")
public class FavoritesCountryApiController {

    private final FavoritesCountryService favoritesCountryService;

    public FavoritesCountryApiController(FavoritesCountryService favoritesCountryService) {
        this.favoritesCountryService = favoritesCountryService;
    }

    /**
     * 특정 사용자(userId)의 즐겨찾기 국가 목록 조회
     * @param userId 로그인 사용자 ID
     * @return 즐겨찾기 국가 리스트(JSON)
     */
    @GetMapping("/list/{userId}")
    public List<FavoritesCountry> getFavorites(@PathVariable Long userId) {
        return favoritesCountryService.getFavoritesByUser(userId);
    }
}
