package com.oneplane.admin.controller;

import com.oneplane.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminUserService adminUserService;

        @GetMapping("/dashboard")
    public String adminMain(Model model) {
            model.addAttribute("contentPage", "dashboard.jsp");
            model.addAttribute("activeMenu", "dashboard");

        return "admin/layout/adminLayout";
    }

    /**
     * 전체 사용자 목록 페이지
     */
    @GetMapping
    public String users(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "search", required = false) String search,
            Model model) {

        log.info("전체 사용자 목록 조회 - page: {}, search: {}", page, search);

        try {
            // 페이지 크기 (한 페이지당 20개)
            int pageSize = 20;

            // 사용자 목록 조회
            Map<String, Object> result = adminUserService.getUserList(page, pageSize, search);

            // 모델에 데이터 추가
            model.addAttribute("users", result.get("users"));
            model.addAttribute("currentPage", result.get("currentPage"));
            model.addAttribute("totalPages", result.get("totalPages"));
            model.addAttribute("totalCount", result.get("totalCount"));
            model.addAttribute("hasNext", result.get("hasNext"));
            model.addAttribute("hasPrevious", result.get("hasPrevious"));
            model.addAttribute("search", search);

            // 페이지 정보
            model.addAttribute("contentPage", "users.jsp");
            model.addAttribute("activeMenu", "users");

        } catch (Exception e) {
            log.error("사용자 목록 조회 중 오류 발생", e);
            model.addAttribute("error", "사용자 목록을 불러오는데 실패했습니다.");
        }

        return "admin/layout/adminLayout";
    }

}
