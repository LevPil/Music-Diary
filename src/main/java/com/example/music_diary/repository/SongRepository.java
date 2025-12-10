package com.example.music_diary.repository;

import com.example.music_diary.models.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import java.util.List;


@Repository
public interface SongRepository extends JpaRepository<Song, Long>{
    @Query("SELECT s FROM Song s ORDER BY s.title DESC")
    List<Song> findRecentSongs();

    List<Song> findByAuthorUsername(String username);


    @Query("SELECT DISTINCT s.author.username FROM Song s WHERE s.author.username IS NOT NULL")
    List<String> findAllAuthorUsernames();
}
