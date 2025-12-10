package com.example.music_diary.service;

import com.example.music_diary.dto.AddPracticeSessionDto;
import com.example.music_diary.dto.ShowPracticeSessionDto;
import com.example.music_diary.models.entity.PracticeSession;
import com.example.music_diary.models.entity.Song;
import com.example.music_diary.models.entity.User;
import com.example.music_diary.models.exceptions.SessionNotFoundException;
import com.example.music_diary.models.exceptions.SongNotFoundException;
import com.example.music_diary.models.exceptions.UserNotFoundException;
import com.example.music_diary.repository.PracticeSessionRepository;
import com.example.music_diary.repository.SongRepository;
import com.example.music_diary.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PracticeSessionServiceImpl implements PracticeSessionService {

    private final PracticeSessionRepository practiceSessionRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public PracticeSessionServiceImpl(PracticeSessionRepository practiceSessionRepository, SongRepository songRepository, UserRepository userRepository, ModelMapper mapper) {
        this.practiceSessionRepository = practiceSessionRepository;
        this.songRepository = songRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    @Cacheable(value = "practiceSessions", key = "'user:' + #username")
    public List<ShowPracticeSessionDto> getUserSessions(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));
        List<PracticeSession> sessions = practiceSessionRepository.findByUserWithSongOrderByDateDesc(user);
        return sessions.stream()
                .map(session -> mapper.map(session, ShowPracticeSessionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "practiceSessions", allEntries = true)
    public void addSession(AddPracticeSessionDto sessionDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));
        PracticeSession session = mapper.map(sessionDto, PracticeSession.class);
        session.setId(null);
        session.setUser(user);
        if (sessionDto.getSongId() != null) {
            Song song = songRepository.findById(sessionDto.getSongId())
                    .orElseThrow(() -> new SongNotFoundException("Песня не найдена"));
            session.setSong(song);
        }
        practiceSessionRepository.save(session);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "practiceSessions", allEntries = true)
    public void removeSession(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + username));

        PracticeSession session = practiceSessionRepository.findById(id)
                .orElseThrow(() -> new SessionNotFoundException("Тренировка не найдена: " + id));

        practiceSessionRepository.deleteById(id);
    }

    @Override
    public ShowPracticeSessionDto sessionDetails(Long id) {
        PracticeSession session = practiceSessionRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Тренировка не найдена: " + id));
        return mapper.map(session, ShowPracticeSessionDto.class);
    }
}