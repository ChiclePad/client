package org.chiclepad.backend.session;

import org.chiclepad.backend.Dao.ChiclePadUserDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.assertj.core.api.Assertions.*;


class AuthentificatorTest {

    private static ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();

    @BeforeEach
    void createUser() {
        userDao.deleteAll();
        Authentificator.INSTANCE.register("alan@turing.com", "root");
        Authentificator.INSTANCE.register("dijkstra@paths.com", "root");
    }

    @Test
    void logIn() {
        assertThatThrownBy(() -> Authentificator.INSTANCE.logIn("alan@turing.com", "test"))
                .isInstanceOf(BadPasswordException.class);
        assertThatThrownBy(() -> Authentificator.INSTANCE.logIn("alan@turing223123.com", "test"))
                .isInstanceOf(EmptyResultDataAccessException.class);

        assertThatCode(() -> Authentificator.INSTANCE.logIn("alan@turing.com", "root"))
                .doesNotThrowAnyException();

        assertThat(Authentificator.INSTANCE.logIn("alan@turing.com", "root")).isNotNull();
    }

    @Test
    void register() {
        assertThatThrownBy(() -> Authentificator.INSTANCE.register("alan@turing.com", "test"))
                .isInstanceOf(UserAlreadyExistsException.class);
        assertThatCode(() -> Authentificator.INSTANCE.register("alan@turing223123.com", "test"))
                .doesNotThrowAnyException();

        assertThat(Authentificator.INSTANCE.register("alan12@turing.com", "root")).isNotNull();
    }

    @AfterAll
    static void deleteUsers() {
        userDao.deleteAll();
    }

}
