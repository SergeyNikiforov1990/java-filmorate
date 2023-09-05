package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        log.info("Добавление фильма " + film);
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновление фильма с id: " + film.getId() + film);
        filmService.updateFilm(film);
        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        log.info("Получение списка всех фильмов");
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{id}")
    @ResponseBody
    public Film getFilm(@PathVariable Integer id) {
        log.info("Получение фильма с id: " + id);
        return filmService.getFilm(id);
    }

    @GetMapping("/films/popular")
    @ResponseBody
    public List<Film> getListBestMovies(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        log.info("Получение списка " + count + " лучших фильмов ");
        return filmService.getListBestMovies(count);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLikeFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.addLikeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        log.info("Получение списка всех жанров");
        return filmService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable Integer id) {
        log.info("Получение жанра под id: " + id);
        return filmService.getGenreById(id);
    }

    @GetMapping("/mpa")
    public List<RatingMPA> getAllRatings() {
        List<RatingMPA> ratings = filmService.getAllRatings();
        log.info("Получение списка всех рейтингов MPA: " + ratings);
        return ratings;
    }

    @GetMapping("/mpa/{id}")
    public RatingMPA getRatingById(@PathVariable Integer id) {
        RatingMPA rating = filmService.getRatingMpaById(id);
        log.info("Получение MPA рейтинга с id " + id);
        return rating;
    }

}


