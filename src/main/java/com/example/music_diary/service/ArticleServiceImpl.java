package com.example.music_diary.service;

import com.example.music_diary.dto.AddArticleDto;
import com.example.music_diary.dto.ShowArticleInfoDto;
import com.example.music_diary.models.entity.Article;
import com.example.music_diary.models.entity.User;
import com.example.music_diary.repository.ArticleRepository;
import com.example.music_diary.repository.UserRepository;
import com.example.music_diary.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public ArticleServiceImpl(ArticleRepository articleRepository, UserRepository userRepository, ModelMapper mapper) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    @Cacheable(value = "articles", key = "'all'")
    public List<ShowArticleInfoDto> allArticles() {
        return articleRepository.findAll().stream()
                .map(article -> mapper.map(article, ShowArticleInfoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = "articles", allEntries = true)
    public void addArticle(AddArticleDto articleDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User author = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Автор не найден: " + currentUsername));

        Article article = mapper.map(articleDto, Article.class);
        article.setAuthor(author);

        articleRepository.save(article);
    }

    @Override
    @Transactional
    @CacheEvict(value = "articles", allEntries = true)
    public void removeArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new IllegalArgumentException("Статья не найдена: " + id);
        }
        articleRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "article", key = "#id")
    public ShowArticleInfoDto articleDetails(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Статья не найдена: " + id));

        return mapper.map(article, ShowArticleInfoDto.class);
    }

    @Override
    @Cacheable(value = "articles", key = "'latest:' + #count")
    public List<ShowArticleInfoDto> getLatestArticles(int count) {
        return articleRepository.findTopByOrderByCreatedAtDesc(count).stream()
                .map(article -> mapper.map(article, ShowArticleInfoDto.class))
                .limit(count)
                .collect(Collectors.toList());
    }
}