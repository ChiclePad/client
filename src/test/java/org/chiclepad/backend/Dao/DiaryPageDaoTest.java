package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.DiaryPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class DiaryPageDaoTest {

    private static DiaryPageDao dao = DaoFactory.INSTANCE.getDiaryPageDao();
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
        DiaryPage page1 = dao.create(user1.getId(), "test1", LocalDate.now());
        DiaryPage page2 = dao.create(user1.getId(), "test2aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", LocalDate.now().plusDays(1));
        DiaryPage page3 = dao.create(user2.getId(), "", LocalDate.now());

        assertThat(dao.getAll(user1.getId())).containsExactlyInAnyOrder(page1, page2);
        assertThat(dao.getAll(user2.getId())).containsExactlyInAnyOrder(page3);
    }

    @Test
    void get() {
        DiaryPage page1 = dao.create(user1.getId(), "test1", LocalDate.now());
        DiaryPage page2 = dao.create(user1.getId(), "test2aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", LocalDate.now().plusDays(1));
        DiaryPage page3 = dao.create(user2.getId(), "", LocalDate.now());

        assertThat(dao.get(page1.getId())).isEqualTo(page1);
        assertThat(dao.get(page3.getId())).isEqualTo(page3);
    }

    @Test
    void getAll() {
        DiaryPage page1 = dao.create(user1.getId(), "test1", LocalDate.now());
        DiaryPage page2 = dao.create(user1.getId(), "test2aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", LocalDate.now().plusDays(1));
        DiaryPage page3 = dao.create(user2.getId(), "", LocalDate.now());
        DiaryPage page4 = dao.create(user2.getId(), "ad", LocalDate.now().plusDays(123));

        dao.markDeleted(page1.getEntryId());
        dao.markDeleted(page3.getEntryId());
        dao.markDeleted(page4.getEntryId());

        assertThat(dao.getAll(user1.getId())).containsExactlyInAnyOrder(page2);
        assertThat(dao.getAll(user2.getId())).isEmpty();
    }

    @Test
    void getAllWithDeleted() {
        DiaryPage page1 = dao.create(user1.getId(), "test1", LocalDate.now());
        DiaryPage page2 = dao.create(user1.getId(), "test2aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", LocalDate.now().plusDays(1));
        DiaryPage page3 = dao.create(user2.getId(), "", LocalDate.now());

        dao.markDeleted(page1.getEntryId());
        dao.markDeleted(page3.getEntryId());

        assertThat(dao.getAllWithDeleted(user1.getId())).containsExactlyInAnyOrder(page1, page2);
        assertThat(dao.getAllWithDeleted(user2.getId())).containsExactlyInAnyOrder(page3);
    }

    @Test
    void update() {
        DiaryPage page1 = dao.create(user1.getId(), "test1", LocalDate.now());
        DiaryPage page2 = dao.create(user1.getId(), "test2aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", LocalDate.now().plusDays(1));
        DiaryPage page3 = dao.create(user2.getId(), "test3", LocalDate.now());

        page2.setText("");
        dao.update(page2);

        assertThat(dao.get(page2.getId()))
                .extracting("text")
                .containsExactly("");
        assertThat(dao.get(page3.getId()))
                .extracting("text")
                .containsExactly("test3");
    }

    @Test
    void delete() {
        DiaryPage page1 = dao.create(user1.getId(), "test1", LocalDate.now());
        DiaryPage page2 = dao.create(user1.getId(), "test2aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", LocalDate.now().plusDays(1));
        DiaryPage page3 = dao.create(user2.getId(), "", LocalDate.now());

        dao.markDeleted(page1.getEntryId());
        dao.markDeleted(page3.getEntryId());

        assertThat(dao.getAllWithDeleted(user1.getId())).containsExactlyInAnyOrder(page1, page2);
        assertThat(dao.getAllWithDeleted(user2.getId())).containsExactlyInAnyOrder(page3);

        assertThat(dao.getAll(user1.getId())).containsExactlyInAnyOrder(page2);
        assertThat(dao.getAll(user2.getId())).isEmpty();

        dao.markDeleted(page2.getEntryId());
        dao.delete(page1);
        dao.delete(page3);

        assertThat(dao.getAllWithDeleted(user1.getId())).containsExactlyInAnyOrder(page2);
        assertThat(dao.getAllWithDeleted(user2.getId())).isEmpty();

        assertThat(dao.getAll(user1.getId())).isEmpty();
        assertThat(dao.getAll(user2.getId())).isEmpty();
    }

    @Test
    void deleteAll() {
        DiaryPage page1 = dao.create(user1.getId(), "test1", LocalDate.now());
        DiaryPage page2 = dao.create(user1.getId(), "test2aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", LocalDate.now().plusDays(1));
        DiaryPage page3 = dao.create(user2.getId(), "", LocalDate.now());

        dao.deleteAll();

        assertThat(dao.getAll(user1.getId())).isEmpty();
        assertThat(dao.getAll(user2.getId())).isEmpty();
    }

    @AfterAll
    static void deleteUsers() {
        userDao.deleteAll();
    }

}
