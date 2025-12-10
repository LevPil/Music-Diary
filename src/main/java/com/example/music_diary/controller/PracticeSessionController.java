package com.example.music_diary.controller;

import com.example.music_diary.dto.AddPracticeSessionDto;
import com.example.music_diary.dto.ShowPracticeSessionDto;
import com.example.music_diary.service.PracticeSessionService;
import com.example.music_diary.service.SongService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
public class PracticeSessionController {
    private final PracticeSessionService practiceSessionService;
    private final SongService songService;

    public PracticeSessionController(PracticeSessionService practiceSessionService, SongService songService) {
        this.practiceSessionService = practiceSessionService;
        this.songService = songService;
    }


    @GetMapping("/diary")
    public String showAllSessions(Principal principal, Model model) {
        log.debug("Отображение списка тренировок для пользователя: {}", principal.getName());
        List<ShowPracticeSessionDto> sessions = practiceSessionService.getUserSessions(principal.getName());
        model.addAttribute("sessions", sessions);


        return "practice-session-all";
    }

    @GetMapping("/diary/add")
    public String addSession(Model model) {
        log.debug("Отображение формы добавления записи");
        model.addAttribute("availableSongs", songService.allSongs());
        return "practice-session-add";
    }

    @ModelAttribute("sessionModel")
    public AddPracticeSessionDto initSession() {
        return new AddPracticeSessionDto();
    }

    @PostMapping("/diary/add")
    public String addSession(@Valid @ModelAttribute("sessionModel") AddPracticeSessionDto sessionModel, BindingResult bindingResult, Principal principal, RedirectAttributes redirectAttributes) {
        log.debug("Обработка POST запроса на добавление тренировки");
        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", "Исправьте ошибки в форме");
            redirectAttributes.addFlashAttribute("sessionModel", sessionModel);
            return "redirect:/diary/add";
        }
        try {
            practiceSessionService.addSession(sessionModel, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Запись тренировки успешно добавлена!");
            return "redirect:/diary";
        } catch (Exception e) {
            log.error("Ошибка при добавлении тренировки", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось добавить запись: " + e.getMessage());
            return "redirect:/diary/add";
        }
    }


    @GetMapping("/diary/session-details/{id}")
    public String sessionDetails(@PathVariable("id") Long id, Model model) {
        log.debug("Запрос тренировки: {}", id);
        model.addAttribute("sessionDetails", practiceSessionService.sessionDetails(id));
        return "practice-session-details";
    }


    @GetMapping("/diary/session-delete/{id}")
    public String deleteSession(@PathVariable("id") Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            practiceSessionService.removeSession(id, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Тренировка успешно удалена!");
        } catch (IllegalArgumentException e) {
            log.warn("Недопустимая попытка удаления: id={}, user={}", id, principal.getName());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            log.error("Ошибка при удалении тренировки", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось удалить тренировку.");
        }
        return "redirect:/diary";
    }
}