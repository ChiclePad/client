package org.chiclepad.backend.Dao;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

abstract class EntryDao {

   // Data source
   protected JdbcTemplate jdbcTemplate;

   EntryDao(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   //CREATE
   public int create(int userId, LocalDateTime created) throws DuplicateKeyException {

      String sqlInsert = "INSERT INTO entry(id,user_id, created)"
            + " VALUES(DEFAULT ,?,?) RETURNING id ;";

      Object id = jdbcTemplate.queryForObject(sqlInsert, new Object[] { userId, created }, Integer.class);

      return id == null ? -1 : (int) id;
   }

   //DELETE
   // Deletion by user, entry is not deleted for real, only put to deleted entries.
   // Returns deleted_entry.id
   public int delete(int entryId) {
      String sqlAddToDeletedEntries = "INSERT INTO deleted_entry(id,entry_id, deleted_time)"
            + " VALUES(DEFAULT ,?,?) RETURNING id ;";

      Object id = jdbcTemplate
            .queryForObject(sqlAddToDeletedEntries, new Object[] { entryId, LocalDateTime.now() }, Integer.class);
      return id == null ? -1 : (int) id;
   }

}
