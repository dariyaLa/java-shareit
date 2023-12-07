package ru.practicum.shareIt.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareIt.users.User;
import ru.practicum.shareIt.users.UserDto;
import ru.practicum.shareIt.users.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private EntityManager em;

    private User user;

    @Test
    void saveUserTest() {
        User newUser = User.builder()
                .name("testNameSave")
                .email("testSave@email.com")
                .build();
        userService.save(newUser);

        TypedQuery<User> query = em.createQuery("select u from User u where u.email = :email", User.class);
        User newUserSave = query.setParameter("email", newUser.getEmail()).getSingleResult();

        assertThat(newUserSave.getId(), notNullValue());
        assertThat(newUserSave.getName(), equalTo(newUser.getName()));
        assertThat(newUserSave.getEmail(), equalTo(newUser.getEmail()));

    }

    @Test
    void deleteTest() {
        userService.delete(user.getId());

        assertThrows(NoResultException.class,
                () -> {
                    TypedQuery<User> query = em.createQuery("select u from User u where u.id = :id", User.class);
                    User userFind = query.setParameter("id", user.getId()).getSingleResult();
                });
    }

    @Test
    void findTest() {
        UserDto userFind = userService.find(user.getId()).get();

        assertThat(userFind.getId(), notNullValue());
        assertThat(userFind.getName(), equalTo(user.getName()));
        assertThat(userFind.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void findStringTest() {
        User userFind = userService.find("test@email.com").get();

        assertThat(userFind.getId(), notNullValue());
        assertThat(userFind.getName(), equalTo(user.getName()));
        assertThat(userFind.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void findAllTest() {
        User newUser = User.builder()
                .name("testname")
                .email("testSave@email.com")
                .build();
        userService.save(newUser);

        Collection<UserDto> userDtoList = userService.findAll();

        assertThat(userDtoList.size(), equalTo(2));
    }

    @Test
    void updateTest() {
        user.setName("testNameUpdate");
        userService.update(user);

        TypedQuery<User> query = em.createQuery("select u from User u where u.id = :id", User.class);
        User userFind = query.setParameter("id", user.getId()).getSingleResult();

        assertThat(userFind.getId(), notNullValue());
        assertThat(userFind.getName(), equalTo("testNameUpdate"));
        assertThat(userFind.getEmail(), equalTo(user.getEmail()));
    }


    @BeforeEach
    void init() {
        user = User.builder()
                .name("testName")
                .email("test@email.com")
                .build();
        userService.save(user);
    }

    @AfterEach
    public void clean() {
        em.createNativeQuery("delete from Users").executeUpdate();

    }

}
