package com.example.music_diary.config;

import com.example.music_diary.service.AppUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.music_diary.models.enums.Roles;
import com.example.music_diary.repository.UserRepository;

@Slf4j
@EnableWebSecurity
@Configuration
public class AppSecurityConfiguration {

    private final UserRepository userRepository;

    public AppSecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
        log.info("SecurityConfig инициализирована");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/favicon.ico", "/error").permitAll()
                        .requestMatchers("/login", "/register", "/login-error").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/profile").authenticated()
                        .requestMatchers("/statistics").authenticated()
                        .requestMatchers("/articles/add", "/articles/article-delete/*")
                        .hasRole(Roles.ADMIN.name())
                        .requestMatchers("/songs/add", "/songs/song-delete/*")
                        .hasAnyRole(Roles.USER.name(), Roles.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                        .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400 * 7)
                        .userDetailsService(userDetailsService())
                        .rememberMeParameter("remember-me")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll()
                )

                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/actuator/**")
                );

        log.info("SecurityFilterChain настроен");
        return http.build();
    }


    public org.springframework.security.web.context.SecurityContextRepository securityContextRepository() {
        return new org.springframework.security.web.context.DelegatingSecurityContextRepository(
                new org.springframework.security.web.context.RequestAttributeSecurityContextRepository(),
                new org.springframework.security.web.context.HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new AppUserDetailsService(userRepository);
    }
}