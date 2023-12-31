package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public List<Film> getAllFilms() {
        log.info("Запрос на вывод списка фильмов");
        List<Film> filmList = new ArrayList<>(films.values());
        return filmList;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(id);
        id++;
        films.put(film.getId(), film);
        log.info("Добавлен фильм: " + film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new RuntimeException("В базе нет фильма с таким id: " + id); // Вопрос по классу исключения!
        }
        films.put(film.getId(), film);
        log.info("фильм с id " + film.getId() + " обновлен! " + film);
        return film;
    }

    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new DataNotFoundException("Фильм с таким id не найден: " + id);
        }
        log.info("запрос на получение фильма по id: " + id);
        return films.get(id);
    }

    @Override
    public Film deleteFilm(int id) {
        if (!films.containsKey(id)) {
            throw new DataNotFoundException("Фильм с таким id не найден: " + id);
        }
        log.info("запрос на удаление фильма по id: " + id);
        return films.remove(id);
    }
}
