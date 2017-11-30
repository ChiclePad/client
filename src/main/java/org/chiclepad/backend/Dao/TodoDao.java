package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.Todo;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TodoDao extends EntryDao {

   protected TodoDao(JdbcTemplate jdbcTemplate) {
      super(jdbcTemplate);
   }

   //CREATE
   public Todo create(int userId, LocalDateTime created, String description, LocalDateTime deadline,
         Optional<LocalDateTime> softDeadline, int priority) throws DuplicateKeyException {

      // First it is needed to create an entry and take its id
      int entryId = super.create(userId, created);

      String sqlInsert = "INSERT INTO todo(id, entry_id, description, deadline, soft_deadline, priority)"
            + " VALUES(DEFAULT ,?,?,?,?,?) RETURNING id ;";

      Object id = jdbcTemplate
            .queryForObject(sqlInsert, new Object[] { entryId, description, deadline, softDeadline.get(), priority },
                  Integer.class);

      return id == null ? null : new Todo(entryId, created, (int) id, description, deadline, priority);
   }

   //READ
   public Todo get(int id) throws EmptyResultDataAccessException {
      String sqlGet = "SELECT * FROM todo"
            + " INNER JOIN  entry ON todo.entry_id = entry.id"
            + " WHERE todo.id = " + id + ";";

      return jdbcTemplate.queryForObject(sqlGet,
            (RowMapper<Todo>) (ResultSet resultSet, int rowNum) -> {
               return getTodo(resultSet);
            }
      );
   }

   private Todo getTodo(final ResultSet resultSet) throws SQLException {
      int todoId = resultSet.getInt("id");
      int entryId = resultSet.getInt("entry_id");
      LocalDateTime created = (LocalDateTime) resultSet.getObject("created");
      LocalDateTime deadline = (LocalDateTime) resultSet.getObject("deadline");
      int priority = resultSet.getInt("priority");
      String description = resultSet.getString("description");
      LocalDateTime softDeadline = (LocalDateTime) resultSet.getObject("soft_deadline");

      Optional<LocalDateTime> realSoftDeadline;
      if (softDeadline == null) {
         realSoftDeadline = Optional.empty();
      } else {
         realSoftDeadline = Optional.of(softDeadline);
      }

      return new Todo(entryId, created, todoId, description, deadline, realSoftDeadline.get(), priority);
   }

   public List<Todo> getAll() {
      String sqlGetAll = "SELECT * FROM todo"
            + " INNER JOIN  entry ON todo.entry_id = entry.id;";

      return jdbcTemplate.query(sqlGetAll,
            (RowMapper<Todo>) (resultSet, rowNum) -> {
               return getTodo(resultSet);
            }
      );
   }

   //UPDATE
   public Todo update(Todo todo) throws DuplicateKeyException {

      String sqlUpdateAll = "UPDATE todo "
            + "SET description = ?, deadline = ?, soft_deadline = ?, priority = ? WHERE id = "
            + todo.getId() + ";";
      jdbcTemplate.update(sqlUpdateAll, todo.getDescription(), todo.getDeadline(), todo.getSoftDeadline().get(),
            todo.getPriority());

      return todo;
   }

   //DELETE
   public Todo delete(Todo todo) {
      super.delete(todo.getEntryId());
      return todo;
   }
   
}
