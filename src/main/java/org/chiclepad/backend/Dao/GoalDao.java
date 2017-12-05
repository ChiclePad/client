package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.Goal;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class GoalDao extends EntryDao {

   GoalDao(JdbcTemplate jdbcTemplate) {
      super(jdbcTemplate);
   }

   //CREATE
   public Goal create(int userId, LocalDateTime created, String description)
         throws DuplicateKeyException {

      // First it is needed to create an entry and take its id
      int entryId = super.create(userId, created);

      String sqlInsert = "INSERT INTO goal(id, entry_id, description)"
            + " VALUES(DEFAULT ,?,?) RETURNING id ;";

      Object id = jdbcTemplate
            .queryForObject(sqlInsert, new Object[] { entryId, description },
                  Integer.class);

      return id == null ? null : new Goal(entryId, created, (int) id, description);
   }

   //READ
   public Goal get(int id) throws EmptyResultDataAccessException {
      String sqlGet = "SELECT * FROM goal"
            + " INNER JOIN  entry ON goal.entry_id = entry.id"
            + " WHERE goal.id = " + id + ";";

      return jdbcTemplate.queryForObject(sqlGet,
            (RowMapper<Goal>) (ResultSet resultSet, int rowNum) -> {
               return getGoal(resultSet);
            }
      );
   }

   private Goal getGoal(final ResultSet resultSet) throws SQLException {
      int goalId = resultSet.getInt("id");
      int entryId = resultSet.getInt("entry_id");
      LocalDateTime created = (LocalDateTime) resultSet.getObject("created");

      String description = resultSet.getString("description");

      return new Goal(entryId, created, goalId, description);
   }

   public List<Goal> getAll() {
      String sqlGetAll = "SELECT * FROM goal"
            + " INNER JOIN  entry ON goal.entry_id = entry.id;";

      return jdbcTemplate.query(sqlGetAll,
            (RowMapper<Goal>) (resultSet, rowNum) -> {
               return getGoal(resultSet);
            }
      );
   }

   //UPDATE
   public Goal update(Goal goal) throws DuplicateKeyException {

      String sqlUpdateAll = "UPDATE goal "
            + "SET description = ? WHERE id = "
            + goal.getId() + ";";
      jdbcTemplate.update(sqlUpdateAll, goal.getDescription());

      return goal;
   }

   //DELETE
   public Goal delete(Goal goal) {
      String sqlDelete = "DELETE FROM goal WHERE id = "+goal.getId();
      jdbcTemplate.update(sqlDelete);
      return goal;
   }
}
