package com.example.music_diary.models.exceptions;

public class SongIsInSessionUseException extends RuntimeException{
    public SongIsInSessionUseException(String message) {
        super(message);
    }
}
