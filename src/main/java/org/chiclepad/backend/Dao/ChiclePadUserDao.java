package org.chiclepad.backend.Dao;

import org.chiclepad.backend.LocaleUtils;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class ChiclePadUserDao {

   // Data source
   private JdbcTemplate jdbcTemplate;

   public ChiclePadUserDao(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   //CREATE
   public ChiclePadUser create(String email, String password, String salt) throws DuplicateKeyException {

      String sqlInsert = "INSERT INTO chiclepad_user(id,email, password, salt)"
            + " VALUES(DEFAULT ,?,?,?) RETURNING id ;";

      Object id = jdbcTemplate.queryForObject(sqlInsert, new Object[] { email, password, salt }, Integer.class);
      return id == null ? null : new ChiclePadUser((int) id, email, password, salt);
   }

   //READ
   public ChiclePadUser get(int id) throws EmptyResultDataAccessException {
      String sqlGet = "SELECT * FROM chiclepad_user"
            + " LEFT OUTER JOIN chiclepad_user_details ON chiclepad_user_details.user_id = " + id
            + " WHERE chiclepad_user.id =" + id + ";";

      return jdbcTemplate.queryForObject(sqlGet,
            (RowMapper<ChiclePadUser>) (ResultSet resultSet, int rowNum) -> {
               return getChiclePadUser(resultSet);
            }
      );
   }

   public ChiclePadUser get(String email) throws EmptyResultDataAccessException {
      String sqlGet = "SELECT * FROM chiclepad_user"
            + " LEFT OUTER JOIN chiclepad_user_details ON chiclepad_user_details.user_id = chiclepad_user.id "
            + "WHERE  chiclepad_user.email = \'" + email + "\' ;";

      return jdbcTemplate.queryForObject(sqlGet,
            (RowMapper<ChiclePadUser>) (ResultSet resultSet, int rowNum) -> {
               return getChiclePadUser(resultSet);
            }
      );
   }

   private ChiclePadUser getChiclePadUser(final ResultSet resultSet) throws SQLException {
      int userId = resultSet.getInt("id");
      String email = resultSet.getString("email");
      String password = resultSet.getString("password");
      String salt = resultSet.getString("salt");
      String name = resultSet.getString("name");
      String localeCode = resultSet.getString("locale");
      Locale locale = null;
      if (localeCode != null) {
         locale = LocaleUtils.localeFromCode(localeCode);
      }

      if (name != null && locale != null) {
         return new ChiclePadUser(userId, email, password, salt, locale, name);
      } else if (name != null && locale == null) {
         return new ChiclePadUser(userId, email, password, salt, name);
      } else if (name == null && locale != null) {
         return new ChiclePadUser(userId, email, password, salt, locale);
      }

      return new ChiclePadUser(userId, email, password, salt);
   }

   public List<ChiclePadUser> getAll() {
      String sqlGetAll = "SELECT * FROM chiclepad_user"
            + " LEFT OUTER JOIN chiclepad_user_details ON chiclepad_user_details.user_id = chiclepad_user.id;";

      return jdbcTemplate.query(sqlGetAll,
            (RowMapper<ChiclePadUser>) (resultSet, rowNum) -> {
               return getChiclePadUser(resultSet);
            }
      );
   }

   //UPDATE
   public ChiclePadUser update(ChiclePadUser chiclePadUser) throws DuplicateKeyException {

      String sqlUpdatePassword = "UPDATE chiclepad_user "
            + "SET password = ? WHERE id = "
            + chiclePadUser.getId() + ";";
      jdbcTemplate.update(sqlUpdatePassword, chiclePadUser.getPassword());

      String sqlAreUserDetailsPresent =
            " SELECT count(*) FROM chiclepad_user_details WHERE  user_id = " + chiclePadUser.getId() + " ;";

      int userId = -1;
      if (chiclePadUser.getLocale().isPresent() || chiclePadUser.getName().isPresent()) {
         Integer userIdQueried = jdbcTemplate.queryForObject(sqlAreUserDetailsPresent, Integer.class);
         if (userIdQueried != null) {
            userId = userIdQueried;
         }

         if (userId == 0) {
            String sqlInsertUserDetailsRow =
                  "INSERT INTO chiclepad_user_details(user_id) VALUES (" + chiclePadUser.getId() + ")";
            jdbcTemplate.update(sqlInsertUserDetailsRow);
         }

         // Set locale
         if (chiclePadUser.getLocale().isPresent()) {
            Locale locale = chiclePadUser.getLocale().get();
            String sqlUpdateLocale =
                  "UPDATE chiclepad_user_details SET locale = ? WHERE user_id = "
                        + chiclePadUser.getId()
                        + ";";
            jdbcTemplate.update(sqlUpdateLocale, locale);
         }

         // Set name
         if (chiclePadUser.getName().isPresent()) {
            String name = chiclePadUser.getName().get();
            String sqlUpdateName =
                  "UPDATE chiclepad_user_details SET name = ? WHERE user_id = "
                        + chiclePadUser.getId()
                        + ";";
            jdbcTemplate.update(sqlUpdateName, name);
         }
      }

      return chiclePadUser;
   }

   //DELETE
   public ChiclePadUser delete(ChiclePadUser chiclePadUser) {
      String sqlDelete = "DELETE  FROM chiclepad_user WHERE  id = " + chiclePadUser.getId();
      jdbcTemplate.update(sqlDelete);
      return chiclePadUser;
   }

}
