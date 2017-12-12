package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.ChiclePadUser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GoalDaoTest {

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
    }

    @Test
    void createCopletedGoal() {
    }

    @Test
    void get() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getAllWithDeleted() {
    }

    @Test
    void getCompletedGoals() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void deleteAll() {
    }

    @AfterAll
    static void deleteUsers() {
        userDao.deleteAll();
    }

}
