package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryDaoTest {

    private static CategoryDao dao = DaoFactory.INSTANCE.getCategoryDao();
    private static ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();

    private static ChiclePadUser user1;
    private static ChiclePadUser user2;

    @BeforeAll
    static void createUser() {
        userDao.deleteAll();
        dao.deleteAll();

        user1 = userDao.create("alan@turing.com", "root");
        user2 = userDao.create("dijkstra@paths.com", "root");
    }

    @AfterEach
    void emptyDatabase() {
        dao.deleteAll();
    }

    @Test
    void create() {
        Category category1 = dao.create(user1.getId(), "Skola", "CIRCLE", "#ababab");
        Category category2 = dao.create(user1.getId(), "Praca", "CIRCLE", "#ffffff");
        Category category3 = dao.create(user2.getId(), "Nakup", "SQUARE", "#123456");
        List<Category> categories = dao.getAll(user1.getId());

        assertThat(categories).containsExactly(category1, category2);
    }

    @Test
    void get() {
        Category category1 = dao.create(user1.getId(), "Skola", "CIRCLE", "#ababab");
        Category category2 = dao.create(user2.getId(), "Praca", "CIRCLE", "#ffffff");

        assertThat(dao.get(category2.getId())).isEqualTo(category2);
        assertThat(dao.get(category1.getId())).isEqualTo(category1);
    }

    @Test
    void getAll() {
        Category category1 = dao.create(user1.getId(), "Skola", "CIRCLE", "#ababab");
        Category category2 = dao.create(user1.getId(), "Praca", "CIRCLE", "#ffffff");
        Category category3 = dao.create(user1.getId(), "Nakup", "SQUARE", "#123456");

        List<Category> categories = dao.getAll(user1.getId());

        assertThat(categories).containsExactly(category1, category2, category3);
    }

    @Test
    void update() {
        Category category1 = dao.create(user1.getId(), "Skola", "CIRCLE", "#ababab");
        category1.setName("Volno");
        dao.update(category1);
        Category category = dao.get(category1.getId());
        assertThat(category.getName()).isEqualTo("Volno");
    }

    @Test
    void delete() {
        Category category1 = dao.create(user1.getId(), "Skola", "CIRCLE", "#ababab");
        Category category2 = dao.create(user1.getId(), "Praca", "CIRCLE", "#ffffff");
        Category category3 = dao.create(user2.getId(), "Nakup", "SQUARE", "#123456");
    }

    @Test
    void deleteAll() {
        Category category1 = dao.create(user1.getId(), "Skola", "CIRCLE", "#ababab");
        Category category2 = dao.create(user1.getId(), "Praca", "CIRCLE", "#ffffff");
        Category category3 = dao.create(user2.getId(), "Nakup", "SQUARE", "#123456");

        dao.deleteAll();
        assertThatThrownBy(() -> dao.get(category1.getId())).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @AfterAll
    static void deleteUsers() {
        userDao.deleteAll();
    }

}
