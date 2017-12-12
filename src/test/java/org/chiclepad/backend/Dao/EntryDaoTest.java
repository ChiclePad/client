package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.ChiclePadUser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class EntryDaoTest {

    private static NoteDao dao = DaoFactory.INSTANCE.getNoteDao();
    private static ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();

    private static ChiclePadUser user1;
    private static ChiclePadUser user2;

    @BeforeAll
    static void createUser() {
        userDao.deleteAll();
        dao.deleteAllEntries();

        user1 = userDao.create("alan@turing.com", "root");
        user2 = userDao.create("dijkstra@paths.com", "root");
    }

    @AfterEach
    void emptyDatabase() {
        dao.deleteAllEntries();
    }

    @Test
    void create() {
        int entry1Id = dao.create(user1.getId());
        int entry2Id = dao.create(user1.getId());
        int entry3Id = dao.create(user2.getId());

        assertThat(dao.getAllEntries(user1.getId()))
                .extracting("entryId")
                .containsExactlyInAnyOrder(entry1Id, entry2Id);
        assertThat(dao.getAllEntries(user2.getId()))
                .extracting("entryId")
                .containsExactlyInAnyOrder(entry3Id);
    }

    @Test
    void markDeleted() {
        int entry1Id = dao.create(user1.getId());
        int entry2Id = dao.create(user1.getId());
        int entry3Id = dao.create(user2.getId());

        dao.markDeleted(entry1Id);
        dao.markDeleted(entry3Id);

        assertThat(dao.getAllEntries(user1.getId()))
                .extracting("id")
                .containsExactlyInAnyOrder(entry2Id);
        assertThat(dao.getAllEntries(user2.getId())).isEmpty();

        assertThat(dao.getAllEntriesWithDeleted(user1.getId()))
                .extracting("id")
                .containsExactlyInAnyOrder(entry1Id, entry2Id);
    }

    @Test
    void delete() {
        int entry1Id = dao.create(user1.getId());
        int entry2Id = dao.create(user1.getId());
        int entry3Id = dao.create(user2.getId());

        dao.delete(entry1Id);
        dao.delete(entry3Id);

        assertThat(dao.getAllEntries(user1.getId()))
                .extracting("entryId")
                .containsExactlyInAnyOrder(entry2Id);
        assertThat(dao.getAllEntries(user2.getId()))
                .isEmpty();
    }

    @Test
    void deleteAll() {
        int entry1Id = dao.create(user1.getId());
        int entry2Id = dao.create(user1.getId());
        int entry3Id = dao.create(user2.getId());

        dao.deleteAllEntries();

        assertThat(dao.getAllEntries(user1.getId())).isEmpty();
        assertThat(dao.getAllEntries(user2.getId())).isEmpty();
    }

    @AfterAll
    static void deleteUsers() {
        userDao.deleteAll();
    }

}
