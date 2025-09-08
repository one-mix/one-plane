package com.oneplane.alert.controller;

import com.oneplane.alert.domain.AlertLevel;
import com.oneplane.alert.service.AlertLevelService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alerts")
public class AlertLevelController {
    private final AlertLevelService alertLevelService;

    public AlertLevelController(AlertLevelService alertLevelService) {
        this.alertLevelService = alertLevelService;
    }

    @GetMapping("/{countryId}")
    public AlertLevel getAlert(@PathVariable Long countryId) {
        return alertLevelService.getAlertByCountryId(countryId);
    }

    @PostMapping("/add")
    public String addAlert(@RequestBody AlertLevel alertLevel) {
        alertLevelService.saveAlert(alertLevel);
        return "등록 완료";
    }
}
