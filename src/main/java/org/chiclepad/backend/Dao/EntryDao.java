package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.Entry;
import org.chiclepad.backend.entity.Note;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

abstract class EntryDao {

    private final String CREATE_ENTRY_SQL = "INSERT INTO entry(user_id, created) VALUES (?, NOW()) RETURNING id";

    private final String GET_ALL_ENTRY_SQL = "SELECT entry.id, entry.user_id, entry.created " +
            "FROM entry " +
            "LEFT OUTER JOIN deleted_entry ON entry_id = entry.id " +
            "WHERE deleted_entry.deleted_time IS NULL AND entry.user_id = ? ;";

    private final String GET_ALL_WITH_DELETED_ENTRY_SQL = "SELECT entry.id, entry.user_id, entry.created " +
            "FROM entry " +
            "WHERE entry.user_id = ? ;";

    private final String MARK_DELETED_ENTRY_SQL = "INSERT INTO deleted_entry(entry_id, deleted_time) " +
            "VALUES (?, NOW());";

    private final String DELETE_ENTRY_SQL = "DELETE FROM entry WHERE id = ? ;";

    private final String DELETE_ALL_ENTRY_SQL = "DELETE FROM entry";

    JdbcTemplate jdbcTemplate;

    EntryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    int create(int userId) throws DuplicateKeyException {
        return jdbcTemplate.queryForObject(
                CREATE_ENTRY_SQL,
                new Object[]{userId},
                Integer.class
        );
    }

    List<Entry> getAllEntries(int userId) throws EmptyResultDataAccessException {
        return jdbcTemplate.query(
                GET_ALL_ENTRY_SQL,
                new Object[]{userId},
                (resultSet, row) -> new Note(resultSet.getInt("id"), resultSet.getInt("id"), "")
        );
    }

    List<Entry> getAllEntriesWithDeleted(int userId) throws EmptyResultDataAccessException {
        return jdbcTemplate.query(
                GET_ALL_WITH_DELETED_ENTRY_SQL,
                new Object[]{userId},
                (resultSet, row) -> new Note(resultSet.getInt("id"), resultSet.getInt("id"), "")
        );
    }

    public void markDeleted(int entryId) {
        jdbcTemplate.update(MARK_DELETED_ENTRY_SQL, entryId);
    }

    void delete(int entryId) {
        jdbcTemplate.update(DELETE_ENTRY_SQL, entryId);
    }

    void deleteAllEntries() {
        jdbcTemplate.update(DELETE_ALL_ENTRY_SQL);
    }

}
