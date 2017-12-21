package org.chiclepad.backend.business.session;

import org.chiclepad.backend.Dao.ChiclePadUserDao;
import org.chiclepad.backend.Dao.DaoFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.assertj.core.api.Assertions.*;


class AuthenticatorTest {

    private static ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();

    @AfterAll
    static void deleteUsers() {
        userDao.deleteAll();
    }

    @BeforeEach
    void createUser() {
        userDao.deleteAll();
        Authenticator.INSTANCE.register("alan@turing.com", "root");
        Authenticator.INSTANCE.register("dijkstra@paths.com", "root");
    }

    @Test
    void logIn() {
        assertThatThrownBy(() -> Authenticator.INSTANCE.logIn("alan@turing.com", "test"))
                .isInstanceOf(BadPasswordException.class);
        assertThatThrownBy(() -> Authenticator.INSTANCE.logIn("alan@turing223123.com", "test"))
                .isInstanceOf(EmptyResultDataAccessException.class);

        assertThatCode(() -> Authenticator.INSTANCE.logIn("alan@turing.com", "root"))
                .doesNotThrowAnyException();

        assertThat(Authenticator.INSTANCE.logIn("alan@turing.com", "root")).isNotNull();
    }

    @Test
    void register() {
        assertThatThrownBy(() -> Authenticator.INSTANCE.register("alan@turing.com", "test"))
                .isInstanceOf(UserAlreadyExistsException.class);
        assertThatCode(() -> Authenticator.INSTANCE.register("alan@turing223123.com", "test"))
                .doesNotThrowAnyException();

        assertThat(Authenticator.INSTANCE.register("alan12@turing.com", "root")).isNotNull();
    }

}
