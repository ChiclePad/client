package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.Note;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class NoteDao extends EntryDao {

   NoteDao(JdbcTemplate jdbcTemplate) {
      super(jdbcTemplate);
   }

   //CREATE
   public Note create(int userId, LocalDateTime created, int positionX, int positionY, String content,
         LocalDateTime reminderTime) throws DuplicateKeyException {

      // First it is needed to create an entry and take its id
      int entryId = super.create(userId, created);

      String sqlInsert = "INSERT INTO note(id, entry_id, position_x, position_y, content, reminder_time)"
            + " VALUES(DEFAULT ,?,?,?,?,?) RETURNING id ;";

      Object id = jdbcTemplate
            .queryForObject(sqlInsert, new Object[] { entryId, positionX, positionY, content, reminderTime },
                  Integer.class);

      return id == null ? null : new Note(entryId, created, (int) id, positionX, positionY, content, reminderTime);
   }

   //READ
   public Note get(int id) throws EmptyResultDataAccessException {
      String sqlGet = "SELECT * FROM note"
            + " INNER JOIN  entry ON note.entry_id = entry.id"
            + " WHERE note.id = " + id + ";";

      return jdbcTemplate.queryForObject(sqlGet,
            (RowMapper<Note>) (ResultSet resultSet, int rowNum) -> {
               return getNote(resultSet);
            }
      );
   }

   private Note getNote(final ResultSet resultSet) throws SQLException {
      int noteId = resultSet.getInt("id");
      int entryId = resultSet.getInt("entry_id");
      LocalDateTime created = (LocalDateTime) resultSet.getObject("created");

      int positionX = resultSet.getInt("position_x");
      int positionY = resultSet.getInt("position_y");
      String content = resultSet.getString("content");

      LocalDateTime reminderTime = (LocalDateTime) resultSet.getObject("reminder_time");

      Optional<LocalDateTime> realReminderTime;
      if (reminderTime == null) {
         realReminderTime = Optional.empty();
      } else {
         realReminderTime = Optional.of(reminderTime);
      }

      return new Note(entryId, created, noteId, positionX, positionY, content, realReminderTime.get());
   }

   public List<Note> getAll() {
      String sqlGetAll = "SELECT * FROM note"
            + " INNER JOIN  entry ON note.entry_id = entry.id;";

      return jdbcTemplate.query(sqlGetAll,
            (RowMapper<Note>) (resultSet, rowNum) -> {
               return getNote(resultSet);
            }
      );
   }

   //UPDATE
   public Note update(Note note) throws DuplicateKeyException {

      String sqlUpdateAll = "UPDATE note "
            + "SET position_x = ?, position_y = ?, content = ?, reminder_time = ? WHERE id = "
            + note.getId() + ";";
      jdbcTemplate.update(sqlUpdateAll, note.getPositionX(), note.getPositionY(), note.getContent(),
            note.getReminderTime().get());

      return note;
   }

   //DELETE
   public Note delete(Note note) {
      String sqlDelete = "DELETE FROM note WHERE id = "+note.getId();
      jdbcTemplate.update(sqlDelete);
      return note;
   }
}
