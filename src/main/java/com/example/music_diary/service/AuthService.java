package com.example.music_diary.service;

import com.example.music_diary.dto.UserRegistrationDto;
import com.example.music_diary.models.entity.User;

import java.util.List;

public interface AuthService {
    void register(UserRegistrationDto registrationDTO);

    User getUser(String username);
    List<String> getAllUsernames();
}
