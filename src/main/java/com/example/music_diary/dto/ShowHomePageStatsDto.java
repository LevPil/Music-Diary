package com.example.music_diary.dto;

import java.io.Serializable;
import java.util.List;

public class ShowHomePageStatsDto implements Serializable {
    private Long activeUsersToday;
    private Long totalPracticeMinutesToday;
    private List<ShowTopItemDto> topSongsThisWeek;
    private String topStreakUsername;
    private Integer topStreakDays;
    private String topProgressUsername;
    private Integer topProgressValue;
    private List<ShowArticleInfoDto> popularArticlesThisWeek;

    public Long getActiveUsersToday() {
        return activeUsersToday;
    }

    public void setActiveUsersToday(Long activeUsersToday) {
        this.activeUsersToday = activeUsersToday;
    }

    public Long getTotalPracticeMinutesToday() {
        return totalPracticeMinutesToday;
    }

    public void setTotalPracticeMinutesToday(Long totalPracticeMinutesToday) {
        this.totalPracticeMinutesToday = totalPracticeMinutesToday;
    }

    public List<ShowTopItemDto> getTopSongsThisWeek() {
        return topSongsThisWeek;
    }

    public void setTopSongsThisWeek(List<ShowTopItemDto> topSongsThisWeek) {
        this.topSongsThisWeek = topSongsThisWeek;
    }

    public String getTopStreakUsername() {
        return topStreakUsername;
    }

    public void setTopStreakUsername(String topStreakUsername) {
        this.topStreakUsername = topStreakUsername;
    }

    public Integer getTopStreakDays() {
        return topStreakDays;
    }

    public void setTopStreakDays(Integer topStreakDays) {
        this.topStreakDays = topStreakDays;
    }

    public String getTopProgressUsername() {
        return topProgressUsername;
    }

    public void setTopProgressUsername(String topProgressUsername) {
        this.topProgressUsername = topProgressUsername;
    }

    public Integer getTopProgressValue() {
        return topProgressValue;
    }

    public void setTopProgressValue(Integer topProgressValue) {
        this.topProgressValue = topProgressValue;
    }

    public List<ShowArticleInfoDto> getPopularArticlesThisWeek() {
        return popularArticlesThisWeek;
    }

    public void setPopularArticlesThisWeek(List<ShowArticleInfoDto> popularArticlesThisWeek) {
        this.popularArticlesThisWeek = popularArticlesThisWeek;
    }
}
