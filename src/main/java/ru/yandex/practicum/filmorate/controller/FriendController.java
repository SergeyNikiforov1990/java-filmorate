package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users/{id}/friends")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendController {
    private final UserService userService;

    @PutMapping("{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Добавление пользователем с id: " + id + " в друзья пользователя с id:" + friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Удаление пользователем с id: " + id + " из друзей пользователя с id: " + friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping
    public List<User> getUserFriends(@PathVariable int id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getListCommonFriends(id, otherId);
    }
}
