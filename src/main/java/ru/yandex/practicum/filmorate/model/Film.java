package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Film {
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private Integer duration;
    private Set<Integer> userIdLikesFilm = new HashSet<>();
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    private RatingMPA mpa;

    /*public Film(Integer id, @NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, @NonNull Integer duration, @NonNull RatingMPA mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(@NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, @NonNull int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Set<Integer> getUserIdLikesFilm() {
        if (userIdLikesFilm == null) {
            throw new DataNotFoundException("userLikesFilm пуст"); // надо ли?
        }
        return userIdLikesFilm;
    }*/
}