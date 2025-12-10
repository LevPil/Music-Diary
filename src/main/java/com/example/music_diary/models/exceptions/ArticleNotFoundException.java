package com.example.music_diary.models.exceptions;

public class ArticleNotFoundException extends RuntimeException{

    public ArticleNotFoundException(String message){
        super(message);
    }
    public ArticleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
