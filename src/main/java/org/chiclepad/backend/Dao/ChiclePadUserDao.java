package org.chiclepad.backend.Dao;

import org.chiclepad.backend.business.LocaleUtils;
import org.chiclepad.backend.entity.ChiclePadUser;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ChiclePadUserDao {

    private final String CREATE_USER_SQL = "INSERT INTO chiclepad_user (email, password) " +
            "VALUES (?, ?) " +
            "RETURNING id;";

    private final String CREATE_DETAILS_USER_SQL = "INSERT INTO chiclepad_user_details (user_id) VALUES (?);";

    private final String GET_USER_BY_ID_SQL = "SELECT * " +
            "FROM chiclepad_user " +
            "LEFT OUTER JOIN chiclepad_user_details ON user_id = chiclepad_user.id " +
            "WHERE chiclepad_user.id = ?;";

    private final String GET_USER_BY_EMAIL_SQL = "SELECT * " +
            "FROM chiclepad_user " +
            "LEFT OUTER JOIN chiclepad_user_details ON user_id = chiclepad_user.id " +
            "WHERE lower(email) = lower(?) ;";

    private final String GET_ALL_USERS_SQL = "SELECT * " +
            "FROM chiclepad_user " +
            "LEFT OUTER JOIN chiclepad_user_details ON user_id = chiclepad_user.id;";

    private final String UPDATE_PASSWORD_USER_SQL = "UPDATE chiclepad_user " +
            "SET password = ? " +
            "WHERE id = ?;";

    private final String HAS_DETAILS_USER_SQL = "SELECT count(*) >= 1 " +
            "FROM chiclepad_user_details " +
            "WHERE  user_id = ?;";

    private final String UPDATE_DETAILS_USER_SQL = "UPDATE chiclepad_user_details " +
            "SET name = ?, locale = ? " +
            "WHERE user_id = ?;";

    private final String DELETE_DETAILS_USER_SQL = "DELETE " +
            "FROM chiclepad_user_details " +
            "WHERE user_id = ?;";

    private final String DELETE_USER_SQL = "DELETE " +
            "FROM chiclepad_user " +
            "WHERE id = ?;";

    private final String DELETE_ALL_USER_SQL = "DELETE FROM chiclepad_user;";


    private JdbcTemplate jdbcTemplate;

    ChiclePadUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ChiclePadUser create(String email, String password) throws DuplicateKeyException {
        int id = jdbcTemplate.queryForObject(
                CREATE_USER_SQL,
                new Object[]{email, password},
                Integer.class
        );
        return new ChiclePadUser(id, email, password);
    }

    public ChiclePadUser get(int id) throws EmptyResultDataAccessException {
        return jdbcTemplate.queryForObject(
                GET_USER_BY_ID_SQL,
                new Object[]{id},
                (resultSet, row) -> readUser(resultSet)
        );
    }

    public ChiclePadUser get(String email) throws EmptyResultDataAccessException {
        return jdbcTemplate.queryForObject(
                GET_USER_BY_EMAIL_SQL,
                new Object[]{email},
                (resultSet, row) -> readUser(resultSet)
        );
    }

    public List<ChiclePadUser> getAll() {
        return jdbcTemplate.query(
                GET_ALL_USERS_SQL,
                (resultSet, row) -> readUser(resultSet)
        );
    }

    public ChiclePadUser updatePassword(ChiclePadUser chiclePadUser) throws DuplicateKeyException {
        jdbcTemplate.update(UPDATE_PASSWORD_USER_SQL, chiclePadUser.getPassword(), chiclePadUser.getId());
        return chiclePadUser;
    }

    public ChiclePadUser updateDetails(ChiclePadUser chiclePadUser) throws DuplicateKeyException {
        if (chiclePadUser.getName().isPresent() || chiclePadUser.getLocale().isPresent()) {
            if (!userDetailsExist(chiclePadUser)) {
                createUserDetails(chiclePadUser);
            }

            updateUserDetails(chiclePadUser);
        } else {
            deleteUserDetails(chiclePadUser);
        }

        return chiclePadUser;
    }

    private void createUserDetails(ChiclePadUser chiclePadUser) {
        jdbcTemplate.update(CREATE_DETAILS_USER_SQL, chiclePadUser.getId());
    }

    private void updateUserDetails(ChiclePadUser chiclePadUser) {
        jdbcTemplate.update(
                UPDATE_DETAILS_USER_SQL,
                chiclePadUser.getName().orElse(null),
                LocaleUtils.codeFromLocale(chiclePadUser.getLocale().orElse(null)).orElse(null),
                chiclePadUser.getId()
        );
    }

    private void deleteUserDetails(ChiclePadUser chiclePadUser) {
        jdbcTemplate.update(
                DELETE_DETAILS_USER_SQL,
                chiclePadUser.getId()
        );
    }

    private boolean userDetailsExist(ChiclePadUser chiclePadUser) {
        return jdbcTemplate.queryForObject(
                HAS_DETAILS_USER_SQL,
                new Object[]{chiclePadUser.getId()},
                Boolean.class
        );
    }

    public ChiclePadUser delete(ChiclePadUser chiclePadUser) {
        jdbcTemplate.update(DELETE_USER_SQL, chiclePadUser.getId());
        return chiclePadUser;
    }

    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_USER_SQL);
    }

    private ChiclePadUser readUser(final ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");

        String name = resultSet.getString("name");
        String localeCode = resultSet.getString("locale");

        if (name != null && localeCode != null) {
            return new ChiclePadUser(userId, email, password, LocaleUtils.localeFromCode(localeCode), name);
        }

        if (name != null) {
            return new ChiclePadUser(userId, email, password, name);
        }

        if (localeCode != null) {
            return new ChiclePadUser(userId, email, password, LocaleUtils.localeFromCode(localeCode));
        }

        return new ChiclePadUser(userId, email, password);
    }

}
