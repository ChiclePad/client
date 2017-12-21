package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.Category;
import org.chiclepad.backend.entity.Entry;
import org.chiclepad.backend.entity.Note;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

abstract class EntryDao {

    private final String CREATE_ENTRY_SQL = "INSERT INTO entry(user_id, created) VALUES (?, NOW()) RETURNING id;";

    private final String BIND_CATEGORY_TO_ENTRY_SQL = "INSERT INTO registered_category (category_id, entry_id) " +
            "VALUES (?, ?);";

    private final String GET_ALL_ENTRY_SQL = "SELECT entry.id, entry.user_id, entry.created " +
            "FROM entry " +
            "LEFT OUTER JOIN deleted_entry ON entry_id = entry.id " +
            "WHERE deleted_entry.deleted_time IS NULL AND entry.user_id = ? ;";

    private final String GET_ALL_CATEGORIES_OF_ENTRY_SQL = "SELECT category.id, category.name, category.icon, category.color " +
            "FROM entry " +
            "INNER JOIN registered_category ON entry.id = entry_id " +
            "INNER JOIN category ON category.id = category_id " +
            "WHERE entry.id = ?;";

    private final String GET_ALL_WITH_DELETED_ENTRY_SQL = "SELECT entry.id, entry.user_id, entry.created " +
            "FROM entry " +
            "WHERE entry.user_id = ? ;";

    private final String MARK_DELETED_ENTRY_SQL = "INSERT INTO deleted_entry(entry_id, deleted_time) " +
            "VALUES (?, NOW());";

    private final String DELETE_ENTRY_SQL = "DELETE FROM entry WHERE id = ? ;";

    private final String DELETE_ALL_ENTRY_SQL = "DELETE FROM entry";

    private final String UNBIND_CATEGORY_TO_ENTRY_SQL = "DELETE FROM registered_category " +
            "WHERE category_id = ? AND entry_id = ?;";

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

    public void bind(Category category, Entry entry) {
        jdbcTemplate.update(
                BIND_CATEGORY_TO_ENTRY_SQL,
                category.getId(),
                entry.getEntryId()
        );
    }

    @Deprecated
    List<Entry> getAllEntries(int userId) throws EmptyResultDataAccessException {
        return jdbcTemplate.query(
                GET_ALL_ENTRY_SQL,
                new Object[]{userId},
                (resultSet, row) -> new Note(resultSet.getInt("id"), resultSet.getInt("id"), "")
        );
    }

    @Deprecated
    List<Entry> getAllEntriesWithDeleted(int userId) throws EmptyResultDataAccessException {
        return jdbcTemplate.query(
                GET_ALL_WITH_DELETED_ENTRY_SQL,
                new Object[]{userId},
                (resultSet, row) -> new Note(resultSet.getInt("id"), resultSet.getInt("id"), "")
        );
    }

    List<Category> getAllCategoriesOfEntry(Entry entry) throws EmptyResultDataAccessException {
        return jdbcTemplate.query(
                GET_ALL_CATEGORIES_OF_ENTRY_SQL,
                new Object[]{entry.getEntryId()},
                (resultSet, row) -> readCategory(resultSet)
        );
    }

    void fetchAndSetCategories(List<? extends Entry> entries) throws EmptyResultDataAccessException {
        entries.forEach(this::fetchAndSetCategories);
    }

    void fetchAndSetCategories(Entry entry) throws EmptyResultDataAccessException {
        entry.getCategories().clear();
        entry.getCategories().addAll(getAllCategoriesOfEntry(entry));
    }

    public void markDeleted(Entry entry) {
        markDeleted(entry.getEntryId());
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

    public void unbind(Category category, Entry entry) {
        jdbcTemplate.update(
                UNBIND_CATEGORY_TO_ENTRY_SQL,
                category.getId(),
                entry.getEntryId()
        );
    }

    private Category readCategory(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String icon = resultSet.getString("icon");
        String color = resultSet.getString("color");

        return new Category(id, name, icon, color);
    }

}
