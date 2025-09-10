package com.oneplane.alert.controller;

import com.oneplane.alert.domain.AlertLevel;
import com.oneplane.alert.service.AlertLevelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class AlertLevelController {
    private final AlertLevelService alertLevelService;

    public AlertLevelController(AlertLevelService alertLevelService) {
        this.alertLevelService = alertLevelService;
    }

    @GetMapping("/all")
    public List<AlertLevel> getAllAlerts() {
        return alertLevelService.getAllAlerts();
    }

}
