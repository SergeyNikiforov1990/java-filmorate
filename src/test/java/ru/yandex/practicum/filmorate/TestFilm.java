package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestFilm {
    InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    InMemoryUserStorage userStorage = new InMemoryUserStorage();
    FilmService filmService = new FilmServiceImpl(filmStorage, userStorage);
    public FilmController filmController = new FilmController(filmService);
    Film templateFilm;
    String description = "a";

    @BeforeEach
    public void setData() {
        templateFilm = new Film("Догма", "Треш, угар и содомия", LocalDate.of(1999, 10, 12), 2);
    }

    @Test
    void createAnObject() {
        filmController.addFilm(templateFilm);
        assertEquals(1, filmController.getAllFilms().size(), "Валидация прошла");
    }

    @Test
    void createAnObjectWrongReleaseDate() {
        templateFilm.setReleaseDate(LocalDate.of(1880, 10, 10));
        assertThrows(ValidationException.class, () -> filmController.addFilm(templateFilm), "Неверная дата релиза");
    }

    @Test
    void createAnObjectEmptyName() {
        templateFilm.setName("");
        assertThrows(ValidationException.class, () -> filmController.addFilm(templateFilm), "Пустое имя");
    }

    @Test
    void createAnObjectMaximumDescription201() {
        templateFilm.setDescription(description.repeat(201));
        assertThrows(ValidationException.class, () -> filmController.addFilm(templateFilm), "Описание слишком длинное");
    }

    @Test
    void createAnObjectMaximumDescription200() {
        templateFilm.setDescription(description.repeat(200));
        filmController.addFilm(templateFilm);
        assertEquals(1, filmController.getAllFilms().size(), "Добавлен фильм с описанием в 200 символов");
    }

    @Test
    void createObjectDurationNull() {
        templateFilm.setDuration(0);
        assertThrows(ValidationException.class, () -> filmController.addFilm(templateFilm), "Неверная длительность");
    }

    @Test
    void createAnObjectPut() {
        filmController.addFilm(templateFilm);
        templateFilm.setId(1);
        templateFilm.setName("Догма 2");
        filmController.updateFilm(templateFilm);
        assertEquals(1, filmController.getAllFilms().size(), "Фильм обновлен");
    }
}
