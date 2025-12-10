package com.example.music_diary.models.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "songs", uniqueConstraints = @UniqueConstraint(columnNames = {"title", "artist"}))
public class Song extends BaseEntity {
    private String title;
    private String artist;
    private String description;
    private String tabs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "song")
    private Set<PracticeSession> practiceSessions;

    public Song(String title, String artist, User author) {
        this.title = title;
        this.artist = artist;
        this.author = author;
        this.practiceSessions = new HashSet<>();
    }

    public Song() {}

    @Column(name = "title", length = 200, nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "artist", length = 150, nullable = false)
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Column(name = "description", columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "tabs", columnDefinition = "TEXT")
    public String getTabs() {
        return tabs;
    }

    public void setTabs(String tabs) {
        this.tabs = tabs;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<PracticeSession> getPracticeSessions() {
        return practiceSessions;
    }

    public void setPracticeSessions(Set<PracticeSession> practiceSessions) {
        this.practiceSessions = practiceSessions;
    }

//    @Override
//    public String toString() {
//        return "Song{" +
//                "title='" + title + '\'' +
//                ", artist='" + artist + '\'' +
//                ", description='" + description + '\'' +
//                ", tabs='" + tabs + '\'' +
//                ", author=" + author +
//                '}';
//    }
}