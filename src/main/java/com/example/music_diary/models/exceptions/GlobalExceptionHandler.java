package com.example.music_diary.models.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.ui.Model;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoResourceFound(NoResourceFoundException ex, Model model) {
        log.warn("Ресурс не найден: {}", ex.getResourcePath());
        model.addAttribute("errorTitle", "Ресурс не найден");
        model.addAttribute("errorMessage", "Файл " + ex.getResourcePath() + " не найден.");
        model.addAttribute("errorCode", "404");
        return "error/custom-error";
    }

    @ExceptionHandler(SongIsInSessionUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleSongInUse(SongIsInSessionUseException ex, Model model) {
        log.warn("Попытка удалить песню, которая используется: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Песня используется");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", "409");
        return "error/custom-error";
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Внутренняя ошибка сервера", ex);
        model.addAttribute("errorTitle", "Внутренняя ошибка сервера");
        model.addAttribute("errorMessage", "Произошла непредвиденная ошибка");
        model.addAttribute("errorCode", "500");
        return "error/custom-error";
    }
}