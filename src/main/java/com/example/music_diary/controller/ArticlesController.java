package com.example.music_diary.controller;

import com.example.music_diary.dto.AddArticleDto;
import com.example.music_diary.service.ArticleService;
import com.example.music_diary.dto.ShowArticleInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class ArticlesController {

    private final ArticleService articleService;

    public ArticlesController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/articles")
    public String showAllArticles(Model model) {
        log.debug("Отображение списка статей");
        List<ShowArticleInfoDto> articles = articleService.allArticles();
        model.addAttribute("allArticles", articles);
        return "articles";
    }

    @GetMapping("/articles/article-details/{id}")
    public String articleDetails(@PathVariable("id") Long id, Model model) {
        log.debug("Отображение деталей статьи: {}", id);
        model.addAttribute("articleDetails", articleService.articleDetails(id));
        return "article-details";
    }

    @GetMapping("/articles/add")
    public String addArticleForm(Model model) {
        log.debug("Отображение формы добавления статьи");
        model.addAttribute("articleModel", new AddArticleDto());
        return "article-add";
    }

    @PostMapping("/articles/add")
    public String addArticle(@Valid @ModelAttribute("articleModel") AddArticleDto articleModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        log.debug("Обработка добавления статьи");

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("articleModel", articleModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.articleModel",
                    bindingResult);
            return "redirect:/articles/add";
        }

        articleService.addArticle(articleModel);
        redirectAttributes.addFlashAttribute("successMessage", "Статья успешно добавлена!");

        return "redirect:/articles";
    }
    @GetMapping("/articles/article-delete/{id}")
    public String deleteArticle(@PathVariable("id") Long id,
                             RedirectAttributes redirectAttributes) {
        log.debug("Запрос на удаление статьи: {}", id);
        articleService.removeArticle(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Песня успешно удалена!");
        return "redirect:/articles";
    }
}