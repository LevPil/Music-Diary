package com.example.music_diary.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class AddArticleDto implements Serializable {
    private String title;
    private String content;

    @NotEmpty(message = "Заголовок статьи не должен быть пустым!")
    @Size(min = 1, max = 256, message = "Заголовок статьи от 1 до 256 символов!")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @NotEmpty(message = "Содержимое статьи не должно быть пустым!")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
