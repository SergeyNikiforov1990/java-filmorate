package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final ValidationUser validationUser = new ValidationUser();
    private int id = 1;

    @GetMapping
    public List<User> getAllUsers() {
        List<User> listUsers = new ArrayList<>(users.values());
        log.trace("Количество пользователей в текущий момент: " + listUsers.size());
        return listUsers;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        validationUser.validation(user);
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        log.trace("Сохранен пользователь: " + user);
        return user;
    }


    @PutMapping
    public User updateUser(@RequestBody User user) {
        validationUser.validationId(user);
        if (!users.containsKey(user.getId())) {
            throw new RuntimeException("В базе нет пользователя с таким ID");
        }
        validationUser.validation(user);
        users.put(user.getId(), user);
        return user;
    }
}
