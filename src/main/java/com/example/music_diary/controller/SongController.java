package com.example.music_diary.controller;

import com.example.music_diary.dto.AddSongDto;
import com.example.music_diary.models.exceptions.SongIsInSessionUseException;
import com.example.music_diary.models.exceptions.SongNotFoundException;
import com.example.music_diary.service.AuthService;
import com.example.music_diary.service.SongService;
import com.example.music_diary.dto.ShowSongInfoDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.security.access.AccessDeniedException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Controller
public class SongController {

    private final SongService songService;
    private final AuthService authService;
    public SongController(SongService songService, AuthService authService) {
        this.songService = songService;
        this.authService = authService;
    }

    @GetMapping("/songs")
    public String showAllSongs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String author,
            Model model) {

        log.debug("Запрос списка песен: page={}, size={}, search='{}', author='{}'", page, size, search, author);

        String cleanSearch = (search == null) ? "" : search.trim();
        String cleanAuthor = (author == null || author.isEmpty() || "-".equals(author)) ? null : author.trim();
        List<ShowSongInfoDto> songsToShow;
        List<ShowSongInfoDto> allSongs = songService.allSongs();
        if (!cleanSearch.isEmpty() || cleanAuthor != null) {
            Stream<ShowSongInfoDto> stream = allSongs.stream();
            if (cleanAuthor != null) {
                stream = stream.filter(s -> cleanAuthor.equals(s.getAuthorUsername()));
            }
            if (!cleanSearch.isEmpty()) {
                String q = cleanSearch.toLowerCase();
                stream = stream.filter(s ->
                        (s.getTitle() != null && s.getTitle().toLowerCase().contains(q)) ||
                                (s.getArtist() != null && s.getArtist().toLowerCase().contains(q)));
            }
            songsToShow = stream.collect(Collectors.toList());
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
            Page<ShowSongInfoDto> paginated = songService.allSongsPaginated(pageable);
            songsToShow = paginated.getContent();
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", paginated.getTotalPages());
            model.addAttribute("totalItems", paginated.getTotalElements());
        }

        model.addAttribute("allSongs", songsToShow);
        model.addAttribute("search", search);
        model.addAttribute("selectedAuthor", author);
        model.addAttribute("allAuthors", songService.getAllAuthorUsernames());

        return "song-all";
    }

    @GetMapping("/songs/song-details/{id}")
    public String songDetails(@PathVariable("id") Long id, Model model) {
        log.debug("Запрос деталей песни: {}", id);
        model.addAttribute("songDetails", songService.songDetails(id));
        return "song-details";
    }

    @GetMapping("/songs/add")
    public String addSong() {
        log.debug("Отображение формы добавления песни");
        return "song-add";
    }

    @ModelAttribute("songModel")
    public AddSongDto initSong() {
        return new AddSongDto();
    }

    @PostMapping("/songs/add")
    public String addSong(@Valid @ModelAttribute("songModel") AddSongDto songModel,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        log.debug("Обработка POST запроса на добавление песни");
        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("songModel", songModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.songModel",
                    bindingResult);
            return "redirect:/songs/add";
        }

        songService.addSong(songModel);
        redirectAttributes.addFlashAttribute("successMessage",
                "Песня '" + songModel.getTitle() + "' успешно добавлена!");
        return "redirect:/songs";
    }

    @GetMapping("/songs/song-delete/{id}")
    public String deleteSong(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        log.debug("Запрос на удаление песни: {}", id);
        try {
            songService.removeSong(id);
            redirectAttributes.addFlashAttribute("successMessage", "Песня успешно удалена!");
        } catch (SongIsInSessionUseException e) {
            log.warn("Попытка удалить песню, которая используется: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (SongNotFoundException e) {
            log.warn("Песня не найдена: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/songs";
    }

}