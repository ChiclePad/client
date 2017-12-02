package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.DiaryPage;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DiaryPageDao extends EntryDao {
   protected DiaryPageDao(JdbcTemplate jdbcTemplate) {
      super(jdbcTemplate);
   }

   //CREATE
   public DiaryPage create(int userId, LocalDateTime created, String text, LocalDate recordedDay)
         throws DuplicateKeyException {

      // First it is needed to create an entry and take its id
      int entryId = super.create(userId, created);

      String sqlInsert = "INSERT INTO diary_page(id, entry_id, text, recorded_day)"
            + " VALUES(DEFAULT ,?,?,?) RETURNING id ;";

      Object id = jdbcTemplate
            .queryForObject(sqlInsert, new Object[] { entryId, text, recordedDay },
                  Integer.class);

      return id == null ? null : new DiaryPage(entryId, created, (int) id, text, recordedDay);
   }

   //READ
   public DiaryPage get(int id) throws EmptyResultDataAccessException {
      String sqlGet = "SELECT * FROM diary_page"
            + " INNER JOIN  entry ON diary_page.entry_id = entry.id"
            + " WHERE diaryPage.id = " + id + ";";

      return jdbcTemplate.queryForObject(sqlGet,
            (RowMapper<DiaryPage>) (ResultSet resultSet, int rowNum) -> {
               return getDiaryPage(resultSet);
            }
      );
   }

   private DiaryPage getDiaryPage(final ResultSet resultSet) throws SQLException {
      int diaryPageId = resultSet.getInt("id");
      int entryId = resultSet.getInt("entry_id");
      LocalDateTime created = (LocalDateTime) resultSet.getObject("created");

      String text = resultSet.getString("text");
      LocalDate recordedDay = resultSet.getDate("recorded_day").toLocalDate();

      return new DiaryPage(entryId, created, diaryPageId, text, recordedDay);
   }

   public List<DiaryPage> getAll() {
      String sqlGetAll = "SELECT * FROM diary_page"
            + " INNER JOIN  entry ON diaryPage.entry_id = entry.id;";

      return jdbcTemplate.query(sqlGetAll,
            (RowMapper<DiaryPage>) (resultSet, rowNum) -> {
               return getDiaryPage(resultSet);
            }
      );
   }

   //UPDATE
   public DiaryPage update(DiaryPage diaryPage) throws DuplicateKeyException {

      String sqlUpdateAll = "UPDATE diary_page "
            + "SET text = ?, recorded_day = ? WHERE id = "
            + diaryPage.getId() + ";";
      jdbcTemplate.update(sqlUpdateAll, diaryPage.getText(), diaryPage.getRecordedDay());

      return diaryPage;
   }

   //DELETE
   public DiaryPage delete(DiaryPage diaryPage) {
      String sqlDelete = "DELETE FROM diary_page WHERE id = "+diaryPage.getId();
      jdbcTemplate.update(sqlDelete);
      return diaryPage;
   }
}
