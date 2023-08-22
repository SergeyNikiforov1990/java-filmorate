package ru.yandex.practicum.filmorate.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    @Override
    public User deleteUser(int id) {
        return userStorage.deleteUser(id);
    }

    @Override
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (userId == friendId){
            throw new ValidationException("Неверный запрос на добавление себя в список своих друзей");
        }
        User user = getUser(userId); // валидация здесь
        User friendsUser = getUser(friendId);// валидация здесь
        log.info("Пользователь: " + userId + " добвил в список друзей пользователя: " + friendId);
        user.getFriendList().add(friendId);
        friendsUser.getFriendList().add(userId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        if (userId == friendId){
            throw new ValidationException("Неверный запрос на удаление себя из списка своих друзей");
        }
        User user = userStorage.getUser(userId); // валидация здесь
        User friendsUser = userStorage.getUser(friendId); // валидация здесь
        log.info("Пользователь: " + userId + " удалил из списка друзей пользователя: " + friendId);
        user.getFriendList().remove(friendId);
        friendsUser.getFriendList().remove(userId);
    }

    @Override
    public List<User> getUserFriends(int userId) {
        Set<Integer> friendIds = userStorage.getUser(userId).getFriendList(); // валидация здесь
        log.info("Запрос на вывод списка друзей пользователя: " + userId);
        return friendIds.stream().map(userStorage::getUser).collect(Collectors.toList());
    }

    @Override
    public List<User> getListCommonFriends(int userId, int otherId) {
        if (userId == otherId){
            throw new ValidationException("Неверный запрос на вывод общих друзей с самим собой");
        }
        User user = userStorage.getUser(userId);// валидация здесь
        User user1 = userStorage.getUser(otherId);// валидация здесь
        log.info("Запрос на вывод списка общих друзей пользователей: " + userId + " и " + otherId);
        Set<Integer> firstUserFriends = new HashSet<>(user.getFriendList());
        firstUserFriends.retainAll(user1.getFriendList());
        return firstUserFriends.stream()
                .map(userStorage::getUser)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
