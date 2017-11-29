package org.chiclepad.backend.Dao;

import org.chiclepad.backend.LocaleUtils;
import org.chiclepad.backend.entity.Category;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class CategoryDao {
//   // Dara source
//   private JdbcTemplate jdbcTemplate;
//
//   protected CategoryDao(JdbcTemplate jdbcTemplate) {
//      this.jdbcTemplate = jdbcTemplate;
//   }
//
//   //CREATE
//   public Category create(String name, String icon, String color) throws DuplicateKeyException {
//
//      String sqlInsert = "INSERT INTO category(id, name, icon, color)"
//            + " VALUES(DEFAULT ,?,?,?) RETURNING id ;";
//
//      Object id = null;
//      try {
//         id = jdbcTemplate.queryForObject(sqlInsert, new Object[] { name, icon, color }, Integer.class);
//      } catch (DuplicateKeyException e) {
//         System.out.println("This user already exists.");
//      }
//
//      return id == null ? null : new Category((int) id, name, icon, color);
//   }
//
//   //READ
//   public Category get(int id) {
//      String sqlGet = "SELECT * FROM category"
//            + " LEFT OUTER JOIN category_details ON category_details.user_id = " + id
//            + " WHERE category.id =" + id + ";";
//
//      try {
//         return jdbcTemplate.queryForObject(sqlGet,
//               (RowMapper<Category>) (ResultSet resultSet, int rowNum) -> {
//                  return getCategory(resultSet);
//               }
//         );
//      } catch (EmptyResultDataAccessException e) {
//         System.out.println("User with this id: " + id + " does not exist.");
//      }
//      return null;
//   }
//
//   private Category getCategory(final ResultSet resultSet) throws SQLException {
//      int userId = resultSet.getInt("id");
//      String email = resultSet.getString("email");
//      String password = resultSet.getString("password");
//      String salt = resultSet.getString("salt");
//      String name = resultSet.getString("name");
//      String localeCode = resultSet.getString("locale");
//      Locale locale = null;
//
//
//
//      return new Category(userId, email, password, salt);
//   }
//
//   public List<Category> getAll() {
//      String sqlGetAll = "SELECT * FROM category"
//            + " LEFT OUTER JOIN category_details ON category_details.user_id = category.id;";
//
//      return jdbcTemplate.query(sqlGetAll,
//            (RowMapper<Category>) (resultSet, rowNum) -> {
//               return getCategory(resultSet);
//            }
//      );
//   }
//
//   //UPDATE
//   public Category update(Category category) throws DuplicateKeyException {
////
////      String sqlUpdatePassword = "UPDATE category "
////            + "SET password = ? WHERE id = "
////            + category.getId() + ";";
////      jdbcTemplate.update(sqlUpdatePassword, category.getPassword());
////
////      String sqlAreUserDetailsPresent =
////            " SELECT count(*) FROM category_details WHERE  user_id = " + category.getId() + " ;";
////
////      int userId = -1;
////      if (category.getLocale().isPresent() || category.getName().isPresent()) {
////         Integer userIdQueried = jdbcTemplate.queryForObject(sqlAreUserDetailsPresent, Integer.class);
////         if (userIdQueried != null) {
////            userId = userIdQueried;
////         }
////
////         if (userId == 0) {
////            String sqlInsertUserDetailsRow =
////                  "INSERT INTO category_details(user_id) VALUES (" + category.getId() + ")";
////            jdbcTemplate.update(sqlInsertUserDetailsRow);
////         }
////
////         // Set locale
////         if (category.getLocale().isPresent()) {
////            Locale locale = category.getLocale().get();
////            String sqlUpdateLocale =
////                  "UPDATE category_details SET locale = ? WHERE user_id = "
////                        + category.getId()
////                        + ";";
////            jdbcTemplate.update(sqlUpdateLocale, locale);
////         }
////
////         // Set name
////         if (category.getName().isPresent()) {
////            String name = category.getName().get();
////            String sqlUpdateName =
////                  "UPDATE category_details SET name = ? WHERE user_id = "
////                        + category.getId()
////                        + ";";
////            jdbcTemplate.update(sqlUpdateName, name);
////         }
////      }
//
//      return category;
//   }
//
//   //DELETE
//   public Category delete(Category category) {
//      String sqlDelete = "DELETE  FROM category WHERE  id = " + category.getId();
//      jdbcTemplate.update(sqlDelete);
//      return category;
//   }


}
