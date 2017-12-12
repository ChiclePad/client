package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.Todo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class TodoDaoTest {

    private static TodoDao dao = DaoFactory.INSTANCE.getTodoDao();
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
        Todo todo1 = dao.create(user1.getId(), "test1", LocalDateTime.now(), 20);
        Todo todo2 = dao.create(user1.getId(), "test2", LocalDateTime.now().plusDays(1), LocalDateTime.now(), -1);
        Todo todo3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(4), LocalDateTime.now(), 10);

        assertThat(dao.getAll(user1.getId())).containsExactlyInAnyOrder(todo1, todo2);
        assertThat(dao.getAll(user2.getId())).containsExactlyInAnyOrder(todo3);
    }

    @Test
    void get() {
        Todo todo1 = dao.create(user1.getId(), "test1", LocalDateTime.now(), 20);
        Todo todo2 = dao.create(user1.getId(), "test2", LocalDateTime.now().plusDays(1), LocalDateTime.now(), -1);
        Todo todo3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(4), LocalDateTime.now(), 10);

        assertThat(dao.get(todo1.getId())).isEqualTo(todo1);
        assertThat(dao.get(todo3.getId())).isEqualTo(todo3);
    }

    @Test
    void getAll() {
        Todo todo1 = dao.create(user1.getId(), "test1", LocalDateTime.now(), 20);
        Todo todo2 = dao.create(user1.getId(), "test2", LocalDateTime.now().plusDays(1), LocalDateTime.now(), -1);
        Todo todo3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(4), LocalDateTime.now(), 10);

        dao.markDeleted(todo1.getEntryId());
        dao.markDeleted(todo3.getEntryId());

        assertThat(dao.getAll(user1.getId())).containsExactlyInAnyOrder(todo2);
        assertThat(dao.getAll(user2.getId())).isEmpty();
    }

    @Test
    void getAllWithDeleted() {
        Todo todo1 = dao.create(user1.getId(), "test1", LocalDateTime.now(), 20);
        Todo todo2 = dao.create(user1.getId(), "test2", LocalDateTime.now().plusDays(1), LocalDateTime.now(), -1);
        Todo todo3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(4), LocalDateTime.now(), 10);

        dao.markDeleted(todo1.getEntryId());
        dao.markDeleted(todo3.getEntryId());

        assertThat(dao.getAllWithDeleted(user1.getId())).containsExactlyInAnyOrder(todo1, todo2);
        assertThat(dao.getAllWithDeleted(user2.getId())).containsExactlyInAnyOrder(todo3);
    }

    @Test
    void update() {
        Todo todo1 = dao.create(user1.getId(), "test1", LocalDateTime.now(), 20);
        Todo todo2 = dao.create(user1.getId(), "test2", LocalDateTime.now().plusDays(1), LocalDateTime.now(), -1);
        Todo todo3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(4), LocalDateTime.now(), 10);

        LocalDateTime deadline = LocalDateTime.now().plusDays(20);
        todo1.setDeadline(deadline);
        todo1.setSoftDeadline(deadline.minusDays(10));

        todo3.setDescription("");
        todo3.setPriority(3);

        dao.update(todo1);
        dao.update(todo3);

        assertThat(dao.get(todo1.getId()))
                .extracting("deadline")
                .containsExactly(deadline);
        assertThat(dao.get(todo1.getId()))
                .extracting("softDeadline")
                .containsExactly(Optional.of(deadline.minusDays(10)));

        assertThat(dao.get(todo3.getId()))
                .extracting("description")
                .containsExactly("");
        assertThat(dao.get(todo3.getId()))
                .extracting("priority")
                .containsExactly(3);
    }

    @Test
    void delete() {
        Todo todo1 = dao.create(user1.getId(), "test1", LocalDateTime.now(), 20);
        Todo todo2 = dao.create(user1.getId(), "test2", LocalDateTime.now().plusDays(1), LocalDateTime.now(), -1);
        Todo todo3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(4), LocalDateTime.now(), 10);

        dao.markDeleted(todo1.getEntryId());
        dao.markDeleted(todo3.getEntryId());

        assertThat(dao.getAllWithDeleted(user1.getId())).containsExactlyInAnyOrder(todo1, todo2);
        assertThat(dao.getAllWithDeleted(user2.getId())).containsExactlyInAnyOrder(todo3);

        assertThat(dao.getAll(user1.getId())).containsExactlyInAnyOrder(todo2);
        assertThat(dao.getAll(user2.getId())).isEmpty();

        dao.markDeleted(todo2.getEntryId());
        dao.delete(todo1);
        dao.delete(todo3);

        assertThat(dao.getAllWithDeleted(user1.getId())).containsExactlyInAnyOrder(todo2);
        assertThat(dao.getAllWithDeleted(user2.getId())).isEmpty();

        assertThat(dao.getAll(user1.getId())).isEmpty();
        assertThat(dao.getAll(user2.getId())).isEmpty();
    }

    @Test
    void deleteAll() {
        Todo todo1 = dao.create(user1.getId(), "test1", LocalDateTime.now(), 20);
        Todo todo2 = dao.create(user1.getId(), "test2", LocalDateTime.now().plusDays(1), LocalDateTime.now(), -1);
        Todo todo3 = dao.create(user2.getId(), "test3", LocalDateTime.now().plusDays(4), LocalDateTime.now(), 10);

        dao.deleteAll();

        assertThat(dao.getAll(user1.getId())).isEmpty();
        assertThat(dao.getAll(user2.getId())).isEmpty();
    }

    @AfterAll
    static void deleteUsers() {
        userDao.deleteAll();
    }

}
