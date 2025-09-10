package com.oneplane.favorites.controller;

import com.oneplane.favorites.domain.FavoritesCountry;
import com.oneplane.favorites.service.FavoritesCountryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/favorites")
public class FavoritesCountryController {

    private final FavoritesCountryService favoritesCountryService;

    public FavoritesCountryController(FavoritesCountryService favoritesCountryService) {
        this.favoritesCountryService = favoritesCountryService;
    }

    @PostMapping("/add")
    public String addFavorite(@RequestParam Long countryId, @RequestParam Long userId) {
        favoritesCountryService.addFavorite(countryId, userId);
        return "redirect:/favorites/list?userId=" + userId;
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