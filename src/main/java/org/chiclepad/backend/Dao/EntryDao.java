package org.chiclepad.backend.Dao;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

abstract class EntryDao {

    private final String CREATE_ENTRY_SQL = "INSERT INTO entry(user_id, created) VALUES (?, NOW())";

    private final String MARK_DELETED_ENTRY_SQL = "INSERT INTO deleted_entry(id, entry_id, deleted_time) " +
            "VALUES (DEFAULT , ?, NOW()) " +
            "RETURNING id;";

    private final String DELETE_ENTRY_SQL = "DELETE FROM entry WHERE id = ?;";

    private final String DELETE_ALL_ENTRY_SQL = "DELETE FROM entry";

    JdbcTemplate jdbcTemplate;

    EntryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int create(int userId) throws DuplicateKeyException {
        return jdbcTemplate.queryForObject(
                CREATE_ENTRY_SQL,
                new Object[]{userId},
                Integer.class
        );
    }

    public void markDeleted(int entryId) {
        jdbcTemplate.update(MARK_DELETED_ENTRY_SQL, entryId, LocalDateTime.now());
    }

    public void delete(int entryId) {
        jdbcTemplate.update(DELETE_ENTRY_SQL,
                new Object[]{entryId},
                Integer.class
        );
    }

}
