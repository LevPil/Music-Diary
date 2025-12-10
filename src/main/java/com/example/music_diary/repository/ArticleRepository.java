package com.example.music_diary.repository;

import com.example.music_diary.models.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>  {
    @Query("SELECT a FROM Article a ORDER BY a.createdAt DESC")
    List<Article> findTopByOrderByCreatedAtDesc(int count);
}
