package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.Note;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class NoteDao extends EntryDao {

    private final String CREATE_NOTE_SQL = "INSERT INTO note(entry_id, content) " +
            "VALUES (?, ?) " +
            "RETURNING id;";

    private final String CREATE_NOTE_WITH_REMINDER_SQL = "INSERT INTO note(entry_id, content, reminder_time) " +
            "VALUES (?, ?, ?) " +
            "RETURNING id;";

    private final String GET_NOTE_SQL = "SELECT * " +
            "FROM note " +
            "INNER JOIN entry ON note.entry_id = entry.id " +
            "WHERE note.id = ? ;";

    private final String GET_ALL_NOTE_SQL = "SELECT * " +
            "FROM note " +
            "INNER JOIN entry ON note.entry_id = entry.id " +
            "LEFT OUTER JOIN deleted_entry ON deleted_entry.entry_id = entry.id " +
            "WHERE deleted_entry.deleted_time IS NULL AND entry.user_id = ? ;";

    private final String GET_ALL_WITH_DELETED_NOTE_SQL = "SELECT * " +
            "FROM note " +
            "INNER JOIN entry ON note.entry_id = entry.id " +
            "WHERE entry.user_id = ? ;";

    private final String UPDATE_NOTE_SQL = "UPDATE note " +
            "SET content = ?, reminder_time = ? " +
            "WHERE id = ?;";

    private final String DELETE_NOTE_SQL = "DELETE FROM note WHERE id = ?;";

    private final String DELETE_ALL_NOTE_SQL = "DELETE FROM note;";

    NoteDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Note create(int userId, String content) throws DuplicateKeyException {
        int entryId = super.create(userId);
        int id = jdbcTemplate.queryForObject(
                CREATE_NOTE_SQL,
                new Object[]{entryId, content},
                Integer.class
        );

        return new Note(entryId, id, content);
    }

    public Note create(int userId, String content, LocalDateTime reminderTime) throws DuplicateKeyException {
        int entryId = super.create(userId);
        int id = jdbcTemplate.queryForObject(
                CREATE_NOTE_WITH_REMINDER_SQL,
                new Object[]{entryId, content, Timestamp.valueOf(reminderTime)},
                Integer.class
        );

        return new Note(entryId, id, content, reminderTime);
    }

    public Note get(int id) throws EmptyResultDataAccessException {
        Note note = jdbcTemplate.queryForObject(
                GET_NOTE_SQL,
                new Object[]{id},
                (resultSet, row) -> readNote(resultSet)
        );

        fetchAndSetCategories(note);
        return note;
    }

    public List<Note> getAll(int userId) throws EmptyResultDataAccessException {
        List<Note> notes = jdbcTemplate.query(
                GET_ALL_NOTE_SQL,
                new Object[]{userId},
                (resultSet, row) -> readNote(resultSet)
        );

        fetchAndSetCategories(notes);
        return notes;
    }

    public List<Note> getAllWithDeleted(int userId) throws EmptyResultDataAccessException {
        List<Note> notes = jdbcTemplate.query(
                GET_ALL_WITH_DELETED_NOTE_SQL,
                new Object[]{userId},
                (resultSet, row) -> readNote(resultSet)
        );

        fetchAndSetCategories(notes);
        return notes;
    }

    public Note update(Note note) throws DuplicateKeyException {
        Timestamp reminderTime = reminderTimeToTimestamp(note);
        jdbcTemplate.update(UPDATE_NOTE_SQL, note.getContent(), reminderTime, note.getId());
        return note;
    }

    private Timestamp reminderTimeToTimestamp(Note note) {
        return note.getReminderTime()
                .map(Timestamp::valueOf)
                .orElse(null);
    }

    public Note delete(Note note) {
        jdbcTemplate.update(DELETE_NOTE_SQL, note.getId());
        return note;
    }

    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_NOTE_SQL);
    }

    private Note readNote(final ResultSet resultSet) throws SQLException {
        int noteId = resultSet.getInt("id");
        int entryId = resultSet.getInt("entry_id");
        String content = resultSet.getString("content");
        Timestamp reminderTime = resultSet.getTimestamp("reminder_time");

        if (reminderTime != null) {
            return new Note(entryId, noteId, content, reminderTime.toLocalDateTime());
        } else {
            return new Note(entryId, noteId, content);
        }
    }

}
