package com.example.music_diary.service;

import com.example.music_diary.dto.AddArticleDto;
import com.example.music_diary.dto.ShowArticleInfoDto;


import java.util.List;

public interface ArticleService {
    List<ShowArticleInfoDto> allArticles();
    void addArticle(AddArticleDto articleDto);
    void removeArticle(Long id);
    ShowArticleInfoDto articleDetails(Long id);
    List<ShowArticleInfoDto> getLatestArticles(int count);

}
