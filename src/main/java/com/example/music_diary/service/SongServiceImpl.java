package com.example.music_diary.service;

import com.example.music_diary.dto.AddSongDto;
import com.example.music_diary.dto.ShowSongInfoDto;
import com.example.music_diary.models.entity.Song;
import com.example.music_diary.models.entity.User;
import com.example.music_diary.models.exceptions.SongIsInSessionUseException;
import com.example.music_diary.models.exceptions.SongNotFoundException;
import com.example.music_diary.models.exceptions.UserNotFoundException;
import com.example.music_diary.repository.PracticeSessionRepository;
import com.example.music_diary.repository.SongRepository;
import com.example.music_diary.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final PracticeSessionRepository practiceSessionRepository;
    private final ModelMapper mapper;

    public SongServiceImpl(SongRepository songRepository, UserRepository userRepository,
                           PracticeSessionRepository practiceSessionRepository, ModelMapper mapper) {
        this.songRepository = songRepository;
        this.userRepository = userRepository;
        this.practiceSessionRepository = practiceSessionRepository;
        this.mapper = mapper;
    }

    @Override
    @Cacheable(value = "songs", key = "'all'")
    public List<ShowSongInfoDto> allSongs() {
        return songRepository.findAll().stream()
                .map(song -> mapper.map(song, ShowSongInfoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ShowSongInfoDto> allSongsPaginated(Pageable pageable) {
        return songRepository.findAll(pageable).map(song -> mapper.map(song, ShowSongInfoDto.class));
    }

    @Override
    public ShowSongInfoDto songDetails(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Песня с ID " + id + " не найдена"));
        return mapper.map(song, ShowSongInfoDto.class);
    }

    @Override
    public List<ShowSongInfoDto> searchSongs(String query) {
        String lowerQuery = query == null ? "" : query.toLowerCase().trim();
        return songRepository.findAll().stream()
                .filter(song -> {
                    String title = song.getTitle() == null ? "" : song.getTitle().toLowerCase();
                    String artist = song.getArtist() == null ? "" : song.getArtist().toLowerCase();
                    return title.contains(lowerQuery) || artist.contains(lowerQuery);
                })
                .map(song -> mapper.map(song, ShowSongInfoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "songs", allEntries = true)
    public void addSong(AddSongDto songDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User author = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + currentUsername));

        Song song = mapper.map(songDto, Song.class);
        song.setAuthor(author);
        songRepository.save(song);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "songs", allEntries = true)
    public void removeSong(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Песня с ID " + id + " не найдена"));

        long usageCount = practiceSessionRepository.countBySongId(id);
        if (usageCount > 0) {
            throw new SongIsInSessionUseException(
                    "Невозможно удалить песню '" + song.getTitle() + "', " +
                            "так как она используется в " + usageCount + " тренировках."
            );
        }

        songRepository.deleteById(id);
    }

    @Override
    public List<String> getAllAuthorUsernames() {
        return allSongs().stream()
                .map(ShowSongInfoDto::getAuthorUsername)
                .filter(Objects::nonNull).filter(s -> !"[unknown]".equals(s))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}