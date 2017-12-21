package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.Entry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class EntryDaoTest {

    private static NoteDao dao = DaoFactory.INSTANCE.getNoteDao();
    private static CategoryDao categoryDao = DaoFactory.INSTANCE.getCategoryDao();
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
        int entry4Id = dao.create(user2.getId());

        assertThat(dao.getAllEntries(user1.getId()))
                .extracting("entryId")
                .containsExactlyInAnyOrder(entry1Id, entry2Id);
        assertThat(dao.getAllEntries(user2.getId()))
                .extracting("entryId")
                .containsExactlyInAnyOrder(entry3Id, entry4Id);
    }

    @Test
    void bind() {
        Entry entry1 = dao.create(user1.getId(), "test1", LocalDateTime.now());
        Entry entry2 = dao.create(user1.getId(), "test2", LocalDateTime.now());
        Entry entry3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(3));

        Category category1 = categoryDao.create(user1.getId(), "Skola", "CIRCLE", "#ababab");
        Category category2 = categoryDao.create(user1.getId(), "Praca", "CIRCLE", "#ffffff");
        Category category3 = categoryDao.create(user2.getId(), "Nakup", "SQUARE", "#123456");

        dao.bind(category1, entry1);
        dao.bind(category3, entry3);

        dao.getAll(user1.getId()).stream()
                .filter(note -> note.getEntryId() == entry1.getEntryId())
                .findFirst()
                .ifPresent(note -> assertThat(note.getCategories()).containsExactlyInAnyOrder(category1));

        assertThat(dao.getAllCategoriesOfEntry(entry1)).containsExactlyInAnyOrder(category1);
        assertThat(dao.getAllCategoriesOfEntry(entry2)).isNotNull().isEmpty();
        assertThat(dao.getAllCategoriesOfEntry(entry3)).containsExactlyInAnyOrder(category3);
    }

    @Test
    void getAllEntries() {
        int entry1Id = dao.create(user1.getId());
        int entry2Id = dao.create(user1.getId());
        int entry3Id = dao.create(user2.getId());

        dao.markDeleted(entry1Id);
        dao.markDeleted(entry3Id);

        assertThat(dao.getAllEntries(user1.getId()))
                .extracting("entryId")
                .containsExactlyInAnyOrder(entry2Id);
        assertThat(dao.getAllEntries(user2.getId()))
                .isNotNull()
                .isEmpty();
    }

    @Test
    void getAllCategoriesOfEntry() {
        Entry entry1 = dao.create(user1.getId(), "test1", LocalDateTime.now());
        Entry entry2 = dao.create(user1.getId(), "test2", LocalDateTime.now());
        Entry entry3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(3));

        Category category1 = categoryDao.create(user1.getId(), "Skola", "CIRCLE", "#ababab");
        Category category2 = categoryDao.create(user1.getId(), "Praca", "CIRCLE", "#ffffff");
        Category category3 = categoryDao.create(user2.getId(), "Nakup", "SQUARE", "#123456");

        dao.bind(category1, entry1);
        dao.bind(category3, entry3);

        assertThat(dao.getAllCategoriesOfEntry(entry1)).containsExactlyInAnyOrder(category1);
        assertThat(dao.getAllCategoriesOfEntry(entry3)).containsExactlyInAnyOrder(category3);
    }

    @Test
    void getAllEntriesWithDeleted() {
        int entry1Id = dao.create(user1.getId());
        int entry2Id = dao.create(user1.getId());
        int entry3Id = dao.create(user2.getId());
        int entry4Id = dao.create(user2.getId());

        dao.markDeleted(entry1Id);
        dao.markDeleted(entry4Id);

        assertThat(dao.getAllEntriesWithDeleted(user1.getId()))
                .extracting("entryId")
                .containsExactlyInAnyOrder(entry1Id, entry2Id);
        assertThat(dao.getAllEntriesWithDeleted(user2.getId()))
                .extracting("entryId")
                .containsExactlyInAnyOrder(entry3Id, entry4Id);
    }

    @Test
    void deleteAllEntries() {
        int entry1Id = dao.create(user1.getId());
        int entry2Id = dao.create(user1.getId());
        int entry3Id = dao.create(user2.getId());

        dao.deleteAllEntries();

        assertThat(dao.getAllEntries(user1.getId()))
                .isNotNull()
                .isEmpty();
        assertThat(dao.getAllEntries(user2.getId()))
                .isNotNull()
                .isEmpty();
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

    @Test
    void unbind() {
        Entry entry1 = dao.create(user1.getId(), "test1", LocalDateTime.now());
        Entry entry2 = dao.create(user1.getId(), "test2", LocalDateTime.now());
        Entry entry3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(3));

        Category category1 = categoryDao.create(user1.getId(), "Skola", "CIRCLE", "#ababab");
        Category category2 = categoryDao.create(user1.getId(), "Praca", "CIRCLE", "#ffffff");
        Category category3 = categoryDao.create(user2.getId(), "Nakup", "SQUARE", "#123456");

        dao.bind(category1, entry1);
        dao.bind(category3, entry3);

        assertThat(dao.getAllCategoriesOfEntry(entry1)).containsExactlyInAnyOrder(category1);
        assertThat(dao.getAllCategoriesOfEntry(entry2)).isNotNull().isEmpty();
        assertThat(dao.getAllCategoriesOfEntry(entry3)).containsExactlyInAnyOrder(category3);

        dao.unbind(category1, entry1);

        assertThat(dao.getAllCategoriesOfEntry(entry1)).isNotNull().isEmpty();
        assertThat(dao.getAllCategoriesOfEntry(entry2)).isNotNull().isEmpty();
        assertThat(dao.getAllCategoriesOfEntry(entry3)).containsExactlyInAnyOrder(category3);

        dao.unbind(category3, entry3);

        assertThat(dao.getAllCategoriesOfEntry(entry1)).isNotNull().isEmpty();
        assertThat(dao.getAllCategoriesOfEntry(entry2)).isNotNull().isEmpty();
        assertThat(dao.getAllCategoriesOfEntry(entry3)).isNotNull().isEmpty();
    }

    @AfterAll
    static void deleteUsers() {
        userDao.deleteAll();
    }

}
