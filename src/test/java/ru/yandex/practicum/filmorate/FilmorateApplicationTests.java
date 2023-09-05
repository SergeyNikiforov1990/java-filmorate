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
class FilmorateApplicationTests {
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
    public void addUser_shouldReturnCreatedUser() {
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);
        assertEquals(createdUser.getId(), 1);
        assertEquals(createdUser.getName(), "UserName1");
        assertEquals(createdUser.getLogin(), "login1");
        assertEquals(createdUser.getBirthday(), LocalDate.parse("1990-01-06"));
        assertEquals(createdUser.getEmail(), "mail@mail.ru");
    }

    @Test
    public void getUserById_shouldReturnUserWithMatchingId() {
        // Создаем пользователя
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);

        // Получаем пользователя из БД по идентификатору 1
        User newUser = userService.getUser(1);
        System.out.println(createdUser.toString());

        // Проверяем, что полученный пользователь имеет ожидаемые значения полей
        assertEquals(createdUser.getId(), 1);
        assertEquals(createdUser.getName(), "UserName1");
        assertEquals(createdUser.getLogin(), "login1");
        assertEquals(createdUser.getBirthday(), LocalDate.parse("1990-01-06"));
        assertEquals(createdUser.getEmail(), "mail@mail.ru");
    }

    @Test
        //Тест на получение списка всех пользователей
    void getAllUsers_shouldReturnListWithAllUsers() {
        //Создаем двух пользователей
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);

        User createdUser2 = createUser(2, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");
        userService.addUser(createdUser2);

        //Сохраняем результат выполнения метода getAllUsers() в виде списка пользователей
        List<User> userList = userService.getAllUsers();
        System.out.println(userList.toString());

        //Проверяем соответствие полученных ползователей, проверяя имена
        assertEquals(userList.get(0).getName(), "UserName1");
        assertEquals(userList.get(1).getName(), "Sergey");
    }

    @Test
    void removeUser_shouldRemoveUser() {
        //Создаем двух пользователей
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);

        User createdUser2 = createUser(2, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");
        userService.addUser(createdUser2);

        //Удаляем пользователя
        userService.deleteUser(1);

        //Сохраняем результат выполнения метода getAllUsers() в виде списка пользователей
        List<Integer> userIdList = userService.getAllUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        System.out.println(userIdList);

        //Проверяем, что первый пользователь был удален, а второй остался
        assertFalse(userIdList.contains(1));
        assertTrue(userIdList.contains(2));

        //Проверяем, что первый пользователь действительно удален и выбрасывается исключение NotFoundException
        assertThrows(DataNotFoundException.class, () -> userService.getUser(1));
    }

    @Test
    void updateUser_shouldUpdateUser() {
        //Создаем двух пользователей
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);

        User createdUser2 = createUser(1, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");

        userService.updateUser(createdUser2);
        System.out.println(createdUser2.toString());
        //Получаем пользователя из БД по идентификатору 1
        User userOptional = userService.getUser(1);
        System.out.println(userOptional.toString());

        //Проверяем соответствие идентификатора сохраненного пользователя ожидаемому
        assertEquals(userOptional.getId(), 1);
        assertEquals(userOptional.getName(), "Sergey");
        assertEquals(userOptional.getEmail(), "mail@google.com");
    }

    @Test
    void friendship_shouldConfirmThatUsersAreFriends() {
        //Создаем двух пользователей
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);

        User createdUser2 = createUser(2, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");
        userService.addUser(createdUser2);

        //Добавляем пользователя 2 в друзья
        userService.addFriend(createdUser.getId(), createdUser2.getId());

        //Проверяем, что пользователь 2 есть в списке наших друзей
        List<User> userFriend = userService.getUserFriends(createdUser.getId());
        assertEquals(userFriend.size(), 1);

        //Так как дружба у нас односторонняя, то список друзей у пользователя 2 должен быть пуст
        List<User> user2Friend = userService.getUserFriends(createdUser2.getId());
        assertEquals(user2Friend.size(), 0);
    }

    @Test
    void friendship_shouldRemoveFriend() {
        //Создаем двух пользователей
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);

        User createdUser2 = createUser(2, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");
        userService.addUser(createdUser2);

        //Добавляем пользователя 2 в друзья
        userService.addFriend(createdUser.getId(), createdUser2.getId());

        //Проверяем, что пользователь 2 есть в списке наших друзей
        List<User> userFriend = userService.getUserFriends(createdUser.getId());
        assertEquals(userFriend.size(), 1);

        userService.deleteFriend(createdUser.getId(), createdUser2.getId());
        List<User> userFriendEmptyList = userService.getUserFriends(createdUser.getId());
        assertEquals(userFriendEmptyList.size(), 0);
    }

    @Test
    void friendship_shouldReturnCommonFriends() {
        //Создаем пользователей
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);

        User createdUser2 = createUser(2, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");
        userService.addUser(createdUser2);

        User createdUser3 = createUser(3, "UserName3", "login3",
                LocalDate.parse("1990-01-08"), "mail@yandex.ru");
        userService.addUser(createdUser3);
        System.out.println(createdUser3);

        //add friendships
        userService.addFriend(createdUser.getId(), createdUser3.getId());
        userService.addFriend(createdUser2.getId(), createdUser3.getId());

        //get common friends
        List<User> commonFriend = userService.getListCommonFriends(createdUser.getId(), createdUser2.getId());
        assertEquals(commonFriend.size(), 1);
        assertEquals(commonFriend.get(0), userService.getUser(createdUser3.getId()));
    }

    @Test
    public void createFilm_shouldReturnCreatedFilm() {
        // Создаем фильм
        Genre genre = filmService.getGenreById(1);
        RatingMPA mpa = filmService.getRatingMpaById(1);
        Film createdFilm = createFilm(1, "New Film", "New film description",
                LocalDate.now(), 120, new LinkedHashSet<>(List.of(genre)), mpa);
        filmService.addFilm(createdFilm);
        Genre expectedGenre = null;
        for (Genre g : createdFilm.getGenres()) {
            expectedGenre = g;
            break; // Получаем только первый жанр
        }
        // Проверяем, что созданный фильм имеет ожидаемые значения полей
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
        // Создаем фильм
        Genre genre = filmService.getGenreById(1);
        RatingMPA mpa = filmService.getRatingMpaById(1);
        Film createdFilm = createFilm(1, "New Film", "New film description",
                LocalDate.now(), 120, new LinkedHashSet<>(List.of(genre)), mpa);
        filmService.addFilm(createdFilm);
        System.out.println(filmService.getFilm(1).toString());

        //Update film
        Genre genre2 = filmService.getGenreById(2);
        Film updatedFilm = createFilm(1, "Upd Film", "Upd film description",
                LocalDate.now().minusDays(12), 120, new LinkedHashSet<>(List.of(genre, genre2)), mpa);
        filmService.updateFilm(updatedFilm);

        //Create optional Film object
        Film filmOptional = filmService.getFilm(1);

        //Check
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
        // Создаем фильм
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
        // Создаем фильм
        Genre genre = filmService.getGenreById(1);
        RatingMPA mpa = filmService.getRatingMpaById(1);

        Film createdFilm = createFilm(1, "New Film", "New film description",
                LocalDate.now(), 120, new LinkedHashSet<>(List.of(genre)), mpa);
        filmService.addFilm(createdFilm);
        System.out.println(filmService.getFilm(1).toString());

        //Second film
        Genre genre2 = filmService.getGenreById(2);
        Film film2 = createFilm(2, "Upd Film", "Upd film description",
                LocalDate.now().minusDays(12), 120, new LinkedHashSet<>(List.of(genre, genre2)), mpa);
        filmService.addFilm(film2);

        List<Film> optionalFilmsList = filmService.getAllFilms();

        assertEquals(optionalFilmsList.size(), 2);
    }

    @Test
    public void addLikeToFilm_shouldReturnFilmWithLike() {
        // Создаем фильм
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
        // Создаем фильм
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
        // Создаем фильм
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

    @Test
    void contextLoads() {
    }

}
