package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.Note;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class NoteDaoTest {

    private static NoteDao dao = DaoFactory.INSTANCE.getNoteDao();
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
        Note note1 = dao.create(user1.getId(), "test1", LocalDateTime.now());
        Note note2 = dao.create(user1.getId(), "test2", LocalDateTime.now());
        Note note3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(3));
        Note note4 = dao.create(user2.getId(), "test4");
        Note note5 = dao.create(user2.getId(), "test5");

        assertThat(dao.getAll(user1.getId())).containsExactlyInAnyOrder(note1, note2);
        assertThat(dao.getAll(user2.getId())).containsExactlyInAnyOrder(note3, note4, note5);
    }

    @Test
    void get() {
        Note note2 = dao.create(user1.getId(), "test2", LocalDateTime.now());
        Note note3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(3));

        assertThat(dao.get(note2.getId())).isEqualTo(note2);
        assertThat(dao.get(note3.getId())).isEqualTo(note3);
    }

    @Test
    void getAll() {
        Note note1 = dao.create(user1.getId(), "test1", LocalDateTime.now());
        Note note2 = dao.create(user1.getId(), "test2", LocalDateTime.now());
        Note note3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(3));

        dao.markDeleted(note1.getEntryId());
        dao.markDeleted(note3.getEntryId());

        assertThat(dao.getAll(user1.getId())).containsExactlyInAnyOrder(note2);
        assertThat(dao.getAll(user2.getId())).isEmpty();
    }

    @Test
    void getAllWithDeleted() {
        Note note1 = dao.create(user1.getId(), "test1", LocalDateTime.now());
        Note note2 = dao.create(user1.getId(), "test2", LocalDateTime.now());
        Note note3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(3));

        dao.markDeleted(note1.getEntryId());
        dao.markDeleted(note3.getEntryId());

        assertThat(dao.getAllWithDeleted(user1.getId())).containsExactlyInAnyOrder(note1, note2);
        assertThat(dao.getAllWithDeleted(user2.getId())).containsExactlyInAnyOrder(note3);
    }

    @Test
    void update() {
        Note note1 = dao.create(user1.getId(), "test1", LocalDateTime.now());
        Note note3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(3));

        note1.setContent("");
        LocalDateTime nextReminder = LocalDateTime.now().plusDays(20);
        note3.setReminderTime(nextReminder);

        dao.update(note1);
        dao.update(note3);

        assertThat(dao.getAll(user1.getId()))
                .extracting("content")
                .containsExactly("");
        assertThat(dao.getAll(user1.getId()))
                .extracting("reminderTime")
                .isNotNull();

        assertThat(dao.getAll(user2.getId()))
                .extracting("content")
                .containsExactly("test3");
        assertThat(dao.getAll(user2.getId()))
                .extracting("reminderTime")
                .containsExactly(Optional.of(nextReminder));
    }

    @Test
    void delete() {
        Note note1 = dao.create(user1.getId(), "test1", LocalDateTime.now());
        Note note2 = dao.create(user1.getId(), "test2", LocalDateTime.now());
        Note note3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(3));

        dao.markDeleted(note1.getEntryId());
        dao.markDeleted(note3.getEntryId());

        assertThat(dao.getAllWithDeleted(user1.getId())).containsExactlyInAnyOrder(note2, note1);
        assertThat(dao.getAllWithDeleted(user2.getId())).containsExactlyInAnyOrder(note3);

        assertThat(dao.getAll(user1.getId())).containsExactlyInAnyOrder(note2);
        assertThat(dao.getAll(user2.getId())).isEmpty();

        dao.markDeleted(note2.getEntryId());
        dao.delete(note1);
        dao.delete(note3);

        assertThat(dao.getAllWithDeleted(user1.getId())).containsExactlyInAnyOrder(note2);
        assertThat(dao.getAllWithDeleted(user2.getId())).isEmpty();

        assertThat(dao.getAll(user1.getId())).isEmpty();
        assertThat(dao.getAll(user2.getId())).isEmpty();
    }

    @Test
    void deleteAll() {
        Note note1 = dao.create(user1.getId(), "test1", LocalDateTime.now());
        Note note2 = dao.create(user1.getId(), "test2", LocalDateTime.now());
        Note note3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(3));

        dao.deleteAll();

        assertThat(dao.getAll(user1.getId())).isEmpty();
        assertThat(dao.getAll(user2.getId())).isEmpty();
    }

    @AfterAll
    static void deleteUsers() {
        userDao.deleteAll();
    }

}
