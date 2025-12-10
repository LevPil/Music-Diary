package com.example.music_diary.service;

import com.example.music_diary.dto.AddSongDto;
import com.example.music_diary.dto.ShowSongInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SongService {
    List<ShowSongInfoDto> allSongs();
    Page<ShowSongInfoDto> allSongsPaginated(Pageable pageable);
    ShowSongInfoDto songDetails(Long id);
    List<ShowSongInfoDto> searchSongs(String query);
    void addSong(AddSongDto addSongDto);
    void removeSong(Long id);
    List<String> getAllAuthorUsernames();
}
