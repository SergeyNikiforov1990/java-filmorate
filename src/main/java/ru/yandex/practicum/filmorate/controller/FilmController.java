package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.util.List;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final ValidationFilm validationFilm = new ValidationFilm();

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Film getFilm(@PathVariable Integer id) {
        validationFilm.searchValidation(filmService.getFilm(id));
        validationFilm.validationIdFilm(filmService.getFilm(id));
        return filmService.getFilm(id);
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeFilm(@PathVariable int id, @PathVariable int userId) {
        validationFilm.validationId(userId);
        filmService.addLikeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable int id, @PathVariable int userId) {
        validationFilm.validationId(userId);
        validationFilm.validationIdFilm(filmService.getFilm(id));
        filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/popular")
    @ResponseBody
    public List<Film> getListBestMovies(@RequestParam(required = false) Integer count) {
        return filmService.getListBestMovies(count);
    }
}


