package org.chiclepad.backend.Dao;

import org.chiclepad.backend.DatabaseManager;
import org.chiclepad.backend.LocaleUtils;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.print.DocFlavor;

public class ChiclePadUserDao {

   // Dara source
   private JdbcTemplate jdbcTemplate;

   protected ChiclePadUserDao(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   public ChiclePadUser create(String email, String password, String salt) throws DuplicateKeyException {
      String sqlCommand = "INSERT INTO chiclepad_user(id,email, password, salt)"
            + " VALUES(DEFAULT ,?,?,?) RETURNING id ;";

      Object id = jdbcTemplate.queryForObject(sqlCommand, new Object[] { email, password, salt }, Integer.class);

      return id == null ? null : new ChiclePadUser((int) id, email, password, salt);
   }

   public ChiclePadUser update(ChiclePadUser chiclePadUser) throws DuplicateKeyException {
      String sqlCommand1 = "UPDATE chiclepad_user "
            + "SET password = chiclePadUser.getPassword()  WHERE chiclepad_user.id = " + chiclePadUser.getId() + ";";

      jdbcTemplate.update(sqlCommand1, chiclePadUser.getEmail(), chiclePadUser.getPassword(), Integer.class);

      String sqlCommand3 =
            " SELECT count(*) FROM chiclepad_user_details WHERE  user_id = " + chiclePadUser.getId() + " ;";

      int number = jdbcTemplate.queryForObject(sqlCommand3, new Object[0], Integer.class);

      if (!chiclePadUser.getName().isPresent() || !chiclePadUser.getLocale().isPresent()) {
         if (number == 0) {
            String sql4 = "INSERT INTO chiclepad_user_details(name, locale) VALUES (" + chiclePadUser.getName() + ","
                  + chiclePadUser.getLocale() + ")";
         }

         String sqlCommand2 =
               "UPDATE chiclepad_user_details SET  chiclepad_user_details.name = " + chiclePadUser.getName() +
                     ", chiclepad_user_details.locale = "
                     + chiclePadUser.getLocale() + " WHERE chiclepad_user_details.user_id = " + chiclePadUser.getId()
                     + ";";

         jdbcTemplate.update(sqlCommand2, chiclePadUser.getEmail(), chiclePadUser.getPassword(), Integer.class);
         return null;
      }

      return chiclePadUser;
   }

   public ChiclePadUser delete(ChiclePadUser chiclePadUser) {
      String sqlCommand = "DELETE  FROM chiclepad_user WHERE  id = " + chiclePadUser.getId();
      return chiclePadUser;
   }

   public ChiclePadUser get(int id) {
      String sqlCommand = "SELECT * FROM chiclepad_user"
            + " LEFT OUTER JOIN chiclepad_user ON chiclepad_user_details.user_id = " + id
            + " WHERE id =" + id + ";";

      return jdbcTemplate.queryForObject(sqlCommand,
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
      Locale locale = LocaleUtils.localeFromCode(resultSet.getString("locale"));
      return new ChiclePadUser(userId, email, password, salt, locale, name);
   }

   public List<ChiclePadUser> getAll() {
      String sqlCommand = "SELECT * FROM chiclepad_user"
            + " LEFT OUTER JOIN chiclepad_user ON chiclepad_user_details.user_id = chiclepad_user.id;";

      return jdbcTemplate.query(sqlCommand,
            (RowMapper<ChiclePadUser>) (resultSet, rowNum) -> {
               return getChiclePadUser(resultSet);
            }
      );
   }

   public static void main(String[] args) {
      ChiclePadUserDao dao = DaoFactory.INSTANCE.getChiclePadUserDao();

      ChiclePadUser chiclePadUser = new ChiclePadUser(899689, "maria@maria.sk", "badpassword", "saltie");

      dao.create("maria@maria.sk", "badpassword", "saltie");
   }

}
