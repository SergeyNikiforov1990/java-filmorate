package ru.yandex.practicum.filmorate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

public class ValidatorFilm {
    private final LocalDate dateAfter = LocalDate.of(1895, 12,28); // в коллекции тестов в Postman
    // дата релиза для создания фильма 1967-03-25, в ТЗ
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    public void validation (Film film) throws ValidationException {
        if (film.getName().length() == 0) {
            log.error("Ошибка поля name "+ film);
            throw new ValidationException("Не введено название фильма");
        } else if (film.getDescription().length() > 200) {
            log.error("Ошибка поля Description "+ film);
            throw new ValidationException("Описание фильма превышает 200 символов");
        } else if (film.getDuration() <= 0) {
            log.error("Ошибка поля Duration"+ film);
            throw new ValidationException("Длительность фильма должна быть положительной");
        } else if ((film.getReleaseDate().isBefore(dateAfter))) {
            log.error("Ошибка в поле ReleaseDate "+ film);
            throw new ValidationException("Дата релиза должна быть позднее 28.12.1895");
        }
    }

    public void validationId(Film film, List<Film> films) { // ????????????????????????????????????????????????
        boolean filmId = false;
        for (Film film1 : films) {
            if (film1.getId() == film.getId()) {
                filmId = true;
                break;
            }
        }
        if (!filmId) {
            throw new ValidationException("В базе нет фильма с таким ID");
        }
    }
}
