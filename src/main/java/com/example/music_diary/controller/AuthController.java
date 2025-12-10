package com.example.music_diary.controller;

import com.example.music_diary.dto.UserRegistrationDto;
import com.example.music_diary.models.entity.User;
import com.example.music_diary.service.AuthService;
//import com.example.music_diary.service.UserService;
import com.example.music_diary.views.UserProfileView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Slf4j
@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @GetMapping("/register")
    public String register(Model model) {
        log.debug("Отображение страницы регистрации");
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute("userRegistrationDto") UserRegistrationDto userRegistrationDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        log.debug("Обработка регистрации пользователя: {}", userRegistrationDto.getUsername());

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации при регистрации: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("userRegistrationDto", userRegistrationDto);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.userRegistrationDto", bindingResult);
            return "redirect:/register";
        }

        authService.register(userRegistrationDto);
        log.info("Пользователь успешно зарегистрирован: {}", userRegistrationDto.getUsername());

        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        String username = principal.getName();
        log.debug("Отображение профиля пользователя: {}", username);

        User user = authService.getUser(username);

        UserProfileView userProfileView = new UserProfileView(
                username,
                user.getEmail(),
                user.getFullName(),
                user.getAge()
        );
        model.addAttribute("user", userProfileView);
        return "profile";
    }

    @GetMapping("/login")
    public String login() {
        log.debug("Отображение страницы входа");
        return "login";
    }

}