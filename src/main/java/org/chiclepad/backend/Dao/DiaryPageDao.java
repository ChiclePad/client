package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.DiaryPage;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DiaryPageDao extends EntryDao {

    private final String CREATE_DIARY_PAGE_SQL = "INSERT INTO diary_page (entry_id, text, recorded_day) " +
            "VALUES (?, ?, ?) " +
            "RETURNING id;";

    private final String GET_DIARY_PAGE_SQL = "SELECT * " +
            "FROM diary_page " +
            "INNER JOIN entry ON entry_id = entry.id " +
            "WHERE diary_page.id = ? ;";

    private final String GET_ALL_DIARY_PAGE_SQL = "SELECT * " +
            "FROM diary_page " +
            "INNER JOIN entry ON diary_page.entry_id = entry.id " +
            "LEFT OUTER JOIN deleted_entry ON deleted_entry.entry_id = entry.id " +
            "WHERE deleted_entry.deleted_time IS NULL AND user_id = ? " +
            "ORDER BY diary_page.recorded_day DESC;";

    private final String GET_ALL_WITH_DELETED_DIARY_PAGE_SQL = "SELECT * " +
            "FROM diary_page " +
            "INNER JOIN entry ON entry_id = entry.id " +
            "WHERE user_id = ? " +
            "ORDER BY diary_page.recorded_day DESC;";

    private final String UPDATE_DIARY_PAGE_SQL = "UPDATE diary_page " +
            "SET text = ?, recorded_day = ? " +
            "WHERE id = ?;";

    private final String DELETE_ALL_DIARY_PAGE_SQL = "DELETE FROM diary_page;";

    DiaryPageDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public DiaryPage create(int userId, String text, LocalDate recordedDay) throws DuplicateKeyException {
        int entryId = super.create(userId);

        int id = jdbcTemplate.queryForObject(
                CREATE_DIARY_PAGE_SQL,
                new Object[]{entryId, text, Date.valueOf(recordedDay)},
                Integer.class
        );
        return new DiaryPage(entryId, id, text, recordedDay);
    }

    public DiaryPage get(int id) throws EmptyResultDataAccessException {
        DiaryPage diaryPage = jdbcTemplate.queryForObject(
                GET_DIARY_PAGE_SQL,
                new Object[]{id},
                (resultSet, row) -> readDiaryPage(resultSet)
        );

        fetchAndSetCategories(diaryPage);
        return diaryPage;
    }

    public List<DiaryPage> getAll(int userId) throws EmptyResultDataAccessException {
        List<DiaryPage> diaryPages = jdbcTemplate.query(
                GET_ALL_DIARY_PAGE_SQL,
                new Object[]{userId},
                (resultSet, row) -> readDiaryPage(resultSet)
        );

        fetchAndSetCategories(diaryPages);
        return diaryPages;
    }

    public List<DiaryPage> getAllWithDeleted(int userId) throws EmptyResultDataAccessException {
        List<DiaryPage> diaryPages = jdbcTemplate.query(
                GET_ALL_WITH_DELETED_DIARY_PAGE_SQL,
                new Object[]{userId},
                (resultSet, row) -> readDiaryPage(resultSet)
        );

        fetchAndSetCategories(diaryPages);
        return diaryPages;
    }

    public DiaryPage update(DiaryPage diaryPage) throws DuplicateKeyException {
        jdbcTemplate.update(UPDATE_DIARY_PAGE_SQL, diaryPage.getText(), Date.valueOf(diaryPage.getRecordedDay()), diaryPage.getId());
        return diaryPage;
    }

    public DiaryPage delete(DiaryPage diaryPage) {
        delete(diaryPage.getEntryId());
        return diaryPage;
    }

    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_DIARY_PAGE_SQL);
    }

    private DiaryPage readDiaryPage(final ResultSet resultSet) throws SQLException {
        int diaryPageId = resultSet.getInt("id");
        int entryId = resultSet.getInt("entry_id");

        String text = resultSet.getString("text");
        LocalDate recordedDay = resultSet.getDate("recorded_day").toLocalDate();

        return new DiaryPage(entryId, diaryPageId, text, recordedDay);
    }

}
