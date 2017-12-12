package org.chiclepad.backend.Dao;

import org.chiclepad.backend.entity.Category;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryDao {

    private final String CREATE_CATEGORY_SQL = "INSERT INTO category (name, icon, color, user_id) " +
            "VALUES (?, ?, ?, ?) " +
            "RETURNING id;";

    private final String GET_CATEGORY_SQL = "SELECT * " +
            "FROM category " +
            "WHERE id = ?;";

    private final String GET_ALL_CATEGORY_SQL = "SELECT * " +
            "FROM category " +
            "WHERE user_id = ?;";

    private final String UPDATE_CATEGORY_SQL = "UPDATE category " +
            "SET name = ?, icon = ?, color = ? " +
            "WHERE id = ?;";

    private final String DELETE_CATEGORY_SQL = "DELETE " +
            "FROM category " +
            "WHERE id = ?;";

    private final String DELETE_ALL_CATEGORY_SQL = "DELETE FROM category;";

    private JdbcTemplate jdbcTemplate;

    CategoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Category create(int userId, String name, String icon, String color) throws DuplicateKeyException {
        Object id = jdbcTemplate.queryForObject(
                CREATE_CATEGORY_SQL,
                new Object[]{name, icon, color, userId},
                Integer.class
        );
        return new Category((int) id, name, icon, color);
    }

    public Category get(int id) throws EmptyResultDataAccessException {
        return jdbcTemplate.queryForObject(
                GET_CATEGORY_SQL,
                new Object[]{id},
                (resultSet, row) -> readResult(resultSet)
        );
    }

    public List<Category> getAll(int userId) {
        return jdbcTemplate.query(
                GET_ALL_CATEGORY_SQL,
                new Object[]{userId},
                (resultSet, rowNum) -> readResult(resultSet)
        );
    }

    public Category update(Category category) throws DuplicateKeyException {
        jdbcTemplate.update(
                UPDATE_CATEGORY_SQL,
                category.getName(),
                category.getIcon(),
                category.getColor(),
                category.getId()
        );
        return category;
    }

    public Category delete(Category category) {
        jdbcTemplate.update(DELETE_CATEGORY_SQL, category.getId());
        return category;
    }

    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_CATEGORY_SQL);
    }

    private Category readResult(final ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String icon = resultSet.getString("icon");
        String color = resultSet.getString("color");

        return new Category(userId, name, icon, color);
    }

}
