package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.CompletedGoal;
import org.chiclepad.backend.entity.Goal;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class GoalDao extends EntryDao {

    private final String CREATE_GOAL_SQL = "INSERT INTO goal (entry_id, description) " +
            "VALUES (?, ?) " +
            "RETURNING id;";

    private final String CREATE_COMPLETED_GOAL = "INSERT INTO completed_goal (goal_id, completed_day, completed_time) " +
            "VALUES (?, ?, ?) " +
            "RETURNING id;";

    private final String GET_GOAL_SQL = "SELECT * " +
            "FROM goal " +
            "INNER JOIN entry ON goal.entry_id = entry.id " +
            "WHERE goal.id = ? ;";

    private final String GET_ALL_GOAL_SQL = "SELECT * " +
            "FROM goal " +
            "INNER JOIN entry ON goal.entry_id = entry.id " +
            "LEFT OUTER JOIN deleted_entry ON deleted_entry.entry_id = entry.id " +
            "WHERE deleted_entry.deleted_time IS NULL AND entry.user_id = ? ;";

    private final String GET_ALL_WITH_DELETED_GOAL_SQL = "SELECT * " +
            "FROM goal " +
            "INNER JOIN entry ON goal.entry_id = entry.id " +
            "WHERE entry.user_id = ? ;";

    private final String GET_COMPLETED_GOALS_GOAL_SQL = "SELECT * " +
            "FROM completed_goal " +
            "INNER JOIN goal ON goal.id = completed_goal.goal_id " +
            "WHERE goal.id = ? ;";

    private final String UPDATE_GOAL_SQL = "UPDATE goal " +
            "SET description = ? " +
            "WHERE id = ?;";

    private final String DELETE_GOAL_SQL = "DELETE FROM goal WHERE id = ? ;";

    private final String DELETE_ALL_GOAL_SQL = "DELETE FROM goal;";

    GoalDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Goal create(int userId, String description) throws DuplicateKeyException {
        int entryId = super.create(userId);
        int id = jdbcTemplate.queryForObject(
                CREATE_GOAL_SQL,
                new Object[]{entryId, description},
                Integer.class
        );

        return new Goal(entryId, id, description);
    }

    public CompletedGoal createCompletedGoal(int goalId) throws DuplicateKeyException {
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();

        int id = jdbcTemplate.queryForObject(
                CREATE_COMPLETED_GOAL,
                new Object[]{goalId, Date.valueOf(date), Time.valueOf(time)},
                Integer.class
        );

        return new CompletedGoal(id, date, time);
    }

    public Goal get(int id) throws EmptyResultDataAccessException {
        return jdbcTemplate.queryForObject(
                GET_GOAL_SQL,
                new Object[]{id},
                (resultSet, row) -> readGoal(resultSet)
        );
    }

    public List<Goal> getAll(int userId) throws EmptyResultDataAccessException {
        return jdbcTemplate.query(
                GET_ALL_GOAL_SQL,
                new Object[]{userId},
                (resultSet, row) -> readGoal(resultSet)
        );
    }

    public List<Goal> getAllWithDeleted(int userId) throws EmptyResultDataAccessException {
        return jdbcTemplate.query(
                GET_ALL_WITH_DELETED_GOAL_SQL,
                new Object[]{userId},
                (resultSet, row) -> readGoal(resultSet)
        );
    }

    public List<CompletedGoal> getCompletedGoals(int goalId) throws EmptyResultDataAccessException {
        return jdbcTemplate.query(
                GET_COMPLETED_GOALS_GOAL_SQL,
                new Object[]{goalId},
                (resultSet, row) -> readCompletedGoal(resultSet)
        );
    }

    public Goal update(Goal goal) throws DuplicateKeyException {
        jdbcTemplate.update(UPDATE_GOAL_SQL, goal.getDescription(), goal.getId());
        return goal;
    }

    public Goal delete(Goal goal) {
        jdbcTemplate.update(DELETE_GOAL_SQL, goal.getId());
        return goal;
    }

    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_GOAL_SQL);
    }

    private Goal readGoal(final ResultSet resultSet) throws SQLException {
        int goalId = resultSet.getInt("id");
        int entryId = resultSet.getInt("entry_id");

        String description = resultSet.getString("description");

        return new Goal(entryId, goalId, description);
    }

    private CompletedGoal readCompletedGoal(final ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        LocalDate completedDay = resultSet.getDate("completed_day").toLocalDate();
        LocalTime completedTime = resultSet.getTime("completed_time").toLocalTime();

        return new CompletedGoal(id, completedDay, completedTime);
    }

}
