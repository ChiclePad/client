package org.chiclepad.backend.Dao;

import org.chiclepad.backend.business.LocaleUtils;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChiclePadUserDaoTest {

    private static ChiclePadUserDao dao = DaoFactory.INSTANCE.getChiclePadUserDao();

    @BeforeEach
    void emptyDatabase() {
        dao.deleteAll();
    }

    @Test
    void create() {
        ChiclePadUser user1 = dao.create("alan@turing.com", "root");
        ChiclePadUser user2 = dao.create("dijkstra@paths.com", "root");
        assertThatThrownBy(() -> dao.create("alan@turing.com", "root1"))
                .isInstanceOf(DuplicateKeyException.class);

        assertThat(dao.getAll()).containsExactlyInAnyOrder(user1, user2);
    }

    @Test
    void get() {
        ChiclePadUser user1 = dao.create("alan@turing.com", "root");
        ChiclePadUser user2 = dao.create("dijkstra@paths.com", "root");

        assertThat(dao.get(user1.getId())).isEqualTo(user1);
        assertThat(dao.get(user2.getEmail())).isEqualTo(user2);
    }

    @Test
    void getAll() {
        ChiclePadUser user1 = dao.create("alan@turing.com", "root");
        ChiclePadUser user2 = dao.create("dijkstra@paths.com", "root");
        ChiclePadUser user3 = dao.create("simon.kocurek@gmail.com", "root");

        assertThat(dao.getAll()).containsExactlyInAnyOrder(user1, user2, user3);
    }

    @Test
    void updatePassword() {
        ChiclePadUser user = dao.create("alan@turing.com", "root");
        user.setPassword("test");
        dao.updatePassword(user);
        String newPassword = dao.get(user.getId()).getPassword();
        assertThat(newPassword).isEqualTo("test");
    }

    @Test
    void updateDetails() {
        ChiclePadUser user = dao.create("alan@turing.com", "root");

        user.setName("Alanko");
        dao.updateDetails(user);
        ChiclePadUser updatedUser = dao.get(user.getId());
        assertThat(updatedUser.getName().isPresent()).isTrue();
        assertThat(updatedUser.getLocale().isPresent()).isFalse();

        user.setName("Test");
        user.setLocale(LocaleUtils.getAllLocals().get(0));
        dao.updateDetails(user);
        updatedUser = dao.get(user.getId());
        assertThat(updatedUser.getLocale().isPresent()).isTrue();
        assertThat(updatedUser.getName().isPresent()).isTrue();
    }

    @Test
    void delete() {
        ChiclePadUser user1 = dao.create("alan@turing.com", "root");
        ChiclePadUser user2 = dao.create("dijkstra@paths.com", "root");
        ChiclePadUser user3 = dao.create("simon.kocurek@gmail.com", "root");

        dao.delete(user1);
        assertThat(dao.getAll()).containsExactlyInAnyOrder(user2, user3);
    }

    @Test
    void deleteAll() {
        ChiclePadUser user1 = dao.create("alan@turing.com", "root");
        ChiclePadUser user2 = dao.create("dijkstra@paths.com", "root");
        ChiclePadUser user3 = dao.create("simon.kocurek@gmail.com", "root");

        dao.deleteAll();
        assertThat(dao.getAll().isEmpty()).isTrue();
    }

    @AfterAll
    static void deleteUsers() {
        dao.deleteAll();
    }

}
