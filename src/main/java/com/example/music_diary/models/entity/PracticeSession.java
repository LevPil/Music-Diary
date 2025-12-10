package com.example.music_diary.models.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "practice_sessions")
public class PracticeSession extends BaseEntity {
    private LocalDate date;
    private int durationMinutes;
    private int repetitions;
    private String exercise;
    private String notes;
    private String tab;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id")
    private Song song;

    public PracticeSession(LocalDate date, Integer durationMinutes, User user) {
        this.date = date;
        this.durationMinutes = durationMinutes;
        this.user = user;
    }

    public PracticeSession() {}

    @Column(name = "date", nullable = false)
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Column(name = "duration_minutes", nullable = false)
    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Column(name = "repetitions")
    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    @Column(name = "exercise", columnDefinition = "TEXT")
    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    @Column(name = "notes", columnDefinition = "TEXT")
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Column(name = "tab", columnDefinition = "TEXT")
    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    @Column(name = "created_at", nullable = false)
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

//    @Override
//    public String toString() {
//        return "PracticeSession{" +
//                "id=" + getId() +
//                ", date=" + date +
//                ", durationMinutes=" + durationMinutes +
//                ", user=" + user.getUsername() +
//                ", song=" + (song != null ? song.getTitle() : "exercise") +
//                '}';
//    }
}
