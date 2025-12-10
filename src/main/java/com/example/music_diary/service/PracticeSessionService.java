package com.example.music_diary.service;

import com.example.music_diary.dto.AddPracticeSessionDto;
import com.example.music_diary.dto.ShowPracticeSessionDto;
import java.util.List;

public interface PracticeSessionService {
    List<ShowPracticeSessionDto> getUserSessions(String username);
    void addSession(AddPracticeSessionDto sessionDto, String username);
    void removeSession(Long id, String username);
    ShowPracticeSessionDto sessionDetails(Long id);
}
