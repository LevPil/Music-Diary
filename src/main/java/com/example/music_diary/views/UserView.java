package com.example.music_diary.views;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserView implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private Integer currentStreak;
    private Integer totalPracticeMinutes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getTotalPracticeMinutes() {
        return totalPracticeMinutes;
    }

    public void setTotalPracticeMinutes(Integer totalPracticeMinutes) {
        this.totalPracticeMinutes = totalPracticeMinutes;
    }
}
