package com.example.music_diary.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;

public class UserRegistrationDto implements Serializable {

    private String username;

    private String fullName;

    private String email;

    private int age;

    private String password;

    private String confirmPassword;

    public UserRegistrationDto() {}

    @NotEmpty(message = "Имя пользователя не должно быть пустым!")
    @Size(min = 5, max = 20, message = "Имя пользователя должно быть от 5 до 20 символов!")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @NotEmpty(message = "Полное имя не должно быть пустым!")
    @Size(min = 5, max = 20, message = "Полное имя должно быть от 5 до 20 символов!")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    @NotEmpty(message = "Email не должен быть пустым!")
    @Email(message = "Введите корректный email!")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Min(value = 0, message = "Возраст не может быть меньше 0!")
    @Max(value = 90, message = "Возраст не может быть больше 90!")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    @NotEmpty(message = "Пароль не должен быть пустым!")
    @Size(min = 5, max = 20, message = "Пароль должен быть от 5 до 20 символов!")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @NotEmpty(message = "Подтверждение пароля не должно быть пустым!")
    @Size(min = 5, max = 20, message = "Подтверждение пароля должно быть от 5 до 20 символов!")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}

