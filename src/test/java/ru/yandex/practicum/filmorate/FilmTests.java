package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
class FilmTests {
    private final UserService userService;

    private final FilmService filmService;

    private User createUser(int id, String name, String login, LocalDate birthday, String email) {
        return User.builder()
                .id(id)
                .name(name)
                .login(login)
                .birthday(birthday)
                .email(email)
                .build();
    }

    private Film createFilm(int id,
                            String name,
                            String description,
                            LocalDate releaseDate,
                            Integer duration,
                            LinkedHashSet<Genre> genres,
                            RatingMPA mpa) {
        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .genres(genres)
                .mpa(mpa)
                .build();
    }



    @Test
    public void createFilm_shouldReturnCreatedFilm() {
        Genre genre = filmService.getGenreById(1);
        RatingMPA mpa = filmService.getRatingMpaById(1);
        Film createdFilm = createFilm(1, "New Film", "New film description",
                LocalDate.now(), 120, new LinkedHashSet<>(List.of(genre)), mpa);
        filmService.addFilm(createdFilm);
        Genre expectedGenre = null;
        for (Genre g : createdFilm.getGenres()) {
            expectedGenre = g;
            break;
        }

        assertEquals(createdFilm.getId(), 1);
        assertEquals(createdFilm.getName(), "New Film");
        assertEquals(createdFilm.getDescription(), "New film description");
        assertEquals(createdFilm.getReleaseDate(), LocalDate.now());
        assertEquals(createdFilm.getDuration(), 120);
        assertEquals(createdFilm.getGenres().size(), 1);
        assertEquals(expectedGenre, genre);
        assertEquals(createdFilm.getMpa(), mpa);
    }

    @Test
    public void updateFilm_shouldReturnUpdatedFilm() {
        Genre genre = filmService.getGenreById(1);
        RatingMPA mpa = filmService.getRatingMpaById(1);
        Film createdFilm = createFilm(1, "New Film", "New film description",
                LocalDate.now(), 120, new LinkedHashSet<>(List.of(genre)), mpa);
        filmService.addFilm(createdFilm);
        System.out.println(filmService.getFilm(1).toString());

        Genre genre2 = filmService.getGenreById(2);
        Film updatedFilm = createFilm(1, "Upd Film", "Upd film description",
                LocalDate.now().minusDays(12), 120, new LinkedHashSet<>(List.of(genre, genre2)), mpa);
        filmService.updateFilm(updatedFilm);

        Film filmOptional = filmService.getFilm(1);

        assertEquals(filmOptional.getId(), 1);
        assertEquals(filmOptional.getName(), "Upd Film");
        assertEquals(filmOptional.getGenres().size(), 2);
    }

    @Test
    public void updateFilm_shouldReturnNotFoundException() {
        Genre genre = filmService.getGenreById(1);
        RatingMPA mpa = new RatingMPA(1);
        Genre genre2 = filmService.getGenreById(2);
        Film updatedFilm = createFilm(9999, "Upd Film", "Upd film description",
                LocalDate.now().minusDays(12), 120, new LinkedHashSet<>(List.of(genre)), mpa);

        assertThrows(DataNotFoundException.class, () -> filmService.updateFilm(updatedFilm));
    }

    @Test
    public void getFilm_shouldReturnFilmById() {
        Genre genre = filmService.getGenreById(1);
        RatingMPA mpa = filmService.getRatingMpaById(1);
        Film createdFilm = createFilm(1, "New Film", "New film description",
                LocalDate.now(), 120, new LinkedHashSet<>(List.of(genre)), mpa);
        filmService.addFilm(createdFilm);
        System.out.println(filmService.getFilm(1).toString());

        assertEquals(filmService.getFilm(createdFilm.getId()).getId(), createdFilm.getId());
    }

    @Test
    public void getFilm_shouldReturnAllFilmsList() {
        Genre genre = filmService.getGenreById(1);
        RatingMPA mpa = filmService.getRatingMpaById(1);

        Film createdFilm = createFilm(1, "New Film", "New film description",
                LocalDate.now(), 120, new LinkedHashSet<>(List.of(genre)), mpa);
        filmService.addFilm(createdFilm);
        System.out.println(filmService.getFilm(1).toString());

        Genre genre2 = filmService.getGenreById(2);
        Film film2 = createFilm(2, "Upd Film", "Upd film description",
                LocalDate.now().minusDays(12), 120, new LinkedHashSet<>(List.of(genre, genre2)), mpa);
        filmService.addFilm(film2);

        List<Film> optionalFilmsList = filmService.getAllFilms();

        assertEquals(optionalFilmsList.size(), 2);
    }

    @Test
    public void addLikeToFilm_shouldReturnFilmWithLike() {
        Genre genre = filmService.getGenreById(1);
        RatingMPA mpa = filmService.getRatingMpaById(1);
        Film createdFilm = createFilm(1, "New Film", "New film description",
                LocalDate.now(), 120, new LinkedHashSet<>(List.of(genre)), mpa);
        filmService.addFilm(createdFilm);
        System.out.println(filmService.getFilm(1).toString());

        User user = createUser(1, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");
        userService.addUser(user);

        filmService.addLikeFilm(createdFilm.getId(), user.getId());
        System.out.println(filmService.getFilm(1).toString());

        assertEquals(filmService.getFilmLikes(createdFilm.getId()).size(), 1);
    }

    @Test
    public void removeFilmLike_shouldReturnFilmWithoutLike() {
        Genre genre = filmService.getGenreById(1);
        RatingMPA mpa = filmService.getRatingMpaById(1);
        Film createdFilm = createFilm(1, "New Film", "New film description",
                LocalDate.now(), 120, new LinkedHashSet<>(List.of(genre)), mpa);
        filmService.addFilm(createdFilm);
        System.out.println(filmService.getFilm(1).toString());

        User user = createUser(1, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");
        userService.addUser(user);

        filmService.addLikeFilm(createdFilm.getId(), user.getId());

        assertEquals(filmService.getFilmLikes(createdFilm.getId()).size(), 1);

        filmService.deleteLikeFilm(createdFilm.getId(), user.getId());

        assertEquals(filmService.getFilmLikes(createdFilm.getId()).size(), 0);
    }

    @Test
    public void getPopular_shouldReturnTop1FilmList() {
        Genre genre = filmService.getGenreById(1);
        RatingMPA mpa = filmService.getRatingMpaById(1);
        Film createdFilm = createFilm(1, "New Film", "New film description",
                LocalDate.now(), 120, new LinkedHashSet<>(List.of(genre)), mpa);
        filmService.addFilm(createdFilm);
        System.out.println(filmService.getFilm(1).toString());

        assertEquals(filmService.getListBestMovies(1).size(), 1);
        assertEquals(filmService.getListBestMovies(1).get(0), filmService.getFilm(createdFilm.getId()));
    }

    @Test
    public void getPopular_shouldReturnTop10FilmList() {
        Genre genre = filmService.getGenreById(1);
        RatingMPA mpa = filmService.getRatingMpaById(1);
        Film createdFilm = createFilm(1, "New Film", "New film description",
                LocalDate.now(), 120, new LinkedHashSet<>(List.of(genre)), mpa);
        filmService.addFilm(createdFilm);
        System.out.println(filmService.getFilm(1).toString());

        Genre genre2 = filmService.getGenreById(2);
        RatingMPA mpa2 = filmService.getRatingMpaById(2);
        Film createdFilm2 = createFilm(1, "New Film", "New film description",
                LocalDate.now(), 120, new LinkedHashSet<>(List.of(genre, genre2)), mpa2);
        filmService.addFilm(createdFilm2);

        User user = createUser(1, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");
        userService.addUser(user);

        filmService.addLikeFilm(createdFilm2.getId(), user.getId());

        System.out.println(filmService.getListBestMovies(10));
        assertEquals(filmService.getListBestMovies(10).size(), 2);
        assertEquals(filmService.getListBestMovies(10).get(0), filmService.getFilm(createdFilm2.getId()));
    }
}
