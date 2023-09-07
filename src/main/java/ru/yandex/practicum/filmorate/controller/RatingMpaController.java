package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingMpaController {
    private final FilmService filmService;

    @GetMapping
    public List<RatingMPA> getAllRatings() {
        List<RatingMPA> ratings = filmService.getAllRatings();
        log.info("Получение списка всех рейтингов MPA: " + ratings);
        return ratings;
    }

    @GetMapping("/{id}")
    public RatingMPA getRatingById(@PathVariable Integer id) {
        RatingMPA rating = filmService.getRatingMpaById(id);
        log.info("Получение MPA рейтинга с id " + id);
        return rating;
    }
}
