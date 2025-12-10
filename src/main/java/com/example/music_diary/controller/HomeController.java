package com.example.music_diary.controller;

import com.example.music_diary.dto.ShowSongInfoDto;
import com.example.music_diary.service.ArticleService;
import com.example.music_diary.service.SongService;
import com.example.music_diary.dto.ShowArticleInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Random;

@Slf4j
@Controller

public class HomeController {
    private final ArticleService articleService;
    private final SongService songService;

    public HomeController(ArticleService articleService, SongService songService) {
        this.articleService = articleService;
        this.songService = songService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        log.debug("Отображение главной страницы");
        List<ShowSongInfoDto> allSongs = songService.allSongs();
        ShowSongInfoDto randomSong = null;
        if (!allSongs.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(allSongs.size());
            randomSong = allSongs.get(index);
        }
        List<ShowArticleInfoDto> latestArticles = articleService.getLatestArticles(2);
        model.addAttribute("randomSong", randomSong);
        model.addAttribute("latestArticles", latestArticles);
        return "index";
    }
}