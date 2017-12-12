package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.ChiclePadUser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EntryDaoTest {

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

    @Test
    void create() {
    }

    @Test
    void markDeleted() {
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
