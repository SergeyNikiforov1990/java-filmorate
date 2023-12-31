package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUser {
    InMemoryUserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserServiceImpl(userStorage);
    UserController userController = new UserController(userService);
    User templateUser;

    @BeforeEach
    public void setData() {
        templateUser = new User("myMail@yandex.ru", "NiSega", "Sergey", LocalDate.of(1990, 1, 6));
    }

    @Test
    void createAnObject() {
        userController.addUser(templateUser);
        assertEquals(1, userController.getAllUsers().size(), "Object created successfully");
    }

    @Test
    void createAnObjectEmailFieldNotFilled() {
        templateUser.setEmail("");
        assertThrows(ValidationException.class, () -> userController.addUser(templateUser), "Address not filled");
    }

    @Test
    void createAnObjectEmailNoRequiredSign() {
        templateUser.setEmail("abcyandex.ru");
        assertThrows(ValidationException.class, () -> userController.addUser(templateUser), "No sign @ in address");
    }

    @Test
    void createAnObjectEmptyFieldLogin() {
        templateUser.setLogin("");
        assertThrows(ValidationException.class, () -> userController.addUser(templateUser), "Login not completed");
    }

    @Test
    void createAnObjectSpacebarsInLogin() {
        templateUser.setLogin("Max Fed");
        assertThrows(ValidationException.class, () -> userController.addUser(templateUser), "Space in logging");
    }

    @Test
    void createAnObjectInNameWeUseTheLogin() {
        templateUser.setName("");
        userController.addUser(templateUser);
        assertEquals(templateUser.getName(), templateUser.getLogin(), "write the login in the name");
    }

    @Test
    void createAnObjectDataOfBirthCheck() {
        templateUser.setBirthday(LocalDate.of(2024, 12, 25));
        assertThrows(ValidationException.class, () -> userController.addUser(templateUser), "wrong date of birth");
    }

    @Test
    void createAnObjectPut() {
        userController.addUser(templateUser);
        templateUser.setId(1);
        templateUser.setName("Вася");
        userController.updateUser(templateUser);
        assertEquals(1, userController.getAllUsers().size(), "Пользователь обновлен");
    }

    @Test
    void getObjectById() {
        userController.addUser(templateUser);
        User templateUserTest = userController.getUser(templateUser.getId());
        assertEquals(templateUser, templateUserTest);
    }

    @Test
    void getObjectByIdFail() {
        userController.addUser(templateUser);
        assertThrows(DataNotFoundException.class,
                () -> {
                    userController.getUser(999);
                }
        );
    }
}
