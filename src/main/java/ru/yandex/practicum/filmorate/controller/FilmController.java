package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Добавление фильма " + film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновление фильма с id: " + film.getId() + film);
        filmService.updateFilm(film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получение списка всех фильмов");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Film getFilm(@PathVariable Integer id) {
        log.info("Получение фильма с id: " + id);
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    @ResponseBody
    public List<Film> getListBestMovies(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        log.info("Получение списка " + count + " лучших фильмов ");
        return filmService.getListBestMovies(count);
    }


}


