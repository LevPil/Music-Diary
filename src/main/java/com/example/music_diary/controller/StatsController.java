package com.example.music_diary.controller;

import com.example.music_diary.service.StatisticService;
import com.example.music_diary.dto.ShowPracticeStatsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Slf4j
@Controller
public class StatsController {

    private final StatisticService statisticsService;

    public StatsController(StatisticService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics")
    public String showStatistics(Principal principal, Model model) {
        log.debug("Отображение статистики для пользователя: {}", principal.getName());
        ShowPracticeStatsDto stats = statisticsService.getUserStats(principal.getName());
        model.addAttribute("stats", stats);
        return "statistics";
    }
}