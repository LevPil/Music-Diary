package com.example.music_diary.repository;

import com.example.music_diary.models.entity.PracticeSession;
import com.example.music_diary.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PracticeSessionRepository extends JpaRepository<PracticeSession, Long> {
    List<PracticeSession> findByUserOrderByDateDesc(User user);

    @Query("SELECT ps FROM PracticeSession ps JOIN FETCH ps.song WHERE ps.user = :user ORDER BY ps.date DESC")
    List<PracticeSession> findByUserWithSongOrderByDateDesc(User user);

    @Query("SELECT SUM(p.durationMinutes) FROM PracticeSession p WHERE p.user.id = :userId AND p.date >= :since")
    Integer getTotalMinutesSince(@Param("userId") Long userId, @Param("since") LocalDate since);

    @Query("SELECT COUNT(DISTINCT p.date) FROM PracticeSession p WHERE p.user.id = :userId AND p.date >= :since")
    Long getActiveDaysSince(@Param("userId") Long userId, @Param("since") LocalDate since);

    @Query("SELECT AVG(p.durationMinutes) FROM PracticeSession p WHERE p.user.id = :userId")
    Double getAverageDurationByUserId(@Param("userId") Long userId);

    @Query("""
    SELECT s.title, COUNT(p) FROM PracticeSession p LEFT JOIN p.song s WHERE p.user.id = :userId AND p.song IS NOT NULL GROUP BY s.id, s.title ORDER BY COUNT(p) DESC""")
    List<Object[]> findTopSongsByUser(@Param("userId") Long userId);

    @Query("""
    SELECT
        SUM(CASE WHEN p.date >= :weekAgo AND p.date < :halfWeekAgo THEN p.durationMinutes ELSE 0 END),
        SUM(CASE WHEN p.date >= :halfWeekAgo THEN p.durationMinutes ELSE 0 END) FROM PracticeSession p WHERE p.user.id = :userId AND p.date >= :weekAgo""")
    List<Object[]> getWeeklyComparisonAsList(
            @Param("userId") Long userId,
            @Param("weekAgo") LocalDate weekAgo,
            @Param("halfWeekAgo") LocalDate halfWeekAgo
    );

    @Modifying
    @Query("DELETE FROM PracticeSession ps WHERE ps.song.id = :songId")
    void deleteBySongId(@Param("songId") Long songId);

    @Query("SELECT COUNT(p) FROM PracticeSession p WHERE p.song.id = :songId")
    long countBySongId(@Param("songId") Long songId);
}