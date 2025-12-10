package com.example.music_diary.service;

import com.example.music_diary.dto.ShowPracticeStatsDto;

public interface StatisticService {

    ShowPracticeStatsDto getUserStats(String username);
}
