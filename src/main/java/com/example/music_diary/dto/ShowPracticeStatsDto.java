package com.example.music_diary.dto;

import java.io.Serializable;
import java.util.List;

public class ShowPracticeStatsDto implements Serializable {
    private Integer totalMinutesLast7Days;
    private Long activeDaysLast7Days;
    private Double averageDuration;
    private List<ShowTopItemDto> topSongs;
    private Integer lastWeekMinutes;
    private Integer currentWeekMinutes;
    private String weeklyTrend;


    public ShowPracticeStatsDto(Integer totalMinutesLast7Days, Long activeDaysLast7Days, Double averageDuration, List<ShowTopItemDto> topSongs, Integer lastWeekMinutes, Integer currentWeekMinutes, String weeklyTrend) {
        this.totalMinutesLast7Days = totalMinutesLast7Days;
        this.activeDaysLast7Days = activeDaysLast7Days;
        this.averageDuration = averageDuration;
        this.topSongs = topSongs;
        this.lastWeekMinutes = lastWeekMinutes;
        this.currentWeekMinutes = currentWeekMinutes;
        this.weeklyTrend = weeklyTrend;
    }

    public Integer getTotalMinutesLast7Days() {
        return totalMinutesLast7Days;
    }

    public void setTotalMinutesLast7Days(Integer totalMinutesLast7Days) {
        this.totalMinutesLast7Days = totalMinutesLast7Days;
    }

    public Long getActiveDaysLast7Days() {
        return activeDaysLast7Days;
    }

    public void setActiveDaysLast7Days(Long activeDaysLast7Days) {
        this.activeDaysLast7Days = activeDaysLast7Days;
    }

    public Double getAverageDuration() {
        return averageDuration;
    }

    public void setAverageDuration(Double averageDuration) {
        this.averageDuration = averageDuration;
    }

    public List<ShowTopItemDto> getTopSongs() {
        return topSongs;
    }

    public void setTopSongs(List<ShowTopItemDto> topSongs) {
        this.topSongs = topSongs;
    }

    public Integer getLastWeekMinutes() {
        return lastWeekMinutes;
    }

    public void setLastWeekMinutes(Integer lastWeekMinutes) {
        this.lastWeekMinutes = lastWeekMinutes;
    }

    public Integer getCurrentWeekMinutes() {
        return currentWeekMinutes;
    }

    public void setCurrentWeekMinutes(Integer currentWeekMinutes) {
        this.currentWeekMinutes = currentWeekMinutes;
    }

    public String getWeeklyTrend() {
        return weeklyTrend;
    }

    public void setWeeklyTrend(String weeklyTrend) {
        this.weeklyTrend = weeklyTrend;
    }
}
