package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.Todo;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TodoDao extends EntryDao {

    private final String CREATE_TODO_SQL = "INSERT INTO todo(entry_id, description, deadline, soft_deadline, priority) " +
            "VALUES (?, ?, ?, ?, ?) " +
            "RETURNING id ;";

    private final String GET_TODO_SQL = "SELECT * " +
            "FROM todo " +
            "INNER JOIN entry ON todo.entry_id = entry.id " +
            "WHERE todo.id = ? ;";

    private final String GET_ALL_TODO_PAGE_SQL = "SELECT * " +
            "FROM todo " +
            "INNER JOIN entry ON todo.entry_id = entry.id " +
            "LEFT OUTER JOIN deleted_entry ON deleted_entry.entry_id = entry.id " +
            "WHERE deleted_entry.deleted_time IS NULL AND user_id = ? " +
            "ORDER BY todo.deadline ASC;";

    private final String GET_ALL_WITH_DELETED_DIARY_PAGE_SQL = "SELECT * " +
            "FROM todo " +
            "INNER JOIN entry ON entry_id = entry.id " +
            "WHERE user_id = ? " +
            "ORDER BY todo.deadline ASC;";

    private final String UPDATE_DIARY_PAGE_SQL = "UPDATE todo " +
            "SET description = ?, deadline = ?, soft_deadline = ?, priority = ? " +
            "WHERE id = ?;";

    private final String DELETE_ALL_DIARY_PAGE_SQL = "DELETE FROM todo;";

    TodoDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Todo create(int userId, String description, LocalDateTime deadline, int priority)
            throws DuplicateKeyException {
        int entryId = super.create(userId);

        int id = jdbcTemplate.queryForObject(
                CREATE_TODO_SQL,
                new Object[]{entryId, description, Timestamp.valueOf(deadline), null, priority},
                Integer.class
        );

        return new Todo(entryId, id, description, deadline, priority);
    }

    public Todo create(int userId, String description, LocalDateTime deadline, LocalDateTime softDeadline, int priority)
            throws DuplicateKeyException {
        int entryId = super.create(userId);

        int id = jdbcTemplate.queryForObject(
                CREATE_TODO_SQL,
                new Object[]{entryId, description, Timestamp.valueOf(deadline), Timestamp.valueOf(softDeadline), priority},
                Integer.class
        );
        return new Todo(entryId, id, description, deadline, softDeadline, priority);
    }

    public Todo get(int id) throws EmptyResultDataAccessException {
        Todo todo = jdbcTemplate.queryForObject(
                GET_TODO_SQL,
                new Object[]{id},
                (resultSet, row) -> readTodo(resultSet)
        );

        fetchAndSetCategories(todo);
        return todo;
    }

    public List<Todo> getAll(int userId) {
        List<Todo> todos = jdbcTemplate.query(
                GET_ALL_TODO_PAGE_SQL,
                new Object[]{userId},
                (resultSet, row) -> readTodo(resultSet)
        );

        fetchAndSetCategories(todos);
        return todos;
    }

    public List<Todo> getAllWithDeleted(int userId) {
        List<Todo> todos = jdbcTemplate.query(
                GET_ALL_WITH_DELETED_DIARY_PAGE_SQL,
                new Object[]{userId},
                (resultSet, row) -> readTodo(resultSet)
        );

        fetchAndSetCategories(todos);
        return todos;
    }

    public Todo update(Todo todo) throws DuplicateKeyException {
        Timestamp softDeadline = localDateTimeToTimestamp(todo.getSoftDeadline());

        jdbcTemplate.update(
                UPDATE_DIARY_PAGE_SQL,
                todo.getDescription(),
                Timestamp.valueOf(todo.getDeadline()),
                softDeadline,
                todo.getPriority(),
                todo.getId()
        );

        return todo;
    }

    private Timestamp localDateTimeToTimestamp(Optional<LocalDateTime> softDeadline) {
        return softDeadline
                .map(Timestamp::valueOf)
                .orElse(null);
    }

    public Todo delete(Todo todo) {
        super.delete(todo.getEntryId());
        return todo;
    }

    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_DIARY_PAGE_SQL);
    }

    private Todo readTodo(final ResultSet resultSet) throws SQLException {
        int todoId = resultSet.getInt("id");
        int entryId = resultSet.getInt("entry_id");
        Timestamp deadline = resultSet.getTimestamp("deadline");
        Timestamp softDeadline = resultSet.getTimestamp("soft_deadline");
        int priority = resultSet.getInt("priority");
        String description = resultSet.getString("description");

        if (softDeadline == null) {
            return new Todo(entryId, todoId, description, deadline.toLocalDateTime(), priority);
        } else {
            return new Todo(entryId, todoId, description, deadline.toLocalDateTime(), softDeadline.toLocalDateTime(), priority);
        }
    }

}
