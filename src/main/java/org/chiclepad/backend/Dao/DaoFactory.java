package org.chiclepad.backend.Dao;

import org.chiclepad.backend.business.DatabaseManager;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;

public enum DaoFactory {

    INSTANCE;

    private JdbcTemplate jdbcTemplate;
    private CategoryDao categoryDao;
    private ChiclePadUserDao chiclePadUserDao;
    private DiaryPageDao diaryPageDao;
    private GoalDao goalDao;
    private NoteDao noteDao;
    private TodoDao todoDao;

    DaoFactory() {
        if (!DatabaseManager.INSTANCE.isConnected()) {
            DatabaseManager.INSTANCE.connect(new File("postgress/connection.properties"));
        }

        if (DatabaseManager.INSTANCE.isConnected()) {
            this.jdbcTemplate = DatabaseManager.INSTANCE.getConnection();
        }

        this.categoryDao = new CategoryDao(this.jdbcTemplate);
        this.chiclePadUserDao = new ChiclePadUserDao(this.jdbcTemplate);
        this.diaryPageDao = new DiaryPageDao(this.jdbcTemplate);
        this.goalDao = new GoalDao(this.jdbcTemplate);
        this.noteDao = new NoteDao(this.jdbcTemplate);
        this.todoDao = new TodoDao(this.jdbcTemplate);
    }

    public CategoryDao getCategoryDao() {
        return this.categoryDao;
    }

    public ChiclePadUserDao getChiclePadUserDao() {
        return this.chiclePadUserDao;
    }

    public DiaryPageDao getDiaryPageDao() {
        return this.diaryPageDao;
    }

    public GoalDao getGoalDao() {
        return this.goalDao;
    }

    public NoteDao getNoteDao() {
        return this.noteDao;
    }

    public TodoDao getTodoDao() {
        return this.todoDao;
    }

}
