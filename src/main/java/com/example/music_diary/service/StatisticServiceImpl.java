package com.example.music_diary.service;

import com.example.music_diary.repository.PracticeSessionRepository;
import com.example.music_diary.repository.UserRepository;
import com.example.music_diary.service.StatisticService;
import com.example.music_diary.dto.ShowPracticeStatsDto;
import com.example.music_diary.dto.ShowTopItemDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)

public class StatisticServiceImpl implements StatisticService {

    private final PracticeSessionRepository practiceSessionRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public StatisticServiceImpl(PracticeSessionRepository practiceSessionRepository, UserRepository userRepository, ModelMapper mapper) {
        this.practiceSessionRepository = practiceSessionRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public ShowPracticeStatsDto getUserStats(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));
        Long userId = user.getId();

        Integer totalMinutesLast7Days = practiceSessionRepository.getTotalMinutesSince(userId, LocalDate.now().minusDays(7));

        Long activeDaysLast7Days = practiceSessionRepository.getActiveDaysSince(userId, LocalDate.now().minusDays(7));

        Double averageDuration = practiceSessionRepository.getAverageDurationByUserId(userId);

        List<Object[]> topSongsRaw = practiceSessionRepository.findTopSongsByUser(userId);
        List<ShowTopItemDto> topSongs = topSongsRaw.stream()
                .limit(3)
                .map(obj -> new ShowTopItemDto((String) obj[0], (Long) obj[1]))
                .collect(Collectors.toList());

        LocalDate weekAgo = LocalDate.now().minusWeeks(2);
        LocalDate halfWeekAgo = LocalDate.now().minusWeeks(1);
        List<Object[]> result = practiceSessionRepository.getWeeklyComparisonAsList(userId, weekAgo, halfWeekAgo);
        Object[] row = result.isEmpty() ? null : result.get(0);
        Integer lastWeekMinutes = 0;
        Integer currentWeekMinutes = 0;
        if (row != null && row.length >= 2) {
            if (row[0] != null) lastWeekMinutes = ((Number) row[0]).intValue();
            if (row[1] != null) currentWeekMinutes = ((Number) row[1]).intValue();
        }
        String trend = currentWeekMinutes > lastWeekMinutes ? "ВЫШЕ" :
                currentWeekMinutes < lastWeekMinutes ? "НИЖЕ" : "--";
        return new ShowPracticeStatsDto(
                totalMinutesLast7Days != null ? totalMinutesLast7Days : 0,
                activeDaysLast7Days != null ? activeDaysLast7Days : 0L,
                averageDuration != null ? averageDuration : 0.0,
                topSongs,
                lastWeekMinutes,
                currentWeekMinutes,
                trend
        );
    }
}