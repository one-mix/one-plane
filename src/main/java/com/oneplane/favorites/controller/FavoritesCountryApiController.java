// 작성자: 오수경
package com.oneplane.favorites.controller;

import com.oneplane.favorites.domain.FavoritesCountry;
import com.oneplane.favorites.service.FavoritesCountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/favorites/api")
public class FavoritesCountryApiController {

    private final FavoritesCountryService favoritesCountryService;

    public FavoritesCountryApiController(FavoritesCountryService favoritesCountryService) {
        this.favoritesCountryService = favoritesCountryService;
    }

    @GetMapping("/list/{userId}")
    public List<FavoritesCountry> getFavorites(@PathVariable Long userId) {
        return favoritesCountryService.getFavoritesByUser(userId);
    }
}
