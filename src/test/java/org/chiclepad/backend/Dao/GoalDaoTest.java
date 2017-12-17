package org.chiclepad.backend.Dao;

import org.chiclepad.backend.business.DatabaseManager;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.chiclepad.backend.entity.CompletedGoal;
import org.chiclepad.backend.entity.Goal;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class GoalDaoTest {

    private static GoalDao dao = DaoFactory.INSTANCE.getGoalDao();
    private static ChiclePadUserDao userDao = DaoFactory.INSTANCE.getChiclePadUserDao();
    private static JdbcTemplate jdbcTemplate = DatabaseManager.INSTANCE.getConnection();

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
        Goal goal1 = dao.create(user1.getId(), "test1");
        Goal goal2 = dao.create(user1.getId(), "test2");
        Goal goal3 = dao.create(user2.getId(), "test3");

        assertThat(dao.getAll(user1.getId())).containsExactlyInAnyOrder(goal1, goal2);
        assertThat(dao.getAll(user2.getId())).containsExactlyInAnyOrder(goal3);
    }

    @Test
    void createCompletedGoal() {
        Goal goal1 = dao.create(user1.getId(), "test1");
        Goal goal2 = dao.create(user1.getId(), "test2");
        Goal goal3 = dao.create(user2.getId(), "test3");

        CompletedGoal completedGoal1 = dao.createCompletedGoal(goal1.getId());
        CompletedGoal completedGoal2 = dao.createCompletedGoal(goal1.getId());
        CompletedGoal completedGoal3 = dao.createCompletedGoal(goal2.getId());
        CompletedGoal completedGoal4 = dao.createCompletedGoal(goal3.getId());

        assertThat(dao.getCompletedGoals(goal1.getId())).containsExactlyInAnyOrder(completedGoal1, completedGoal2);
        assertThat(dao.getCompletedGoals(goal3.getId())).containsExactlyInAnyOrder(completedGoal4);
    }

    @Test
    void get() {
        Goal goal1 = dao.create(user1.getId(), "test1");
        Goal goal2 = dao.create(user1.getId(), "test2");
        Goal goal3 = dao.create(user2.getId(), "test3");

        assertThat(dao.get(goal1.getId())).isEqualTo(goal1);
        assertThat(dao.get(goal3.getId())).isEqualTo(goal3);
    }

    @Test
    void getAll() {
        Goal goal1 = dao.create(user1.getId(), "test1");
        Goal goal2 = dao.create(user1.getId(), "test2");
        Goal goal3 = dao.create(user2.getId(), "test3");

        dao.markDeleted(goal1.getEntryId());
        dao.markDeleted(goal3.getEntryId());

        assertThat(dao.getAll(user1.getId())).containsExactlyInAnyOrder(goal2);
        assertThat(dao.getAll(user2.getId())).isEmpty();
    }

    @Test
    void getAllWithDeleted() {
        Goal goal1 = dao.create(user1.getId(), "test1");
        Goal goal2 = dao.create(user1.getId(), "test2");
        Goal goal3 = dao.create(user2.getId(), "test3");

        dao.markDeleted(goal1.getEntryId());
        dao.markDeleted(goal3.getEntryId());

        assertThat(dao.getAllWithDeleted(user1.getId())).containsExactlyInAnyOrder(goal1, goal2);
        assertThat(dao.getAllWithDeleted(user2.getId())).containsExactlyInAnyOrder(goal3);
    }

    @Test
    void getAllGoalsNotCompletedToday() {
        Goal goal1 = dao.create(user1.getId(), "test1");
        Goal goal2 = dao.create(user1.getId(), "test2");
        Goal goal3 = dao.create(user2.getId(), "test3");
        Goal goal4 = dao.create(user2.getId(), "test4");

        CompletedGoal completedGoal1 = dao.createCompletedGoal(goal1.getId());
        CompletedGoal completedGoal4 = dao.createCompletedGoal(goal4.getId());

        assertThat(dao.getAllGoalsNotCompletedToday(user1.getId())).containsExactlyInAnyOrder(goal2);
        assertThat(dao.getAllGoalsNotCompletedToday(user2.getId())).containsExactlyInAnyOrder(goal3);
    }

    @Test
    void getCompletedGoals() {
        Goal goal1 = dao.create(user1.getId(), "test1");
        Goal goal2 = dao.create(user1.getId(), "test2");
        Goal goal3 = dao.create(user2.getId(), "test3");

        CompletedGoal completedGoal1 = dao.createCompletedGoal(goal1.getId());
        CompletedGoal completedGoal2 = dao.createCompletedGoal(goal1.getId());
        CompletedGoal completedGoal3 = dao.createCompletedGoal(goal2.getId());
        CompletedGoal completedGoal4 = dao.createCompletedGoal(goal3.getId());

        assertThat(dao.getCompletedGoals(goal1.getId())).containsExactlyInAnyOrder(completedGoal1, completedGoal2);
        assertThat(dao.getCompletedGoals(goal2.getId())).containsExactlyInAnyOrder(completedGoal3);
        assertThat(dao.getCompletedGoals(goal3.getId())).containsExactlyInAnyOrder(completedGoal4);
    }

    @Test
    void getFilteredCompletedGoalsCountByWeekDay() {
        Goal goal1 = dao.create(user1.getId(), "test1");
        Goal goal2 = dao.create(user1.getId(), "test2");
        Goal goal3 = dao.create(user2.getId(), "test3");

//        jdbcTemplate.update("INSERT INTO ")
    }

    @Test
    void getFilteredCompletedGoalsCountRecentDays() {
        Goal goal1 = dao.create(user1.getId(), "test1");
        Goal goal2 = dao.create(user1.getId(), "test2");
        Goal goal3 = dao.create(user2.getId(), "test3");

//        jdbcTemplate.update("INSERT INTO ")
    }

    @Test
    void update() {
        Goal goal1 = dao.create(user1.getId(), "test1");
        Goal goal2 = dao.create(user1.getId(), "test2");
        Goal goal3 = dao.create(user2.getId(), "test3");
        CompletedGoal completedGoal1 = dao.createCompletedGoal(goal1.getId());

        goal1.setDescription("");

        dao.update(goal1);

        assertThat(dao.get(goal1.getId()))
                .extracting("description")
                .containsExactly("");

        assertThat(dao.getCompletedGoals(goal1.getId())).containsExactlyInAnyOrder(completedGoal1);
    }

    @Test
    void delete() {
        Goal goal1 = dao.create(user1.getId(), "test1");
        Goal goal2 = dao.create(user1.getId(), "test2");
        Goal goal3 = dao.create(user2.getId(), "test3");

        dao.markDeleted(goal1.getEntryId());
        dao.markDeleted(goal3.getEntryId());

        assertThat(dao.getAllWithDeleted(user1.getId())).containsExactlyInAnyOrder(goal2, goal1);
        assertThat(dao.getAllWithDeleted(user2.getId())).containsExactlyInAnyOrder(goal3);

        assertThat(dao.getAll(user1.getId())).containsExactlyInAnyOrder(goal2);
        assertThat(dao.getAll(user2.getId())).isEmpty();

        dao.markDeleted(goal2.getEntryId());
        dao.delete(goal1);
        dao.delete(goal3);

        assertThat(dao.getAllWithDeleted(user1.getId())).containsExactlyInAnyOrder(goal2);
        assertThat(dao.getAllWithDeleted(user2.getId())).isEmpty();

        assertThat(dao.getAll(user1.getId())).isEmpty();
        assertThat(dao.getAll(user2.getId())).isEmpty();
    }

    @Test
    void deleteAll() {
        Goal goal1 = dao.create(user1.getId(), "test1");
        Goal goal2 = dao.create(user1.getId(), "test2");
        Goal goal3 = dao.create(user2.getId(), "test3");

        dao.deleteAll();

        assertThat(dao.getAll(user1.getId())).isEmpty();
        assertThat(dao.getAll(user2.getId())).isEmpty();
    }

    @AfterAll
    static void deleteUsers() {
        userDao.deleteAll();
    }

}
