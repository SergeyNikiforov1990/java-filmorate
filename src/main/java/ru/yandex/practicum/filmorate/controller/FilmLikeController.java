package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;

@RestController
@RequestMapping("/films/{id}/like/{userId}")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmLikeController {
    private final FilmService filmService;

    @PutMapping
    public void addLikeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Добавление лайка к фильму с id: " + id + " от пользователя с id: " + userId);
        filmService.addLikeFilm(id, userId);
    }

    @DeleteMapping
    public void deleteLikeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Удаление лайка у фильма с id: " + id + " от пользователя с id: " + userId);
        filmService.deleteLikeFilm(id, userId);
    }
}
