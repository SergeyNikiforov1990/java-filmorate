package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidatorFilm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final ValidatorFilm validatorFilm = new ValidatorFilm();
    private int id = 1;

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> listFilms = new ArrayList<>(films.values());
        log.trace("Количество фильмов в библиотеке: " + listFilms.size());
        return listFilms;
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        validatorFilm.validation(film);
        film.setId(id);
        id++;
        films.put(film.getId(), film);
        log.info("Добавлен фильм" + film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        validatorFilm.validationId(film, getAllFilms());
        validatorFilm.validation(film);
        films.put(film.getId(), film);
        return film;
    }
}


