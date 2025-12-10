package com.example.music_diary.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;


public class AddSongDto implements Serializable {
    private String title;
    private String artist;
    private String description;
    private String tabs;

    @NotEmpty(message = "Название песни не должно быть пустым!")
    @Size(min = 1, max = 256, message = "Название песни от 1 до 256 символов!")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotEmpty(message = "Название исполнителя не должно быть пустым!")
    @Size(min = 1, max = 256, message = "Название исполнителя от 1 до 256 символов!")
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTabs() {
        return tabs;
    }

    public void setTabs(String tabs) {
        this.tabs = tabs;
    }
}
