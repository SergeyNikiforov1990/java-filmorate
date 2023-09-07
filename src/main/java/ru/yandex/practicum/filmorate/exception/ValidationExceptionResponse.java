package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.*;

import java.util.Map;

@RestControllerAdvice(assignableTypes = {FilmController.class, UserController.class, FilmLikeController.class,
        GenreController.class, RatingMpaController.class, FriendController.class})

public class ValidationExceptionResponse {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> errorValidation(final ValidationException e) {
        return Map.of("Error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFoundObject(final DataNotFoundException e) {
        return Map.of("Error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> internalServerError(final IndexOutOfBoundsException e) {
        return Map.of("Error", "Internal Server Error");
    }
}
