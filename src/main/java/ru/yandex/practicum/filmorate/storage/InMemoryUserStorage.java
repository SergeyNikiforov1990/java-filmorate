package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationUser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private ValidationUser validationUser = new ValidationUser();
    private int id = 1;
    private Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        List<User> listUsers = new ArrayList<>(users.values());
        log.trace("Количество пользователей в текущий момент: " + listUsers.size());
        return listUsers;
    }

    @Override
    public User addUser(User user) {
        validationUser.validationForAdd(user);
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        log.trace("Сохранен пользователь: " + user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        validationUser.validationId(user);
        log.info("Запрос на обновление пользователя" + id);
        if (!users.containsKey(user.getId())) {
            throw new RuntimeException("В базе нет пользователя с таким ID");
        }
        validationUser.validationForAdd(user);
        users.put(user.getId(), user);
        log.trace("Обновлен пользователь: " + user);
        return user;
    }

    @Override
    public User getUser(int id) {
        log.info("Запрос на получение пользователя по id" + id);
        if (!users.containsKey(id)) {
            throw new DataNotFoundException("Пользователь с таким id не найден" + id);
        }
        return users.get(id);
    }

    @Override
    public User deleteUser(int id) {
        log.info("Запрос на получение пользователя по id" + id);
        if (!users.containsKey(id)) {
            throw new DataNotFoundException("Пользователь с таким id не найден" + id);
        }
        log.trace("Удален пользователь с id: " + id);
        return users.remove(id);
    }
}
