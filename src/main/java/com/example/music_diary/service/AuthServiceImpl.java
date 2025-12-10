package com.example.music_diary.service;

import com.example.music_diary.dto.UserRegistrationDto;
import com.example.music_diary.models.entity.Role;
import com.example.music_diary.models.entity.User;
import com.example.music_diary.models.enums.Roles;
import com.example.music_diary.models.exceptions.UserNotFoundException;
import com.example.music_diary.repository.UserRepository;
import com.example.music_diary.repository.UserRoleRepository;
import com.example.music_diary.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public AuthServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void register(UserRegistrationDto registrationDto) {

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new RuntimeException("Пароли не совпадают!");
        }

        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email уже используется!");
        }

        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new RuntimeException("Логин уже используется!");
        }

        var userRole = userRoleRepository.findRoleByName(Roles.USER)
                .orElseThrow(()-> new UserNotFoundException("Роль USER не найдена!"));
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());
        user.setFullName(registrationDto.getFullName());
        user.setAge(registrationDto.getAge());
        user.setRoles(List.of(userRole));

        userRepository.save(user);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }

    @Override
    public List<String> getAllUsernames() {
        return userRepository.findAll().stream()
                .map(User::getUsername)
                .toList();
    }
}