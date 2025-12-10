package com.example.music_diary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.music_diary.models.enums.Roles;
import com.example.music_diary.models.entity.User;
import com.example.music_diary.repository.UserRepository;
import com.example.music_diary.repository.UserRoleRepository;
import com.example.music_diary.models.entity.Role;
import java.util.List;


@Slf4j
@Component
public class Init implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultPassword;

    public Init(UserRepository userRepository,
                UserRoleRepository userRoleRepository,
                PasswordEncoder passwordEncoder,
                @Value("${app.default.password}") String defaultPassword) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultPassword = defaultPassword;
        log.info("Init компонент инициализирован");
    }

    @Override
    public void run(String... args) {
        log.info("Запуск инициализации начальных данных");
        initRoles();
        initUsers();
        log.info("Инициализация начальных данных завершена");
    }

    private void initRoles() {
        if (userRoleRepository.count() == 0) {
            log.info("Создание базовых ролей...");
            userRoleRepository.saveAll(List.of(
                    new Role(Roles.ADMIN),
                    new Role(Roles.USER)
            ));
            log.info("Роли созданы: ADMIN, USER");
        } else {
            log.debug("Роли уже существуют, пропуск инициализации");
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            log.info("Создание пользователей по умолчанию...");
            initAdmin();
            initNormalUser();
            log.info("Пользователи по умолчанию созданы");
        } else {
            log.debug("Пользователи уже существуют, пропуск инициализации");
        }
    }

    private void initAdmin() {
        var adminRole = userRoleRepository
                .findByName(Roles.ADMIN)
                .orElseThrow(() -> new RuntimeException("Роль ADMIN не найдена"));

        var adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode(defaultPassword));
        adminUser.setEmail("admin@example.com");
        adminUser.setFullName("Admin Adminovich");
        adminUser.setAge(30);
        adminUser.setRoles(List.of(adminRole));

        userRepository.save(adminUser);
        log.info("Создан администратор: admin");
    }

    private void initNormalUser() {
        var userRole = userRoleRepository
                .findByName(Roles.USER)
                .orElseThrow(() -> new RuntimeException("Роль USER не найдена"));

        var normalUser = new User();
        normalUser.setUsername("user");
        normalUser.setPassword(passwordEncoder.encode(defaultPassword));
        normalUser.setEmail("user@example.com");
        normalUser.setFullName("User Userovich");
        normalUser.setAge(22);
        normalUser.setRoles(List.of(userRole));

        userRepository.save(normalUser);
        log.info("Создан обычный пользователь: user");
    }
}