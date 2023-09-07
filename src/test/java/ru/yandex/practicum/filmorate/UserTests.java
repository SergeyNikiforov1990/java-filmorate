package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class UserTests {

    private final UserService userService;

    private User createUser(int id, String name, String login, LocalDate birthday, String email) {
        return User.builder()
                .id(id)
                .name(name)
                .login(login)
                .birthday(birthday)
                .email(email)
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
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);

        User newUser = userService.getUser(1);
        System.out.println(createdUser.toString());

        assertEquals(newUser.getId(), 1);
        assertEquals(newUser.getName(), "UserName1");
        assertEquals(newUser.getLogin(), "login1");
        assertEquals(newUser.getBirthday(), LocalDate.parse("1990-01-06"));
        assertEquals(newUser.getEmail(), "mail@mail.ru");
    }

    @Test
    void getAllUsers_shouldReturnListWithAllUsers() {
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);

        User createdUser2 = createUser(2, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");
        userService.addUser(createdUser2);

        List<User> userList = userService.getAllUsers();
        System.out.println(userList.toString());

        assertEquals(userList.get(0).getName(), "UserName1");
        assertEquals(userList.get(1).getName(), "Sergey");
    }

    @Test
    void removeUser_shouldRemoveUser() {
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);

        User createdUser2 = createUser(2, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");
        userService.addUser(createdUser2);

        userService.deleteUser(1);

        List<Integer> userIdList = userService.getAllUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        System.out.println(userIdList);

        assertFalse(userIdList.contains(1));
        assertTrue(userIdList.contains(2));

        assertThrows(DataNotFoundException.class, () -> userService.getUser(1));
    }

    @Test
    void updateUser_shouldUpdateUser() {
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);

        User createdUser2 = createUser(1, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");

        userService.updateUser(createdUser2);
        System.out.println(createdUser2.toString());
        User userOptional = userService.getUser(1);
        System.out.println(userOptional.toString());

        assertEquals(userOptional.getId(), 1);
        assertEquals(userOptional.getName(), "Sergey");
        assertEquals(userOptional.getEmail(), "mail@google.com");
    }

    @Test
    void friendship_shouldConfirmThatUsersAreFriends() {
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);

        User createdUser2 = createUser(2, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");
        userService.addUser(createdUser2);

        userService.addFriend(createdUser.getId(), createdUser2.getId());

        List<User> userFriend = userService.getUserFriends(createdUser.getId());
        assertEquals(userFriend.size(), 1);

        List<User> user2Friend = userService.getUserFriends(createdUser2.getId());
        assertEquals(user2Friend.size(), 0);
    }

    @Test
    void friendship_shouldRemoveFriend() {
        User createdUser = createUser(1, "UserName1", "login1",
                LocalDate.parse("1990-01-06"), "mail@mail.ru");
        userService.addUser(createdUser);

        User createdUser2 = createUser(2, "Sergey", "Sega",
                LocalDate.parse("1990-01-07"), "mail@google.com");
        userService.addUser(createdUser2);

        userService.addFriend(createdUser.getId(), createdUser2.getId());

        List<User> userFriend = userService.getUserFriends(createdUser.getId());
        assertEquals(userFriend.size(), 1);

        userService.deleteFriend(createdUser.getId(), createdUser2.getId());
        List<User> userFriendEmptyList = userService.getUserFriends(createdUser.getId());
        assertEquals(userFriendEmptyList.size(), 0);
    }

    @Test
    void friendship_shouldReturnCommonFriends() {
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

        userService.addFriend(createdUser.getId(), createdUser3.getId());
        userService.addFriend(createdUser2.getId(), createdUser3.getId());

        List<User> commonFriend = userService.getListCommonFriends(createdUser.getId(), createdUser2.getId());
        assertEquals(commonFriend.size(), 1);
        assertEquals(commonFriend.get(0), userService.getUser(createdUser3.getId()));
    }
}
