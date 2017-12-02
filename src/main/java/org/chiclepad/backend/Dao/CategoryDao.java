package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.Category;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryDao {
   // Data source
   private JdbcTemplate jdbcTemplate;

   public CategoryDao(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   //CREATE
   public Category create(String name, String icon, String color) throws DuplicateKeyException {

      String sqlInsert = "INSERT INTO category(id,name, icon, color)"
            + " VALUES(DEFAULT ,?,?,?) RETURNING id ;";

      Object id = jdbcTemplate.queryForObject(sqlInsert, new Object[] { name, icon, color }, Integer.class);
      return id == null ? null : new Category((int) id, name, icon, color);
   }

   //READ
   public Category get(int id) throws EmptyResultDataAccessException {
      String sqlGet = "SELECT * FROM category"
            + " WHERE category.id =" + id + ";";

      return jdbcTemplate.queryForObject(sqlGet,
            (RowMapper<Category>) (ResultSet resultSet, int rowNum) -> {
               return getCategory(resultSet);
            }
      );
   }

   private Category getCategory(final ResultSet resultSet) throws SQLException {
      int userId = resultSet.getInt("id");
      String name = resultSet.getString("name");
      String icon = resultSet.getString("icon");
      String color = resultSet.getString("color");

      return new Category(userId, name, icon, color);
   }

   public List<Category> getAll() {
      String sqlGetAll = "SELECT * FROM category"
            + " LEFT OUTER JOIN category_details ON category_details.user_id = category.id;";

      return jdbcTemplate.query(sqlGetAll,
            (RowMapper<Category>) (resultSet, rowNum) -> {
               return getCategory(resultSet);
            }
      );
   }

   //UPDATE
   public Category update(Category category) throws DuplicateKeyException {

      String sqlUpdateAll= "UPDATE category "
            + "SET name = ?, icon = ?, color = ? WHERE id = "
            + category.getId() + ";";
      jdbcTemplate.update(sqlUpdateAll, category.getName(), category.getIcon(), category.getColor());

      return category;
   }

   //DELETE
   public Category delete(Category category) {
      String sqlDelete = "DELETE  FROM category WHERE  id = " + category.getId();
      jdbcTemplate.update(sqlDelete);
      return category;
   }

}
